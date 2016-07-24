package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

/**
 * Package: be.isach.samaritan.command
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class CommandPokeGo extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandPokeGo(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        getMessageChannel().sendMessage()
    }
}
