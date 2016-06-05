package be.isach.samaritan.brainfuck;

import be.isach.samaritan.exception.BrainfuckException;

import java.nio.ByteBuffer;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.brainfuck
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 15:21
 */
public class BrainfuckInterpreter {

    public static final int MAX_CYCLE_COUNT = 10000;

    /**
     * Processes Brainfuck Code.
     *
     * @param brainfuckCode
     * @return Process Result.
     */
    public String processCode(String brainfuckCode, ByteBuffer bytes, char[] code) {
        int data = 0;
        char[] inChars = brainfuckCode.toCharArray();
        int inChar = 0;
        StringBuilder result = new StringBuilder();
        int cycleCount = 0;
        for (int instruction = 0; instruction < code.length; ++instruction) {
            cycleCount++;
            if (cycleCount > MAX_CYCLE_COUNT) {
                throw new BrainfuckException("Program exceeded the maximum cycle count of " + MAX_CYCLE_COUNT);
            }
            char command = code[instruction];
            switch (command) {
                case '>':
                    ++data;
                    break;
                case '<':
                    --data;
                    if (data < 0) {
                        throw new BrainfuckException("Data pointer out of bounds: " + data);
                    }
                    break;
                case '+':
                    bytes.put(data, (byte) (bytes.get(data) + 1));
                    break;
                case '-':
                    bytes.put(data, (byte) (bytes.get(data) - 1));
                    break;
                case '.':
                    result.append((char) bytes.get(data));
                    break;
                case ',':
                    try {
                        bytes.put(data, (byte) inChars[inChar++]);
                        break;
                    } catch (IndexOutOfBoundsException ex) {
                        throw new BrainfuckException("Input out of bounds at position: " + (inChar - 1), ex);
                    }
                case '[':
                    if (bytes.get(data) == 0) {
                        int depth = 1;
                        do {
                            command = code[++instruction];
                            if (command == '[') {
                                ++depth;
                            } else if (command == ']') {
                                --depth;
                            }
                        } while (depth > 0);
                    }
                    break;
                case ']':
                    if (bytes.get(data) != 0) {
                        int depth = -1;
                        do {
                            command = code[--instruction];
                            if (command == '[') {
                                ++depth;
                            } else if (command == ']') {
                                --depth;
                            }
                        } while (depth < 0);
                    }
                    break;
            }
        }
        return result.toString();
    }
}
