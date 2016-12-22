package be.isach.samaritan.command;

import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Created by sacha on 22/12/16.
 */
public abstract class PanelCommand extends Command {
    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    PanelCommand(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        if(!getGuild().getId().equals("184045680245997568")) {
            return;
        }

        onCall(args);
    }

    abstract void onCall(String[] args);
}
