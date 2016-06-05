package be.isach.samaritan.command;

import be.isach.samaritan.util.SamaritanConstants;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 06th juin, 2016
 * at 00:00
 */
public class CommandSetLevel extends Command {
    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandSetLevel(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        if (args.length < 2) {
            getMessageChannel().sendMessage("Correct Usage: `" + SamaritanConstants.PREFIX + "setlevel <username> <newLevel>`");
            return;
        }
        try {
            User user = getJda().getUsersByName(args[0]).get(0);
            if (user.getId().equals(getSamaritan().getOwnerId())) {
                getMessageChannel().sendMessage("You can't change the Owner's Access Level.");
                return;
            }
            int level = Math.max(0, Math.min(4, Integer.parseInt(args[1])));
            try {
                getSamaritan().getAccessLevelManager().setLevel(user, level);
                getMessageChannel().sendMessage("Successfully set the level of " + user.getUsername() + " to " + level);
            } catch (IOException i) {
                getMessageChannel().sendMessage("Something went wrong... Check Username, etc.");
            }
        } catch (Exception exc) {
            getMessageChannel().sendMessage("Something went wrong... Check Username, etc.");
        }
    }
}
