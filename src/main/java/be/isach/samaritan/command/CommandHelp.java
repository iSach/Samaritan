package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 18th May, 2016
 * at 23:51
 * <p>
 * Shows commands.
 */
class CommandHelp extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandHelp(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when the command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" \n\n__***Available commands:***__ \n");
        for (CommandsRegistry commands : CommandsRegistry.values())
            stringBuilder.append((commands.isPublic() ? "[PUBLIC]" : "[ADMIN]") + "  **£" + commands.getAliases().get(0)
                    + "** _" + commands.getDescription() + "_\n");
        getMessageChannel().sendMessage(stringBuilder.toString());
    }
}
