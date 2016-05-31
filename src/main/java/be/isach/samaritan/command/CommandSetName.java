package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 29th May, 2016
 * at 02:38
 */
class CommandSetName extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandSetName(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        String newName = buildStringFromArgs();
        if (newName.isEmpty()) {
            getMessageChannel().sendMessage("What do you want as new name?");
            newName = nextMessage().getContent();
        }
        getJda().getAccountManager().setUsername(newName + " [S]");
        getJda().getAccountManager().update();
        getMessageChannel().sendMessage("What a cool name!");
    }
}
