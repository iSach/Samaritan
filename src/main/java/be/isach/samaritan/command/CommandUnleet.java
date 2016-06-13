package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

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
        String leetMessage = "```";
        for (int i = 0; i < str.length(); ++i)
            leetMessage += getCharFromLeet(str.charAt(i) + "");
        getMessageChannel().sendMessage(leetMessage +"```");
    }

    private String getCharFromLeet(String s) {
        for (int i = 0; i < CommandLeet.LEET_ARRAY.length; i++) {
            if (s.toLowerCase().equals(CommandLeet.LEET_ARRAY[i].toLowerCase())) return CommandLeet.ENGLISH_ARRAY[i];
        }
        return s;
    }

}
