package be.isach.samaritan.listener;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.command.CommandsRegistry;
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
        if (!event.getMessage().getContent().startsWith("£")) return;

        commandLoop:
        for (String s : event.getMessage().getContent().split(" && ")) {
            String commandFiltered = s.replaceFirst("£", "");
            String commandLabel = commandFiltered.split(" ")[0];
            for (CommandsRegistry command : CommandsRegistry.values()) {
                if (command.correspondsTo(commandLabel)) {
                    if(!command.isPublic() && !event.getAuthor().getUsername().equals(samaritan.admin)) return;
                    String[] g = commandFiltered.split(" ");
                    String[] args = new String[commandFiltered.split(" ").length - 1];
                    for (int i = 1; i < g.length; i++)
                        args[i - 1] = g[i];
                    command.call(event.getTextChannel(), event.getMessage().getAuthor(), event.getGuild(), samaritan, args);
                    event.getMessage().deleteMessage();
                    continue commandLoop;
                }
            }
        }
    }
}
