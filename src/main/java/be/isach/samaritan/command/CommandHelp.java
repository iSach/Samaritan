package be.isach.samaritan.command;

import be.isach.samaritan.util.EmoteUnicodeUtil;
import be.isach.samaritan.util.MathUtils;
import be.isach.samaritan.util.SamaritanConstants;
import be.isach.samaritan.util.TextUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import org.joda.time.Instant;

import java.awt.*;

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

    private String messageId;
    private static final int COMMANDS_PER_PAGE = 10;

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandHelp(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);

        getJda().addEventListener(this);
    }

    /**
     * Called when the command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        int page = 1;
        if (args != null && args.length > 0 && !args[0].isEmpty()) {
            if(MathUtils.isInteger(args[0])) {
                page = Integer.parseInt(args[0]);
            } else {
                onHelpForCommand(args[0]);
                return;
            }
        }
        page = Math.max(1, page);
        page = Math.min(getMaxPages(), page);
        showHelp(page);
    }

    private void showHelp(int page) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        {
            embedBuilder.setTitle("Available commands (Total: " + Commands.values().length + "):");
            embedBuilder.setColor(new Color(255, 255, 255));
            embedBuilder.setDescription("Page " + page + "/" + getMaxPages() + "  (-help [page])");

            StringBuilder aliasesSb = new StringBuilder();
            StringBuilder descriptionSb = new StringBuilder();
            StringBuilder accessLevelSb = new StringBuilder();

            int from = (page - 1) * COMMANDS_PER_PAGE;
            int to = Math.min(Commands.values().length - 1, COMMANDS_PER_PAGE * page - 1);
            for (int i = from; i <= to; i++) {
                Commands commandType = Commands.values()[i];
                aliasesSb.append("-" + commandType.getAliases().get(0)).append("\n");
                descriptionSb.append(commandType.getDescription()).append("\n");
                accessLevelSb.append(commandType.getRequiredAccessLevel()).append("\n");
            }

            embedBuilder.addField("Alias", aliasesSb.toString(), true);
            embedBuilder.addField("Description", descriptionSb.toString(), true);
            embedBuilder.addField("Required Access Level", accessLevelSb.toString(), true);
        }
        getMessageChannel().sendMessage(embedBuilder.build()).queue(message -> {
//            messageId = message.getId();
//            message.addReaction(EmoteUnicodeUtil.getUnicode("track_previous")).queue();
//            message.addReaction(EmoteUnicodeUtil.getUnicode("arrow_left")).queue();
//            message.addReaction(EmoteUnicodeUtil.getUnicode("arrow_right")).queue();
//            message.addReaction(EmoteUnicodeUtil.getUnicode("track_next")).queue();
        });
    }

    @Override
    public void onEvent(Event e) {
        super.onEvent(e);


        if(e instanceof MessageReactionAddEvent) {
            MessageReactionAddEvent event = ((MessageReactionAddEvent) e);
            if(event.getMessageId().equals(messageId)
                    && !event.getUser().equals(getJda().getSelfUser())) {
                try {
                    sleep(1500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                event.getReaction().removeReaction(getJda().getUserById("93721838093352960"));
            }
        }
    }

    private int getMaxPages() {
        return (int)Math.ceil(Commands.values().length / (double)COMMANDS_PER_PAGE);
    }

    private void onHelpForCommand(String commandLabel) {
        if (!Commands.isValidCommandAlias(commandLabel)) {
            getMessageChannel().sendMessage("```" + commandLabel + "``` isn't a valid command!").queue();
            return;
        }
        Commands commandType = Commands.fromAlias(commandLabel);
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
        getMessageChannel().sendMessage(stringBuilder).queue();
    }
}
