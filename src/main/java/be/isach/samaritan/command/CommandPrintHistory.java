package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 15:46
 */
public class CommandPrintHistory extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandPrintHistory(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called on command Execution.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        int size = 100;
        if (args != null) {
            try {
                size = Integer.parseInt(args[0]);
            } catch (Exception ignored) {
                getMessageChannel().sendMessage("Invalid Number provided.");
            }
        } else {
            getMessageChannel().sendMessage("How many messages do you want to print to hastebin?");
            try {
                size = Integer.parseInt(nextMessage().getContent().split(" ")[0]);
            } catch (Exception ignored) {
                getMessageChannel().sendMessage("Invalid Number provided.");
            }
        }
        getSamaritan().getMessageHistoryPrinter().printHistory(size, getMessageChannel());
    }
}
