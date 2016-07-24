package be.isach.samaritan.listener;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.command.CommandType;
import be.isach.samaritan.util.SamaritanConstants;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 29th mai, 2016
 * at 03:01
 */
public class CommandListener extends ListenerAdapter {

    /**
     * Samaritan instance.
     */
    private Samaritan samaritan;

    /**
     * CommandListener Constructor.
     *
     * @param samaritan Samaritan instance.
     */
    public CommandListener(Samaritan samaritan) {
        this.samaritan = samaritan;
    }

    /**
     * Called when a message is received and starts with £.
     *
     * @param event The MessageReceivedEvent, containing data.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getMessage().getContent().startsWith(Character.toString(SamaritanConstants.PREFIX))) return;

        if(event.getAuthor().isBot()) return;

        commandLoop:
        for (String s : event.getMessage().getContent().split(" && ")) {
            String commandFiltered = s.replaceFirst(Character.toString(SamaritanConstants.PREFIX), "");
            String commandLabel = commandFiltered.split(" ")[0];
            for (CommandType commandType : CommandType.values()) {
                if (commandType.correspondsTo(commandLabel)) {
                    if (!samaritan.getAccessLevelManager().hasAccessLevel(commandType.getRequiredAccessLevel(), event.getAuthor())) {
                        event.getChannel().sendMessage("You don't have the required access level for that! (you have: "
                                + samaritan.getAccessLevelManager().getAccessLevel(event.getAuthor()) + ", required: " +
                                commandType.getRequiredAccessLevel() + ")");
                        try {
                            event.getMessage().deleteMessage();
                        } catch (Exception exc) {
                            System.out.println("Samaritan -> Couldn't delete message: " + event.getMessage().getId());
                        }
                        return;
                    }
                    String[] g = commandFiltered.split(" ");
                    String[] args = new String[commandFiltered.split(" ").length - 1];
                    for (int i = 1; i < g.length; i++)
                        args[i - 1] = g[i];
                    long threadId = commandType.call(event.getTextChannel(), event.getMessage().getAuthor(), event.getGuild(), samaritan, args);
                    try {
                        event.getMessage().deleteMessage();
                    } catch (Exception exc) {
                        System.out.println("Couldn't delete command request message for thread: " + threadId);
                    }
                    continue commandLoop;
                }
            }
        }
    }
}
