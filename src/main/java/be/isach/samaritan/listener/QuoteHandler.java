package be.isach.samaritan.listener;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuoteHandler extends Thread implements EventListener {

    private Map<MessageChannel, List<Message>> messageChannelListMap;
    private JDA jda;

    public QuoteHandler(JDA jda) {
        this.jda = jda;
        this.messageChannelListMap = new HashMap<>();

        this.jda.addEventListener(this);
    }

    @Override
    public void run() {
        loadData();
    }

    public void loadData() {
        System.out.println("[Quote Handler]: Loading channel histories...");
        for (TextChannel messageChannel : jda.getTextChannels()) {
            try {
                List<Message> messages = new ArrayList<>();
                MessageHistory messageHistory = new MessageHistory(messageChannel);
                messageHistory.retrievePast(2000).queue((messageList) -> {
                    messages.addAll(messageHistory.getCachedHistory());
                    messageChannelListMap.put(messageChannel, messages);
                    System.out.println("[Quote Handler]: Loaded 1000 messages in channel: " + messageChannel.getName());
                });
            } catch (Exception exc) {
                continue;
            }
        }
    }

    public Message searchForQuote(String str, MessageChannel messageChannel) {
        if (!messageChannelListMap.containsKey(messageChannel) || messageChannelListMap.get(messageChannel) == null)
            return null;

        for (Message message : messageChannelListMap.get(messageChannel)) {
            if (message.getContent().toLowerCase().contains(str.toLowerCase())
                    && !message.getContent().startsWith("-")
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
            if(messageChannelListMap.containsKey(msgEvent.getChannel())) {
                messageChannelListMap.get(msgEvent.getChannel()).add(msgEvent.getMessage());
            }
        }
    }
}
