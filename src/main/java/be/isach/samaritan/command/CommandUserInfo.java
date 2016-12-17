package be.isach.samaritan.command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

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
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSS");
        User user = getExecutor();
        if (args != null && args.length > 0) {
            try {
                user = data.getMessage().getMentionedUsers().get(0);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        Member member = getGuild().getMember(user);
        String stringBuilder = "```" + "\n" +
                "Requested User: " + user.getName() + "#" + user.getDiscriminator() + "\n" +
                "ID: " + user.getId() + "\n" +
                "Game: " + (member.getGame() == null ? "None" : member.getGame().getName()) + "\n" +
                "Status: " + member.getOnlineStatus().toString().toLowerCase() + "\n" +
                "Roles: " + formatRoleList(member.getRoles()) + "\n" +
                "Joined At: " + dateFormat.format(member.getJoinDate()) + "\n" +
                "Access Level: " + getSamaritan().getAccessLevelManager().getAccessLevel(user) + "\n" +
                "Avatar URL: " + user.getAvatarUrl() + "\n" +
                "```";
        getMessageChannel().sendMessage(stringBuilder).queue();
    }

    private String formatRoleList(List<Role> roles) {
        if (roles.isEmpty()) return "None.";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < roles.size(); i++)
            stringBuilder.append(roles.get(i).getName() + (i == roles.size() - 1 ? "" : ", "));
        return stringBuilder.toString();
    }
}
