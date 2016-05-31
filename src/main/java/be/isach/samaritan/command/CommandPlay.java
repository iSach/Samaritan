package be.isach.samaritan.command;

import be.isach.samaritan.util.MathUtils;
import net.dv8tion.jda.entities.MessageChannel;

import java.io.File;
import java.util.Map;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 19th May, 2016
 * at 20:20
 */
class CommandPlay extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandPlay(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        int id = 0;
        if (buildStringFromArgs().isEmpty() || !MathUtils.isInteger(buildStringFromArgs().split(" ")[0])) {
            getMessageChannel().sendMessage("Please enter the song ID.");
            String s = nextMessage().getContent();
            if (!MathUtils.isInteger(s)) {
                id = 0;
            } else id = Integer.parseInt(s);
        } else if (MathUtils.isInteger(buildStringFromArgs().split(" ")[0]))
            id = Integer.parseInt(buildStringFromArgs().split(" ")[0]);
        Map<Integer, File> songs = getSamaritan().getSongPlayer(getGuild()).getSongs();
        getSamaritan().getSongPlayer(getGuild()).playSong(id);
    }
}
