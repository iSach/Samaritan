package be.isach.samaritan.command;

import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Arrays;

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
        for (String aClonedArray : clonedArray) {
            str = str.replace(aClonedArray, CommandLeet.ENGLISH_ARRAY[getIndex(aClonedArray)]);
        }
        getMessageChannel().sendMessage("```" + str + "```");
    }

    private int getIndex(String s) {
        for(int i = 0; i < CommandLeet.LEET_ARRAY.length; i++) {
            if(CommandLeet.LEET_ARRAY[i].toLowerCase().equals(s.toLowerCase())) return i;
        }
        return 0;
    }

}
