package be.isach.samaritan.command;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 19th May, 2016
 * at 20:48
 */
class CommandShuffle extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandShuffle(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        getSamaritan().getSongPlayer(getGuild()).toggleShuffleMode();
        boolean on = getSamaritan().getSongPlayer(getGuild()).isShuffleModeActive();
        if (on) getMessageChannel().sendMessage("Shuffle Mode turned on.");
        else getMessageChannel().sendMessage("Shuffle Mode turned off.");
    }
}
