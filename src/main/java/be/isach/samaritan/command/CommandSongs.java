package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;

import java.io.File;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 19th mai, 2016
 * at 20:10
 */
class CommandSongs extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandSongs(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * List available .mp3 files in music folder.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        getMessageChannel().sendMessage("\nHere's a list of all the songs available.");
        File file = new File("music").getAbsoluteFile();
        if (!file.exists()) {
            getMessageChannel().sendMessage("No songs available.");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        getSamaritan().getSongPlayers().get(getGuild()).getSongs().entrySet().forEach(song -> {
            stringBuilder.append("[#" + song.getKey() + "] Song Name: " + song.getValue().getName() + "\n");
        });
        getMessageChannel().sendMessage(stringBuilder.toString());
    }
}
