package be.isach.samaritan.command;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

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
public enum CommandType {

    /**
     * REMINDER:
     * Access Levels.
     * 4: Owner or Admin. Owner Access can't be changed.
     * Then Levels go from 3 to 0.
     */

    BASH(CommandBash.class, 4, "Runs Bash Command", "bash"),
    BRAINFUCK(CommandBrainfuck.class, 0, "Runs BrainFuck code", "brainfuck", "bf"),
    CAT(CommandCat.class, 0, "Shows a cat image", "cat"),
    CLEAN(CommandClean.class, 2, "Cleans fail lol", "clean"),
    CONVERT(CommandConvert.class, 2, "Converts WebM -> mp3", "convert"),
    DOWNLOAD(CommandDownload.class, 2, "Downloads a Youtube Video.", "download"),
    EVAL(CommandEval.class, 2, "Runs JS code.", "eval"),
    FIND_THE_NUMBER(CommandFindTheNumber.class, 0, "Starts a Find The Number game.", "findthenumber", "ftn"),
    GIF(CommandGif.class, 0, "Sends a gif", "gif"),
    GITHUB(CommandGithub.class, 0, "Shows Github link", "git", "github"),
    HASTE(CommandHaste.class, 0, "Prints Hastebin Code.", "haste"),
    HELP(CommandHelp.class, 0, "Prints this", "help"),
    JOIN_ME(CommandJoinMe.class, 2, "Joins Admin.", "joinme"),
    LEET(CommandLeet.class, 0, "Translates text to Leet.", "leet"),
    //    LUA(CommandLua.class, true, "Runs LUA code.", "lua"),
    PLAY(CommandPlay.class, 2, "Plays a Music", "play"),
    POKE_GO(CommandPokeGo.class, 2, "Pokémon Go!", "pokego", "pokemon", "pokego", "go"),
    PRINT_HISTORY(CommandPrintHistory.class, 0, "Prints History of given size", "history"),
    MEME(CommandMeme.class, 0, "Shows a cool meme.", "meme"),
    QUOTE(CommandQuote.class, 0, "Quotes a message", "quote"),
    SAY(CommandSay.class, 2, "Says a message", "say", "print"),
    SEND(CommandSend.class, 2, "Sends a WebSocket", "send", "socket"),
    SET_LEVEL(CommandSetLevel.class, 4, "Sets a user Level.", "setlevel"),
    SET_NAME(CommandSetName.class, 4, "Sets new name", "setname"),
    SHUFFLE(CommandShuffle.class, 2, "Toggles Shuffle Mode", "shuffle"),
    SONGS(CommandSongs.class, 0, "Lists songs", "songs"),
    SHUTDOWN(CommandShutdown.class, 4, "Stops Samaritan [!!]", "stop", "shutdown"),
    TIC_TAC_TOE(CommandTicTacToe.class, 0, "Starts a Tic Tac Toe Game", "tictactoe", "ttt"),
    TWEET(CommandTweet.class, 2, "Tweets", "tweet"),
    UNLEET(CommandUnleet.class, 0, "Unleets a message", "unleet"),
    UPTIME(CommandUptime.class, 0, "Shows uptime", "uptime"),
    USERINFO(CommandUserInfo.class, 0, "Show infos about a User.", "userinfo", "user-info");

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

    CommandType(Class<? extends Command> clazz, int requiredAccessLevel, String description, String... aliases) {
        this.clazz = clazz;
        this.aliases = Arrays.asList(aliases);
        this.description = description;
        this.requiredAccessLevel = requiredAccessLevel;
    }

    /**
     * @param alias The command alias.
     * @return The command corresponding to given alias.
     */
    public static CommandType fromAlias(String alias) {
        for (CommandType commandType : values()) {
            if (commandType.correspondsTo(alias)) return commandType;
        }
        return null;
    }

    public long call(TextChannel textChannel, User user, Guild guild, Samaritan samaritan, String[] args) {
        Command command = null;
        try {
            CommandData commandData = new CommandData(samaritan, user, guild);
            command = clazz.getDeclaredConstructor(MessageChannel.class, CommandData.class, String[].class).newInstance(textChannel, commandData, args);
            System.out.println("Calling command: " + command.toString());
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
        for (CommandType commandsRegistry : values()) {
            if (commandsRegistry.correspondsTo(alias)) return true;
        }
        return false;
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public static int longestStringLength() {
        int longest = 0;
        for (CommandType commandsRegistry : values())
            longest = Math.max(longest, commandsRegistry.getAliases().get(0).length());
        return longest;
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public static int longestDescriptionLength() {
        int longest = 0;
        for (CommandType commandsRegistry : values())
            longest = Math.max(longest, commandsRegistry.getDescription().length());
        return longest;
    }


}
