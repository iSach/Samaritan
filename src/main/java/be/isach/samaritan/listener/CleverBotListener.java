package be.isach.samaritan.listener;

import be.isach.samaritan.chat.PrivateMessageChatThread;
import be.isach.samaritan.message.MessageScanningThread;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.EventListener;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.Map;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.listener
 * Created by: Sacha
 * Created on: 26th June, 2016
 * at 19:35
 */
public class CleverBotListener extends Thread implements EventListener {

    /**
     * Bot Session Factory.
     */
    private ChatterBotFactory botFactory;

    /**
     * Bot, created with botFactory.
     */
    private ChatterBot bot;

    /**
     * Bot Session, created with bot.
     */
    private ChatterBotSession botSession;

    private JDA jda;

    public CleverBotListener(JDA jda) {
        this.jda = jda;
        this.botFactory = new ChatterBotFactory();

        createBotSessions();

        jda.addEventListener(this);
    }

    /**
     * Creates Bot and the Bot Session.
     * Uses CleverBot.
     */
    private void createBotSessions() {
        try {
            bot = botFactory.create(ChatterBotType.CLEVERBOT);
            botSession = bot.createSession();
        } catch (Exception e) {
            interrupt();
        }
    }

    @Override
    public void onEvent(Event eevent) {
        if (eevent instanceof MessageReceivedEvent) {
            MessageReceivedEvent event = (MessageReceivedEvent) eevent;
            if (event.getMessage().getMentionedUsers().get(0).getId().equals(jda.getSelfInfo().getId())) {
                event.getChannel().sendTyping();
                try {
                    String s = event.getMessage().getContent().replace("@" + jda.getSelfInfo().getUsername() +" ", "");
                    event.getChannel().sendMessage(botSession.think(s));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}