package be.isach.samaritan.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 15:32
 */
public class CommandUserInfo extends Command {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSS");

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandUserInfo(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }


    @Override
    void onExecute(String[] args) {
        User user = getExecutor();
        if (args != null && args.length > 0) {
            try {
                user = data.getMessage().getMentionedUsers().get(0);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        System.out.println(getGuild().getIconUrl());
        Member member = getGuild().getMember(user);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(member.getColor());
        embedBuilder.setThumbnail(user.getAvatarUrl());
        embedBuilder.addField("Discriminator", user.getDiscriminator(), true);
        embedBuilder.addField("ID", user.getId(), true);
        embedBuilder.addField("Game", (member.getGame() == null ? "None" : member.getGame().getName()), true);
        embedBuilder.addField("Status", member.getOnlineStatus().toString().toLowerCase(), true);
        embedBuilder.addField("Joined At", dateFormat.format(member.getJoinDate()), true);
        embedBuilder.addField("Access Level", getSamaritan().getAccessLevelManager().getAccessLevel(user) + "", true);
        embedBuilder.addField("Roles", formatRoleList(member.getRoles()), false);
        embedBuilder.setAuthor(member.getUser().getName(), null, member.getUser().getAvatarUrl());
        embedBuilder.setFooter("Informations requested by " + getExecutor().getName(), "http://discordapp.com");
        getMessageChannel().sendMessage(embedBuilder.build()).queue();
    }

    private String formatRoleList(List<Role> roles) {
        if (roles.isEmpty()) return "None.";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < roles.size(); i++)
            stringBuilder.append(roles.get(i).getName() + (i == roles.size() - 1 ? "" : ", "));
        return stringBuilder.toString();
    }
}
