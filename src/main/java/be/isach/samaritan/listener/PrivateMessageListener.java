package be.isach.samaritan.listener;

import be.isach.samaritan.chat.PrivateMessageChatThread;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.listener
 * Created by: Sacha
 * Created on: 28th mai, 2016
 * at 12:01
 */
public class PrivateMessageListener extends ListenerAdapter {

    /**
     * Chat Thread map.
     * Key: The User talking with Samaritan.
     * Value: The PrivateMessageChatThread for the PM.
     */
    private Map<User, PrivateMessageChatThread> chatThreads = new HashMap<>();

    /**
     * Called when a PM is received.
     * Checks for PrivateMessageChatThread if exists, or creates it.
     *
     * @param event The MessageReceivedEvent, containg the data of the PM.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getAuthor().isBot()) return;
        if (!(event.getChannel() instanceof PrivateChannel)) return;

        if (!exists(event.getPrivateChannel())) {
            PrivateMessageChatThread chatThread = new PrivateMessageChatThread(event.getPrivateChannel(),
                    event.getJDA(), event.getAuthor(), event.getMessage(), chatThreads);
            chatThread.start();
        } else {
            PrivateMessageChatThread chatThread = get(event.getPrivateChannel());
            if (chatThread != null) {
                chatThread.setScannedMessaged(event.getMessage());
            } else {
                System.out.println("Unexpected exception. PrivateMessageChatThread couldn't be found for:" +
                        " " + ((PrivateChannel) event.getChannel()).getUser().getUsername());
            }
        }
    }

    /**
     * @param channel The MessageChannel the PrivateMessageChatThread corresponds to.
     * @return The PrivateMessageChatThread corresponding to the given Message Channel.
     */
    private PrivateMessageChatThread get(MessageChannel channel) {
        for (PrivateMessageChatThread chatThread : chatThreads.values())
            if (chatThread.getMessageChannel() == channel) return chatThread;
        return null;
    }

    /**
     * Checks if a PrivateMessageChatThread exists for a given MessageChannel.
     *
     * @param channel The MessageChannel to check.
     * @return {@code true} if a PrivateMessageChatThread exists for the given MessageChannel, otherwise {@code false}.
     */
    private boolean exists(MessageChannel channel) {
        return get(channel) != null;
    }

    /**
     * @return (User, PrivateMessageChatThread) Map.
     */
    public Map<User, PrivateMessageChatThread> getChatThreads() {
        return chatThreads;
    }
}
