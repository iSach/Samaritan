package be.isach.samaritan.exception;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.exception
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 14:26
 */
public class BrainfuckException extends RuntimeException {

    public BrainfuckException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public BrainfuckException(String string) {
        super(string);
    }
}
