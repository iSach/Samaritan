package be.isach.samaritan.command;

import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Created by sachalewin on 2/07/16.
 */
public class CommandGithub extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandGithub(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        getMessageChannel().sendMessage("Samaritan is entirely open-source.\nSource: http://github.com/iSach/Samaritan");
    }
}
