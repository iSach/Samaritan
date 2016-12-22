package be.isach.samaritan.command;

import be.isach.samaritan.listener.QuoteHandler;
import be.isach.samaritan.util.ColorUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(message.getAuthor().getName() + " (" + message.getCreationTime().toZonedDateTime().withZoneSameInstant(ZoneId.of("Europe/Paris")).format(DATE_FORMAT) + ")", "http://discordapp.com", message.getAuthor().getAvatarUrl());
        embedBuilder.setDescription(message.getRawContent());
        embedBuilder.setColor(getGuild().getMember(message.getAuthor()).getColor());
        embedBuilder.setFooter("Quote requested by " + getExecutor().getName(), null);
        MessageEmbed messageEmbed = embedBuilder.build();
        getMessageChannel().sendMessage(messageEmbed).queue();
    }
}
