package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 13:58
 */
public class CommandHaste extends Command {
    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandHaste(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        try {
            String url;
            if (args != null && args.length > 0) {
                url = args[0];
            } else {
                getMessageChannel().sendMessage("Please give me an URL.");
                url = nextMessage().getContent();
            }
            String[] idAndLanguage = format(url);
            String rawTextUrl = "http://hastebin.com/raw/" + idAndLanguage[0];
            getMessageChannel().sendMessage("```" + idAndLanguage[1] + "\n" +
                    getContentFromHtmlPage(rawTextUrl) + "\n```");
        } catch (Exception exc) {
            getMessageChannel().sendMessage("Mmh, something went wrong. Invalid URL?");
        }
    }

    private String[] format(String url) {
        url = url.replace("http://hastebin.com/", "");
        int indexDot = url.indexOf('.');
        return new String[]{
                url.substring(0, indexDot), url.substring(indexDot + 1, url.length())
        };
    }

    private String getContentFromHtmlPage(String page) {
        StringBuilder sb = new StringBuilder();

        try {
            URLConnection connection = new URL(page).openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
        } catch (IOException e) {
            // handle exception
        }

        return sb.toString();
    }
}
