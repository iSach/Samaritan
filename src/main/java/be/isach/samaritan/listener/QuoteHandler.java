package be.isach.samaritan.listener;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.EventListener;
import net.dv8tion.jda.hooks.ListenerAdapter;
import org.luaj.vm2.ast.Str;

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
    public synchronized void start() {
        super.start();
        loadData();
    }

    public void loadData() {
        System.out.println("[Quote Handler]: Loading channel histories...");
        for (TextChannel messageChannel : jda.getTextChannels()) {
            try {
                List<Message> messages = new ArrayList<>();
                MessageHistory messageHistory = new MessageHistory(messageChannel);
                messageHistory.retrieve(2000);
                messages.addAll(messageHistory.getRecent());
                messageChannelListMap.put(messageChannel, messages);
                System.out.println("[Quote Handler]: Loaded 1000 messages in channel: " + messageChannel.getName());
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



    @Override
    public void onEvent(Event eevent) {
        if (eevent instanceof MessageReceivedEvent) {
            MessageReceivedEvent event = (MessageReceivedEvent) eevent;
            if(messageChannelListMap.containsKey(event.getChannel())) {
                messageChannelListMap.get(event.getChannel()).add(event.getMessage());
            }
        }
    }
}
