package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 29th mai, 2016
 *             at 02:56
 */
class CommandSay extends Command{

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandSay(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        String toSay = buildStringFromArgs();
        if (toSay.isEmpty()) {
            getMessageChannel().sendMessage("What do you want to say?");
            toSay = nextMessage().getContent();
        }
        getMessageChannel().sendMessage(toSay);
    }
}
