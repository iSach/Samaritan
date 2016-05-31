package be.isach.samaritan.command;

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
class CommandQuote extends Command {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        for (Message message : getSamaritan().getQuoteListener().getMessages((TextChannel) getMessageChannel())) {
            if (message.getContent().contains(s)) {
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
                return;
            }
        }
        getMessageChannel().sendMessage("No message found.");
    }
}
