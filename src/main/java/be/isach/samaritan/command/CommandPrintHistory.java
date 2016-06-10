package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 10th juin, 2016
 * at 11:14
 */
class CommandPrintHistory extends Command{

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

    @Override
    void onExecute(String[] args) {
        MessageChannel messageChannel = getMessageChannel();
        int size = 100;
        if (args != null) {
            try {
                size = Integer.parseInt(args[0]);
            } catch (Exception ignored) {
                messageChannel.sendMessage("Invalid Number provided.");
            }

        } else {
            messageChannel.sendMessage("How many messages do you want to print to hastebin?");
            try {
                size = Integer.parseInt(nextMessage().getContent());
            } catch (Exception exc) {
                messageChannel.sendMessage("Invalid Number provided.");
            }

        }
        getSamaritan().getMessageHistoryPrinter().printHistory(size, messageChannel);
    }
}
