package be.isach.samaritan.command;

import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 01th juin, 2016
 * at 13:52
 */
public class CommandTicTacToe extends Command {

    private static final String PATTERN =
            "```\n" +
            "     1   2   3       \n" +
            "\n" +
            "A    a | b | c\n" +
            "    ---+---+---\n" +
            "B    d | e | f\n" +
            "    ---+---+---\n" +
            "C    g | h | i\n" +
            "```";

    private static final char[] PATTERN_LETTERS = new char[] {
            'a', 'b', 'c',
            'd', 'e', 'f',
            'g', 'h', 'i'
    };

    private char[] score = new char[9];

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandTicTacToe(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        String opponentUsername = args[0];
        if(opponentUsername.isEmpty()) {
            getMessageChannel().sendMessage("Who do you want to play against?");
            opponentUsername = nextMessage().getContent().split(" ")[0];
        }
        User user = getJda().getUsersByName(opponentUsername).get(0);
        if(user == null || user.getOnlineStatus() != OnlineStatus.ONLINE) {
            getMessageChannel().sendMessage("This user isn't online, or doesn't exist.");
        }

    }
}
