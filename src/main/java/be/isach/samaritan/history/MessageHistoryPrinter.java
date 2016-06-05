package be.isach.samaritan.history;

import be.isach.samaritan.util.TextUtil;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.history
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 15:48
 */
public class MessageHistoryPrinter {

    private static final int MAX_HISTORY_SIZE = 1000;

    public void printHistory(int size, MessageChannel channel) {

        int realDumpSize = Math.min(size, MAX_HISTORY_SIZE);
        channel.sendTyping();

        try {

            MessageHistory mh = new MessageHistory(channel);
            int availableMessages = mh.getRecent().size();

            while (availableMessages < realDumpSize) {
                int nextMessages = Math.min(100, realDumpSize - availableMessages);
                availableMessages = nextMessages + availableMessages;
                mh.retrieve(nextMessages);
            }

            String dump = "**------ BEGIN OF HISTORY PRINTING ------**\n";
            List<Message> messages = new ArrayList<>(mh.getRecent());
            Collections.reverse(messages);
            messages = messages.subList(0, Math.min(realDumpSize, messages.size()));
            dump = dump + "Size = " + messages.size() + "\nTimes are in UTC!\n\n";

            int i = 1;
            for (Message msg : messages) {
                String author = "[UNKNOWN USER]";
                String time = "[UNKNOWN TIME]";
                String content = "[COULD NOT DISPLAY CONTENT!]";

                try {
                    author = msg.getAuthor().getUsername() + "#" + msg.getAuthor().getDiscriminator();
                } catch (NullPointerException ex) {
                }

                try {
                    time = formatTimestamp(msg.getTime());
                } catch (NullPointerException ex) {
                }

                try {
                    content = msg.getContent();
                } catch (NullPointerException ex) {
                }

                dump = dump + "--Msg #" + i + " by " + author
                        + " at " + time + "--\n" + content + "\n";
                if (msg.getAttachments().size() > 0) {
                    dump = dump + "Attachments:\n";
                    int j = 1;
                    for (Message.Attachment attach : msg.getAttachments()) {
                        dump = dump + "[" + j + "] " + attach.getUrl();
                    }
                }
                dump = dump + "\n\n";
                i++;
            }
            dump = dump + "**------ END OF HISTORY PRINTING ------**\n";

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.appendString("Successfully found and printed history of `" + messages.size() + "` messages.\n");
            messageBuilder.appendString(TextUtil.postToHastebin(dump, true) + ".txt\n");
            channel.sendMessage(messageBuilder.build());
        } catch (UnirestException ex) {
            channel.sendMessage("Failed to connect to Hastebin: " + ex.getMessage());
        }
    }

    private String formatTimestamp(OffsetDateTime t) {
        String str;
        if (LocalDateTime.now(Clock.systemUTC()).getDayOfYear() != t.getDayOfYear()) {
            str = "[" + t.getMonth().name().substring(0, 3).toLowerCase() + " " + t.getDayOfMonth() + " " + forceTwoDigits(t.getHour()) + ":" + forceTwoDigits(t.getMinute()) + "]";
        } else {
            str = "[" + forceTwoDigits(t.getHour()) + ":" + forceTwoDigits(t.getMinute()) + "]";
        }
        return str;
    }

    private String forceTwoDigits(int i) {
        String str = String.valueOf(i);

        if (str.length() == 1) {
            str = "0" + str;
        }

        return str;
    }

}
