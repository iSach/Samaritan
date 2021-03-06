package be.isach.samaritan.command;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 29th mai, 2016
 * at 02:50
 * <p>
 * Represents Command Data.
 */
public class CommandData {

    /**
     * Samaritan Instance.
     */
    private Samaritan samaritan;

    /**
     * User that executed the command.
     */
    private User user;

    /**
     * Guild where the user executed the command.
     */
    private Guild guild;

    private Message message;

    public CommandData(Samaritan samaritan, User user, Guild guild, Message message) {
        this.samaritan = samaritan;
        this.user = user;
        this.guild = guild;
        this.message = message;
    }

    /**
     * @return The Guild where the user executed the command.
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * @return The Samaritan Instance.
     */
    public Samaritan getSamaritan() {
        return samaritan;
    }

    /**
     * @return The Command Executor.
     */
    public User getExecutor() {
        return user;
    }

    public Message getMessage() {
        return message;
    }
}
