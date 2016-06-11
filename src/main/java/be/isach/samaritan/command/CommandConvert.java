package be.isach.samaritan.command;

import be.isach.samaritan.music.AudioFilesManager;
import net.dv8tion.jda.entities.MessageChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 11th June, 2016
 * at 21:30
 */
public class CommandConvert extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandConvert(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        AudioFilesManager.checkforSongsToConvert();
    }

}
