package be.isach.samaritan.command;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;

import java.time.ZoneId;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 24th June, 2016
 * at 01:07
 */
class CommandClean extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandClean(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        MessageHistory messageHistory = new MessageHistory(getMessageChannel());
        messageHistory.retrieve(150);
        for (Message message : messageHistory.getRecent()) {
            if (message.getAuthor().isBot()
                    && message.getContent().contains(buildStringFromArgs())) {
                message.deleteMessage();
                return;
            }
        }
    }
}
