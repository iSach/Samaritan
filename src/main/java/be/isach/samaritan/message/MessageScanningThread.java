package be.isach.samaritan.message;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.EventListener;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.chat
 * Created by: Sacha
 * Created on: 29th mai, 2016
 * at 01:31
 * <p>
 * This class implements nextMessage.
 * To be used like Scanner.nextLine(), it waits for an input in MessageChannel.
 * Fetches message with EventListener.
 */
public abstract class MessageScanningThread extends Thread implements EventListener {

    /**
     * Message to give in nextMessage()
     */
    private Message scannedMessaged;

    /**
     * TextChannel.
     */
    private MessageChannel messageChannel;

    /**
     * JDA of the TextChannel.
     */
    private JDA jda;

    /**
     * Command Executor.
     */
    private User executor;

    public MessageScanningThread(MessageChannel messageChannel, JDA jda, User executor) {

        if (messageChannel == null)
            throw new IllegalArgumentException("MessageScanningThread: text_channel can't be null!");

        this.messageChannel = messageChannel;
        this.jda = jda;
        this.executor = executor;
        jda.addEventListener(this);
    }

    /**
     * Sets the Scanned Message, forces Next Message.
     *
     * @param scannedMessaged the Scanned Message
     */
    public void setScannedMessaged(Message scannedMessaged) {
        this.scannedMessaged = scannedMessaged;
    }

    /**
     * Called for nextMessage management.
     *
     * @param e The event triggered.
     */
    @Override
    public void onEvent(Event e) {
        if (e instanceof MessageReceivedEvent) {
            MessageReceivedEvent event = (MessageReceivedEvent) e;
            if (scannedMessaged != null) return;
            if (event.getTextChannel() != messageChannel) return;
            if (event.getAuthor().isBot()) return;
            if (!event.getAuthor().equals(executor)) return;
            event.getChannel().sendTyping();
            setScannedMessaged(event.getMessage());
        }
    }

    /**
     * Asks for input in the channel.
     *
     * @return The input retrieved in the Channel. As JDA Message.
     */
    protected final Message nextMessage() {
        while (scannedMessaged == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Message message = scannedMessaged;
        scannedMessaged = null;
        return message;
    }

    /**
     * @return JDA.
     */
    public JDA getJda() {
        return jda;
    }

    /**
     * @return the current working Message Channel.
     */
    public MessageChannel getMessageChannel() {
        return messageChannel;
    }

    /**
     * Called when Thread is started.
     */
    @Override
    public void run() {
        onRun();
        jda.removeEventListener(this);
        interrupt();
    }

    protected abstract void onRun();

    public Message getScannedMessaged() {
        return scannedMessaged;
    }
}
