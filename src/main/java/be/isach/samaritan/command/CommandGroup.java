package be.isach.samaritan.command;

import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Package: be.isach.samaritan.command
 * Created by: sachalewin
 * Date: 17/08/16
 * Project: samaritan
 */
public class CommandGroup extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandGroup(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        if(!getGuild().getId().equals("186941943941562369")) {
            getMessageChannel().sendMessage("This command isn't available here.");
            return;
        }

        if(args == null || args.length == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```");
            stringBuilder.append("Sub-Commands:").append("\n");;
            stringBuilder.append("    -group list").append("\n");
            stringBuilder.append("    -group add [group]").append("\n");;
            stringBuilder.append("    -group remove [group]");
            stringBuilder.append("```");
            getMessageChannel().sendMessage(stringBuilder.toString());
            printJoinableGroups();
            return;
        }
        String arg = args[0];
        switch (arg) {
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("```");
                stringBuilder.append("Sub-Commands:").append("\n");;
                stringBuilder.append("    -group list").append("\n");
                stringBuilder.append("    -group add [group]").append("\n");;
                stringBuilder.append("    -group remove [group]");
                stringBuilder.append("```");
                getMessageChannel().sendMessage(stringBuilder.toString());
                printJoinableGroups();
                break;
            case "list":
                list();
                break;
            case "add":
                add(args);
                break;
            case "remove":
                remove(args);
                break;
        }
    }

    private void list() {
        Guild guild = getGuild();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```");
        String s = "%-25s %-8s %-8s %s";
        stringBuilder.append(String.format(s, "Group Name", "Online", "Idle", "Total")).append("\n\n");
        List<Role> roles = guild.getRoles().stream()
                .filter(role -> !role.getName().contains("everyone"))
                .sorted((r1, r2) -> compare(guild, r1, r2))
                .collect(Collectors.toList());
        for (Role role : roles) {
            int online = 0, idle = 0, max = 0;

            for (User user : guild.getUsersWithRole(role)) {
                if (user.getOnlineStatus() == OnlineStatus.ONLINE) online++;
                else if (user.getOnlineStatus() == OnlineStatus.AWAY) idle++;
                max++;
            }

            stringBuilder.append(String.format(s, "@" + role.getName(), online, idle, max)).append("\n");
        }
        stringBuilder.append("```");
        getMessageChannel().sendMessage(stringBuilder.toString());
    }

    private void add(String... args) {
        String roleAsString;
        if(args.length < 2) {
            getMessageChannel().sendMessage("What group do you wanna join?");
            printJoinableGroups();
            roleAsString = nextMessage().getContent().split(" ")[0].toLowerCase();
        } else {
            roleAsString = args[1].toLowerCase();
        }
        Role role = null;
        for(Role r : getGuild().getRoles()) {
            if(r.getName().equalsIgnoreCase(roleAsString)) {
                role = r;
            }
        }
        if(role == null) {
            getMessageChannel().sendMessage("That group doesn't exist!");
            return;
        }
        if(getGuild().getUsersWithRole(role).contains(getExecutor())) {
            getMessageChannel().sendMessage("You're already in that group!");
            return;
        }
        if(role.getPosition() > (getGuild().getRoles().size() - 7)
                || role.getName().contains("everyone")) {
            getMessageChannel().sendMessage("This isn't a language group.");
            printJoinableGroups();
            return;
        }
        getGuild().getManager().addRoleToUser(getExecutor(), role);
        getGuild().getManager().update();
        getMessageChannel().sendMessage("You've been successfully added to the Language Group: " + role.getName());
    }

    private void remove(String... args) {
        String roleAsString;
        if(args.length < 2) {
            getMessageChannel().sendMessage("What group do you wanna be removed from?");
            printJoinableGroups();
            roleAsString = nextMessage().getContent().split(" ")[0].toLowerCase();
        } else {
            roleAsString = args[1].toLowerCase();
        }
        Role role = null;
        for(Role r : getGuild().getRoles()) {
            if(r.getName().equalsIgnoreCase(roleAsString)) {
                role = r;
            }
        }
        if(role == null) {
            getMessageChannel().sendMessage("That group doesn't exist!");
            return;
        }
        if(!getGuild().getUsersWithRole(role).contains(getExecutor())) {
            getMessageChannel().sendMessage("You are not in that group!");
            return;
        }
        if(role.getPosition() > (getGuild().getRoles().size() - 7)
                || role.getName().contains("everyone")) {
            getMessageChannel().sendMessage("This isn't a language group.");
            printJoinableGroups();
            return;
        }
        getGuild().getManager().removeRoleFromUser(getExecutor(), role);
        getGuild().getManager().update();
        getMessageChannel().sendMessage("You've been successfully removed from the Language Group: " + role.getName());
    }

    private void printJoinableGroups() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```");
        stringBuilder.append("Joinable groups:").append("\n");
        for(Role role : getGuild().getRoles()) {
            /*if(role.getPosition() > (getGuild().getRoles().size() - 7)
                    || role.getName().contains("everyone")) {
                continue;
            }*/

            stringBuilder.append("  ").append(role.getName().toLowerCase()).append("   ").append(role.getPosition()).append("\n");
        }
        stringBuilder.append("```");
        getMessageChannel().sendMessage(stringBuilder.toString());
    }

    private static int compare(Guild guild, Role role, Role other) {
        int a = guild.getUsersWithRole(role).size(), b = guild.getUsersWithRole(other).size();
        return b > a ? 1 : b < a ? -1 : 0;
    }
}