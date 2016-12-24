package be.isach.samaritan.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
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
        if (!getGuild().getId().equals("186941943941562369")) {
            getMessageChannel().sendMessage("This command isn't available here.").queue();
            return;
        }

        if (args == null || args.length == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("-group list ").append("\n");
            stringBuilder.append("-group join [group]").append("\n");
            stringBuilder.append("-group leave [group]");

            StringBuilder descriptionBuilder = new StringBuilder();
            descriptionBuilder.append("Lists joinable groups.").append("\n");
            descriptionBuilder.append("Join a group.").append("\n");
            descriptionBuilder.append("Quit a group.");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.WHITE);
            embedBuilder.setTitle("Group Command Help");
            embedBuilder.addField("Sub-Command", stringBuilder.toString(), true);
            embedBuilder.addField("Description", descriptionBuilder.toString(), true);
            embedBuilder.setFooter("Informations requested by " + getExecutor().getName(), null);
            getMessageChannel().sendMessage(embedBuilder.build()).queue();
            printJoinableGroups();
            return;
        }
        String arg = args[0];
        switch (arg) {
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("-group list ").append("\n");
                stringBuilder.append("-group join [group]").append("\n");
                stringBuilder.append("-group leave [group]");

                StringBuilder descriptionBuilder = new StringBuilder();
                descriptionBuilder.append("Lists joinable groups.").append("\n");
                descriptionBuilder.append("Join a group.").append("\n");
                descriptionBuilder.append("Quit a group.");

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.WHITE);
                embedBuilder.setTitle("Group Command Help");
                embedBuilder.addField("Sub-Command", stringBuilder.toString(), true);
                embedBuilder.addField("Description", descriptionBuilder.toString(), true);
                embedBuilder.setFooter("Informations requested by " + getExecutor().getName(), null);
                getMessageChannel().sendMessage(embedBuilder.build()).queue();
                printJoinableGroups();
                break;
            case "list":
                list();
                break;
            case "join":
                join(args);
                break;
            case "leave":
                leave(args);
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

            for (Member member : guild.getMembersWithRoles(role)) {
                if (member.getOnlineStatus() == OnlineStatus.ONLINE) online++;
                else if (member.getOnlineStatus() == OnlineStatus.IDLE) idle++;
                max++;
            }

            stringBuilder.append(String.format(s, "@" + role.getName(), online, idle, max)).append("\n");
        }
        stringBuilder.append("```");
        getMessageChannel().sendMessage(stringBuilder.toString());
    }

    private void join(String... args) {
        String roleAsString;
        if (args.length < 2) {
            getMessageChannel().sendMessage("What group do you want to join?").queue();
            printJoinableGroups();
            roleAsString = nextMessage().getContent().split(" ")[0].toLowerCase();
        } else {
            roleAsString = args[1].toLowerCase();
        }
        Role role = null;
        for (Role r : getGuild().getRoles()) {
            if (r.getName().equalsIgnoreCase(roleAsString)) {
                role = r;
            }
        }
        if (role == null) {
            getMessageChannel().sendMessage("That group doesn't exist!").queue();
            return;
        }
        if (getGuild().getMembersWithRoles(role).contains(getMember())) {
            getMessageChannel().sendMessage("You're already in that group!").queue();
            return;
        }
        if (role.getPosition() > (getGuild().getRoles().size() - 7)
                || role.getName().contains("everyone")) {
            getMessageChannel().sendMessage("This isn't a language group.").queue();
            printJoinableGroups();
            return;
        }
        Role finalRole = role;
        getGuild().getController().addRolesToMember(getGuild().getMember(getExecutor()), role).queue(aVoid -> {
            getMessageChannel().sendMessage("You've been successfully added to the Language Group: " + finalRole.getName()).queue();
        });
    }

    private void leave(String... args) {
        String roleAsString;
        if (args.length < 2) {
            getMessageChannel().sendMessage("What group do you want to leave?");
            printJoinableGroups();
            roleAsString = nextMessage().getContent().split(" ")[0].toLowerCase();
        } else {
            roleAsString = args[1].toLowerCase();
        }
        Role role = null;
        for (Role r : getGuild().getRoles()) {
            if (r.getName().equalsIgnoreCase(roleAsString)) {
                role = r;
            }
        }
        if (role == null) {
            getMessageChannel().sendMessage("That group doesn't exist!").queue();
            return;
        }
        if (!getGuild().getMembersWithRoles(role).contains(getMember())) {
            getMessageChannel().sendMessage("You are not in that group!").queue();
            return;
        }
        if (role.getPosition() > (getGuild().getRoles().size() - 7)
                || role.getName().contains("everyone")) {
            getMessageChannel().sendMessage("This isn't a language group.").queue();
            printJoinableGroups();
            return;
        }
        Role finalRole = role;
        getGuild().getController().removeRolesFromMember(getGuild().getMember(getExecutor()), role).queue((aVoid) -> {
            getMessageChannel().sendMessage("You've been successfully removed from the Language Group: " + finalRole.getName()).queue();
        });
    }

    private void printJoinableGroups() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("").append("\n");
        int langsPos = 0;
        for (Role role : getGuild().getRoles()) {
            if (role.getName().equalsIgnoreCase("langs")) {
                langsPos = role.getPosition();
                break;
            }
        }
        for (Role role : getGuild().getRoles()) {
            if (role.getPosition() < langsPos && role.getPosition() >= 0) {
                stringBuilder.append(role.getName().toLowerCase()).append("\n");
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.WHITE);
        embedBuilder.addField("Language Groups", stringBuilder.toString(), false);
        embedBuilder.setFooter("Informations requested by " + getExecutor().getName(), null);
        getMessageChannel().sendMessage(embedBuilder.build()).queue();
    }

    private static int compare(Guild guild, Role role, Role other) {
        int a = guild.getMembersWithRoles(role).size(), b = guild.getMembersWithRoles(other).size();
        return b > a ? 1 : b < a ? -1 : 0;
    }
}