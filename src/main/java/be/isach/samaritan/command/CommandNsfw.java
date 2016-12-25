package be.isach.samaritan.command;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Package: be.isach.samaritan.command
 * Created by: sachalewin
 * Date: 17/08/16
 * Project: samaritan
 */
public class CommandNsfw extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandNsfw(MessageChannel messageChannel, CommandData commandData, String[] args) {
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

        Role role = null;

        for(Role r : getGuild().getRoles()) {
            if(r.getName().equals("Catholique")) {
                role = r;
                break;
            }
        }

        if(role == null) {
            return;
        }

        if(!getGuild().getMembersWithRoles(role).contains(getMember())) {
            getGuild().getController().addRolesToMember(getGuild().getMember(getExecutor()), role).queue(aVoid -> {
                getMessageChannel().sendMessage(getMember().getAsMention() + " You've been successfully added to the nsfw Group").queue();
            });
        } else {
            getGuild().getController().removeRolesFromMember(getGuild().getMember(getExecutor()), role).queue(aVoid -> {
                getMessageChannel().sendMessage(getMember().getAsMention() + " You've been successfully removed from the Nsfw Group").queue();
            });
        }
    }



    private static int compare(Guild guild, Role role, Role other) {
        int a = guild.getMembersWithRoles(role).size(), b = guild.getMembersWithRoles(other).size();
        return b > a ? 1 : b < a ? -1 : 0;
    }
}