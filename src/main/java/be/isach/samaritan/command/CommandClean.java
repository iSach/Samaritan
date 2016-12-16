package be.isach.samaritan.command;

import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

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
        messageHistory.retrievePast(150).queue((messages -> {

            for (Message message : messages) {
                if (message.getAuthor().isBot()
                        && message.getContent().contains(buildStringFromArgs())) {
                    message.deleteMessage();
                    return;
                }
            }
        }));
    }
}
