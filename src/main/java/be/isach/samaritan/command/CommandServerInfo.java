package be.isach.samaritan.command;

import be.isach.samaritan.util.ColorThief;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 15:32
 */
public class CommandServerInfo extends Command {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSS");

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandServerInfo(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }


    @Override
    void onExecute(String[] args) {
        Guild guild = getGuild();
        if (args != null && args.length > 0) {
            try {
                guild = getJda().getGuildById(args[0]);
            } catch (Exception exc) {
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        try {
            final URL url = new URL(guild.getIconUrl());
            final HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31" +
                            " (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
            final BufferedImage image = ImageIO.read(connection.getInputStream());
            int[] rgb = ColorThief.getColor(image);
            embedBuilder.setColor(new Color(rgb[0], rgb[1], rgb[2]));
        } catch (IOException e) {
            embedBuilder.setColor(Color.BLACK);
            e.printStackTrace();
        }
        embedBuilder.setThumbnail(guild.getIconUrl());
        embedBuilder.addField("ID", guild.getId(), true);
        embedBuilder.addField("Members", guild.getMembers().size() + "", true);
        embedBuilder.addField("Text Channels", guild.getTextChannels().size() + "", true);
        embedBuilder.addField("Voice Channels", guild.getVoiceChannels().size() + "", true);
        embedBuilder.addField("Created on", dateFormat.format(guild.getCreationTime()), true);
        embedBuilder.addField("Owner", guild.getOwner().getUser().getName(), true);
        embedBuilder.addField("Region", guild.getRegion().getName(), true);
        StringBuilder emotes = new StringBuilder();
        for (Emote emote : guild.getEmotes()) {
            emotes.append(emote.getAsMention() + " ");
        }
        embedBuilder.addField("Emotes", emotes.toString().isEmpty() ? "None" : emotes.toString(), true);
        embedBuilder.addField("Roles", formatRoleList(guild.getRoles()), false);
        embedBuilder.setAuthor(guild.getName(), null, guild.getIconUrl());
        embedBuilder.setFooter("Informations requested by " + getExecutor().getName(), "http://discordapp.com");
        getMessageChannel().sendMessage(embedBuilder.build()).queue();
    }

    private String space(int amount) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private String formatRoleList(List<Role> roles) {
        if (roles.isEmpty()) return "None.";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < roles.size(); i++)
            stringBuilder.append(roles.get(i).getName() + (i == roles.size() - 1 ? "" : ", "));
        return stringBuilder.toString();
    }
}
