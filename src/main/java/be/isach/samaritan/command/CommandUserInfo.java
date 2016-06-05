package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

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
        User user = getExecutor();
        if (args != null && args.length > 0) {
            try {
                user = getJda().getUsersByName(args[0]).get(0);
            } catch (Exception exc) {
                getMessageChannel().sendMessage("Invalid user.");
                return;
            }
        }
        String stringBuilder = "```" + "\n" +
                "Requested User: " + user.getUsername() + "#" + user.getDiscriminator() + "\n" +
                "ID: " + user.getId() + "\n" +
                "Game: " + (user.getCurrentGame() == null ? "None" : user.getCurrentGame().getName()) + "\n" +
                "Status: " + user.getOnlineStatus().toString().toLowerCase() + "\n" +
                "Roles: " + formatRoleList(getGuild().getRolesForUser(user)) + "\n" +
                "Access Level: " + getSamaritan().getAccessLevelManager().getAccessLevel(user) + "\n" +
                "Avatar URL: " + user.getAvatarUrl() + "\n" +
                "```";
        getMessageChannel().sendMessage(stringBuilder);
    }

    private String formatRoleList(List<Role> roles) {
        if (roles.isEmpty()) return "None.";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < roles.size(); i++)
            stringBuilder.append(roles.get(i).getName() + (i == roles.size() - 1 ? "" : ", "));
        return stringBuilder.toString();
    }
}
