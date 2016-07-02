package be.isach.samaritan.command;

import be.isach.samaritan.brainfuck.BrainfuckInterpreter;
import be.isach.samaritan.exception.BrainfuckException;
import net.dv8tion.jda.entities.MessageChannel;

import java.nio.ByteBuffer;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 05th juin, 2016
// * at 14:23
// */
class CommandBrainfuck extends Command {

    ByteBuffer bytes = null;

    char[] code;

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandBrainfuck(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        String input = buildStringFromArgs();
        if (buildStringFromArgs().isEmpty()) {
            getMessageChannel().sendMessage("Please enter your Brainfuck code.");
            input = nextMessage().getContent();
        }

        code = input.toCharArray();
        bytes = ByteBuffer.allocateDirect(1024 * 1024 * 8);
        String inputArg = "";

        try {
            inputArg = args[2];
        } catch (Exception ignored) {

        }

        inputArg = inputArg.replaceAll("ZERO", String.valueOf((char) 0));

        try {
            String out = getSamaritan().getBrainfuckInterpreter().processCode(inputArg, bytes, code);
            getMessageChannel().sendMessage("Brainfuck Code: `" + input + "`\n\nResult: `" + out + "`");
        } catch (BrainfuckException bra) {
            getMessageChannel().sendMessage("Program exceeded the maximum cycle count. (" + BrainfuckInterpreter.MAX_CYCLE_COUNT + ")");
        }
    }
}
