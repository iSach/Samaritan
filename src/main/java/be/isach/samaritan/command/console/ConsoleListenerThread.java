package be.isach.samaritan.command.console;

import be.isach.samaritan.Samaritan;

import java.util.Scanner;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command.console
 * Created by: Sacha
 * Created on: 29th mai, 2016
 * at 03:04 am
 * <p>
 * Represents the Console Command Listener.
 */
public class ConsoleListenerThread extends Thread {

    /**
     * Samaritan instance.
     */
    private Samaritan samaritan;

    /**
     * ConsoleListenerThread constructor.
     *
     * @param samaritan Samaritan instance
     */
    public ConsoleListenerThread(Samaritan samaritan) {
        this.samaritan = samaritan;
    }

    /**
     * Called when Thread is started.
     * Starts an infinite loop, running until Samaritan exits.
     * This loop scans constantly the console inputs.
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.nextLine().equals("stop")) {
                samaritan.shutdown(true);
            }
        }
    }

}
