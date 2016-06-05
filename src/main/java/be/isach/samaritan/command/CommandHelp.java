package be.isach.samaritan.command;

import be.isach.samaritan.util.SamaritanConstants;
import be.isach.samaritan.util.TextUtil;
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
        if (args != null && args.length > 0 && !args[0].isEmpty()) {
            onHelpForCommand(args[0]);
            return;
        }
        int totalScale = CommandType.longestStringLength() + 7;
        int totalScaleDesc = CommandType.longestDescriptionLength() + 6;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```");
        stringBuilder.append(" \nAvailable commands: \n\n\n");
        stringBuilder.append("Alias").append(TextUtil.getSpaces(totalScale - "Alias".length())).append(" ");
        stringBuilder.append("Description").append(TextUtil.getSpaces(totalScaleDesc - "Description".length()));
        stringBuilder.append("Required Access Level");
        stringBuilder.append("\n\n");
        for (CommandType commandType : CommandType.values()) {
            String access = commandType.getRequiredAccessLevel() + "";
            String alias = commandType.getAliases().get(0) + TextUtil.getSpaces(totalScale - commandType.getAliases().get(0).length());
            String desc = commandType.getDescription() + TextUtil.getSpaces(totalScaleDesc - commandType.getDescription().length());
            stringBuilder.append(SamaritanConstants.PREFIX).append(alias);
            stringBuilder.append(desc);
            stringBuilder.append(access);
            stringBuilder.append("\n");
        }
        stringBuilder.append("```");
        getMessageChannel().sendMessage(stringBuilder.toString());
    }

    private void onHelpForCommand(String commandLabel) {
        if (!CommandType.isValidCommandAlias(commandLabel)) {
            getMessageChannel().sendMessage("```" + commandLabel + "``` isn't a valid command!");
            return;
        }
        CommandType commandType = CommandType.fromAlias(commandLabel);
        String stringBuilder = "\nCommand " +
                commandLabel +
                "\n" +
                "Aliases: `" +
                TextUtil.formatAliasesList(commandType.getAliases()) +
                "`\n" +
                "Description: `" +
                commandType.getDescription() +
                "`\n" +
                "Required Access Level: `" +
                commandType.getRequiredAccessLevel() +
                "`\n";
        getMessageChannel().sendMessage(stringBuilder);
    }
}
