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

    BRAINFUCK(CommandBrainfuck.class, true, "Runs BrainFuck code", "brainfuck", "bf"),
    CAT(CommandCat.class, false, "Shows a cat image", "cat"),
    //    DOWNLOAD(CommandDownload.class, false, "Downloads a Youtube Video.", "download"),
    EVAL(CommandEval.class, false, "Runs JS code.", "eval"),
    FIND_THE_NUMBER(CommandFindTheNumber.class, true, "Starts a Find The Number game.", "findthenumber", "ftn"),
    GIF(CommandGif.class, true, "Sends a gif", "gif"),
    HASTE(CommandHaste.class, true, "Prints Hastebin Code.", "haste"),
    HELP(CommandHelp.class, true, "Prints this", "help"),
    JOIN_ME(CommandJoinMe.class, false, "Joins Admin.", "joinme"),
    LEET(CommandLeet.class, true, "Translates text to Leet.", "leet"),
//    LUA(CommandLua.class, true, "Runs LUA code.", "lua"),
    PLAY(CommandPlay.class, false, "Plays a Music", "play"),
    PRINT_HISTORY(CommandPrintHistory.class, true, "Prints History of given size", "history"),
    //    MEME(CommandMeme.class, false, "Shows a cool meme.", "meme"),
    QUOTE(CommandQuote.class, false, "Quotes a message", "quote"),
    SAY(CommandSay.class, false, "Says a message", "say", "print"),
    SEND(CommandSend.class, false, "Sends a WebSocket", "send", "socket"),
    SET_NAME(CommandSetName.class, false, "Sets new name", "setname"),
    SHUFFLE(CommandShuffle.class, false, "Toggles Shuffle Mode", "shuffle"),
    SONGS(CommandSongs.class, false, "Lists songs", "songs"),
    SHUTDOWN(CommandShutdown.class, false, "Stops Samaritan [!!]", "stop", "shutdown"),
    TIC_TAC_TOE(CommandTicTacToe.class, true, "Starts a Tic Tac Toe Game", "tictactoe", "ttt"),
    UPTIME(CommandUptime.class, true, "Shows uptime", "uptime"),
    USERINFO(CommandUserInfo.class, true, "Show infos about a User.", "userinfo", "user-info");

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
    private boolean publiic;

    CommandType(Class<? extends Command> clazz, boolean publiic, String description, String... aliases) {
        this.clazz = clazz;
        this.aliases = Arrays.asList(aliases);
        this.description = description;
        this.publiic = publiic;
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
     * @return Whether command is public or not.
     */
    public boolean isPublic() {
        return publiic;
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

}
