package be.isach.samaritan.listener;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.listener
 * Created by: Sacha
 * Created on: 31th mai, 2016
 * at 17:52
 */
public class QuoteListener extends ListenerAdapter {

    private Map<TextChannel, List<Message>> messagesMap;
    private Samaritan samaritan;

    public QuoteListener(Samaritan samaritan) {
        this.samaritan = samaritan;
        this.messagesMap = new HashMap<>();
        for (Guild g : samaritan.getJda().getGuilds()) {
            for (TextChannel textChannel : g.getTextChannels()) {
                messagesMap.put(textChannel, new ArrayList<>());
            }
        }
    }

    public List<Message> getMessages(TextChannel textChannel) {
        return messagesMap.get(textChannel);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        messagesMap.get(event.getTextChannel()).add(event.getMessage());
    }
}
