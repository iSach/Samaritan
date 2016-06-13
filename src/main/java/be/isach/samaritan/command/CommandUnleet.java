package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 13th June, 2016
 * at 22:19
 */
public class CommandUnleet extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandUnleet(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        getMessageChannel().sendTyping();
        String str = buildStringFromArgs();
        String[] clonedArray = Arrays.copyOf(CommandLeet.LEET_ARRAY, CommandLeet.LEET_ARRAY.length);
        Arrays.sort(clonedArray, (s1, s2) -> (s2.length() - s1.length()));
        for (int i = 0; i < clonedArray.length; i++) {
            str = str.replace(clonedArray[i], CommandLeet.ENGLISH_ARRAY[i]);
        }
        getMessageChannel().sendMessage("```" + str + "```");
    }

}
