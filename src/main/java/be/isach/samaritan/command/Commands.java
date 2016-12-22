package be.isach.samaritan.command;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.core.entities.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 30th mai, 2016
 * at 01:51 am
 * <p>
 * Commands registry, all commands are registered here.
 */
public enum Commands {

    /**
     * REMINDER:
     * Access Levels.
     * 4: Owner or Admin. Owner Access can't be changed.
     * Then Levels go from 3 to 0.
     */

    BRAINFUCK(CommandBrainFuck.class, 0, "Runs BrainFuck code", "brainfuck", "bf"),
    BEAM(CommandBeam.class, 2, "Beam Commands", "beam"),
    CLEAN(CommandClean.class, 3, "Cleans fail lol", "clean"),
    EVAL(CommandEval.class, 3, "Runs JS code.", "eval"),
    FIND_THE_NUMBER(CommandFindTheNumber.class, 0, "Find the Number game.", "findthenumber", "ftn"),
    GIF(CommandGif.class, 0, "Sends a gif", "gif"),
    GITHUB(CommandGithub.class, 0, "Shows Github link", "git", "github"),
    GROUP(CommandGroup.class, 0, "Group Related Things", "group", "g"),
    HASTE(CommandHaste.class, 0, "Prints Hastebin Code.", "haste"),
    HELP(CommandHelp.class, 0, "Prints this", "help"),
    JOIN_ME(CommandJoinMe.class, 3, "Joins Admin.", "joinme"),
    LEET(CommandLeet.class, 0, "Translates text to Leet.", "leet"),
    QUOTE(CommandQuote.class, 0, "Quotes a message", "quote", "q"),
    SAY(CommandSay.class, 3, "Says a message", "say", "print"),
    SEND(CommandSend.class, 2, "Sends a WebSocket", "send", "socket"),
    SERVERS(CommandServers.class, 0, "Show Servers list", "servers"),
    SERVERINFO(CommandServerInfo.class, 0, "Show Server Info", "sinfo", "serverinfo", "guildinfo"),
    SET_LEVEL(CommandSetLevel.class, 4, "Sets a user Level.", "setlevel"),
    SHUTDOWN(CommandShutdown.class, 4, "Stops Samaritan [!!]", "stop", "shutdown"),
    TEST(CommandTest.class, 4, "Test Command", "test"),
    TIC_TAC_TOE(CommandTicTacToe.class, 0, "Starts a Tic Tac Toe Game", "tictactoe", "ttt"),
    TWEET(CommandTweet.class, 3, "Tweets", "tweet"),
    TWITCH(CommandTwitch.class, 2, "Twitch Related", "twitch"),
    UNLEET(CommandUnleet.class, 0, "Unleets a message", "unleet"),
    UPTIME(CommandUptime.class, 0, "Shows uptime", "uptime"),
    USERINFO(CommandUserInfo.class, 0, "Show infos about a User.", "userinfo", "user-info"),
    WORD_REACT(CommandWordReaction.class, 0, "Reacts as a word.", "wordreact", "wreact", "wr");

    /**
     * Command class.
     */
    private Class<? extends Command> clazz;

    /**
     * Command aliases
     */
    private List<String> aliases;

    /**
     * Command description
     */
    private String description;

    /**
     * Is command public?
     */
    private int requiredAccessLevel;

    Commands(Class<? extends Command> clazz, int requiredAccessLevel, String description, String... aliases) {
        this.clazz = clazz;
        this.aliases = Arrays.asList(aliases);
        this.description = description;
        this.requiredAccessLevel = requiredAccessLevel;
    }

    /**
     * @param alias The command alias.
     * @return The command corresponding to given alias.
     */
    public static Commands fromAlias(String alias) {
        for (Commands commandType : values()) {
            if (commandType.correspondsTo(alias)) return commandType;
        }
        return null;
    }

    public long call(Message message, TextChannel textChannel, User user, Guild guild, Samaritan samaritan, String[] args) {
        Command command = null;
        try {
            CommandData commandData = new CommandData(samaritan, user, guild, message);
            command = clazz.getDeclaredConstructor(MessageChannel.class, CommandData.class, String[].class).newInstance(textChannel, commandData, args);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Calling command \"");
            // "" + clazz.getSimpleName().replace("Command", "") + "\" on: " + command.toString()
            stringBuilder.append(clazz.getSimpleName().replace("Command", ""));
            stringBuilder.append("\" on: ");
            stringBuilder.append(command.toString());
            stringBuilder.append(" ");
            stringBuilder.append("(").append("Guild: " + guild.getName()).append(", ");
            stringBuilder.append("Channel: #" + textChannel.getName()).append(")");
            samaritan.getLogger().write(stringBuilder.toString());
            command.start();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return command == null ? 0 : command.getId();
    }

    /**
     * @return command description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return command aliases
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * @return Required Access Level to execute.
     */
    public int getRequiredAccessLevel() {
        return requiredAccessLevel;
    }

    /**
     * @return whether the given String corresponds to an alias.
     */
    public boolean correspondsTo(String labelToCheck) {
        return aliases.contains(labelToCheck.toLowerCase());
    }

    /**
     * @param alias The alias to check.
     * @return {@code true} if the alias corresponds to any command, {@code false} otherwise.
     */
    public static boolean isValidCommandAlias(String alias) {
        for (Commands commandsRegistry : values()) {
            if (commandsRegistry.correspondsTo(alias)) return true;
        }
        return false;
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public static int longestStringLength() {
        int longest = 0;
        for (Commands commandsRegistry : values())
            longest = Math.max(longest, commandsRegistry.getAliases().get(0).length());
        return longest;
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public static int longestDescriptionLength() {
        int longest = 0;
        for (Commands commandsRegistry : values())
            longest = Math.max(longest, commandsRegistry.getDescription().length());
        return longest;
    }


}
