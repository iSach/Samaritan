package be.isach.samaritan.command;

import be.isach.samaritan.listener.QuoteHandler;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 31th mai, 2016
 * at 17:50
 * <p>
 * Quotes a message.
 */
public class CommandQuote extends Command {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandQuote(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        String s = buildStringFromArgs();
        getMessageChannel().sendTyping();
        System.out.println("quote command called.");
        QuoteHandler quoteHandler = getSamaritan().getQuoteHandler();

        if(!quoteHandler.getMessageChannelListMap().containsKey(getMessageChannel())) {
            getMessageChannel().sendMessage("Channel History hasn't be fully loaded yet. Please wait a few moments.");
            return;
        }

        Message message = quoteHandler.searchForQuote(s, getMessageChannel());

        if (message == null) {
            getMessageChannel().sendMessage("No message found.");
            return;
        }

        String messageToSend = "```\n" +
                "(" +
                message.getTime().toZonedDateTime().withZoneSameInstant(ZoneId.of("Europe/Paris")).format(DATE_FORMAT) +
                " " +
                message.getAuthor().getUsername() +
                "): " +
                message.getContent() +
                "\n" +
                "```";
        getMessageChannel().sendMessage(messageToSend);
    }
}
