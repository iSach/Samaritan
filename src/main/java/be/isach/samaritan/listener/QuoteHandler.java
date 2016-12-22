package be.isach.samaritan.listener;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.util.SamaritanConstants;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.util.*;
import java.util.logging.Level;

public class QuoteHandler extends Thread implements EventListener {

    private Map<MessageChannel, List<Message>> messageChannelListMap;
    private JDA jda;
    private Samaritan samaritan;

    public QuoteHandler(JDA jda, Samaritan samaritan) {
        this.jda = jda;
        this.samaritan = samaritan;
        this.messageChannelListMap = new HashMap<>();

        this.jda.addEventListener(this);
    }

    @Override
    public void run() {
        loadData();
    }

    public void loadData() {
        samaritan.getLogger().writeFrom("Quote Handler", "Loading channel histories...");
        for (TextChannel messageChannel : jda.getTextChannels()) {
            try {
                List<Message> messages = new ArrayList<>();
                MessageHistory messageHistory = new MessageHistory(messageChannel);
                RestAction.LOG.setLevel(SimpleLog.LEVEL.OFF);
                messageHistory.retrievePast(100).queue((messageList) -> {
                    messages.addAll(messageHistory.getCachedHistory());
                    messageChannelListMap.put(messageChannel, messages);
                });

            } catch (Exception exc) {
            }
        }
    }

    public Message searchForQuote(String str, MessageChannel messageChannel) {
        if (!messageChannelListMap.containsKey(messageChannel) || messageChannelListMap.get(messageChannel) == null)
            return null;

        List<Message> searchableList = new ArrayList<>();
        searchableList.addAll(messageChannelListMap.get(messageChannel));
//        Collections.reverse(searchableList);

        for (Message message : searchableList) {
            if (message.getContent().toLowerCase().contains(str.toLowerCase())
                    && !message.getContent().startsWith(SamaritanConstants.PREFIX + "")
                    && !message.getAuthor().isBot()) {
                return message;
            }
        }
        return null;
    }

    public Map<MessageChannel, List<Message>> getMessageChannelListMap() {
        return messageChannelListMap;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent msgEvent = (MessageReceivedEvent) event;
            if (messageChannelListMap.containsKey(msgEvent.getChannel())) {
                messageChannelListMap.get(msgEvent.getChannel()).add(msgEvent.getMessage());
            }
        }
    }
}
