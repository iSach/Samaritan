package be.isach.samaritan.command;

import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 01th juin, 2016
 * at 13:52
 * <p>
 * TODO: CLEAN!
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
                    "C    g | h | i\n";

    private static final char[] PATTERN_LETTERS = new char[]{
            'a', 'b', 'c',
            'd', 'e', 'f',
            'g', 'h', 'i'
    };

    private char[] board = new char[]{
            ' ', ' ', ' ',
            ' ', ' ', ' ',
            ' ', ' ', ' '
    };

    private Message boardMessage = null;

    private User opponent;

    private boolean opponentTurn = false;

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
        String opponentUsername = "";
        if (args.length > 0) {
            opponentUsername = args[0];
        }
        if (opponentUsername.isEmpty()) {
            getMessageChannel().sendMessage("Who do you want to play against?");
            opponentUsername = nextMessage().getContent().split(" ")[0];
        }
        this.opponent = getJda().getUsersByName(opponentUsername).get(0);
        if (opponent == null || opponent.getOnlineStatus() != OnlineStatus.ONLINE) {
            getMessageChannel().sendMessage("This user isn't online, or doesn't exist.");
            return;
        }
        getMessageChannel().sendMessage("Starting Tic Tac Toe game!\n" +
                "Opposing " + getExecutor().getUsername() + "(X) and " + opponent.getUsername() + "(O) !\n" +
                "To play, enter the letter A B or C followed by 1 2 or 3 when it's your turn.");

        startGame();
    }

    private void startGame() {
        boolean active = true;
        User winner = null;

        sendBoard(false);

        do {
            try {
                User current = opponentTurn ? opponent : getExecutor();
//                getMessageChannel().sendMessage("Turn of " + current.getUsername() + " (" + (opponentTurn ? 'O' : 'X') + ")");
                String s = nextMessage().getContent();
                while (!isValidLocation(s)) {
                    getMessageChannel().sendMessage("Invalid coordinates. (good coordinates example: B2)");
                    s = nextMessage().getContent();
                }
                while (board[getPositionIndex(s)] != ' ') {
                    getMessageChannel().sendMessage("These coordinates aren't available!");
                    s = nextMessage().getContent();
                }
                board[getPositionIndex(s)] = opponentTurn ? 'O' : 'X';

                sendBoard(!opponentTurn);

                opponentTurn = !opponentTurn;

                User user = checkForWinner();
                if (user != null) {
                    winner = checkForWinner();
                    active = false;
                    break;
                }

                if (board[0] != ' '
                        && board[1] != ' '
                        && board[2] != ' '
                        && board[3] != ' '
                        && board[4] != ' '
                        && board[5] != ' '
                        && board[6] != ' '
                        && board[7] != ' '
                        && board[8] != ' ')
                    break;
            } catch (Exception exc) {
                // lel
            }
        } while (active);
        if (winner == null) {
            getMessageChannel().sendMessage("Game draw! No winner.");
        } else {
            getMessageChannel().sendMessage(winner.getUsername() + " (" + (winner == opponent ? 'O' : 'X') + ") wins!");
        }
    }

    private User checkForWinner() {
        char[] symbols = new char[]{'X', 'O'};
        for (char c : symbols) {
            for (int i = 0; i < 3; i++) {
                if (board[i * 3] == c && board[i * 3 + 1] == c && board[i * 3 + 2] == c) {
                    System.out.println("Detected win!");
                    return c == 'O' ? opponent : getExecutor();
                }
            }
            for (int i = 0; i < 3; i++) {
                if (board[i] == c && board[i + 3] == c && board[i + 6] == c) {
                    System.out.println("Detected win!");
                    return c == 'O' ? opponent : getExecutor();
                }
            }
            if (board[0] == c && board[4] == c && board[8] == c) {
                System.out.println("Detected win!");
                return c == 'O' ? opponent : getExecutor();
            }
            if (board[2] == c && board[4] == c && board[6] == c) {
                System.out.println("Detected win!");
                return c == 'O' ? opponent : getExecutor();
            }
        }
        return null;
    }

    /**
     * Check the given String.
     *
     * @param supposedCoordinates Supposed Coordinates String.
     * @return {@code true} if the given String is a valid coordinate, {@code false} otherwise.
     */
    private boolean isValidLocation(String supposedCoordinates) {
        supposedCoordinates = supposedCoordinates.toUpperCase();
        if (supposedCoordinates.length() < 2) {
            return false;
        }
        if (supposedCoordinates.length() > 2) {
            supposedCoordinates = supposedCoordinates.substring(0, 2);
        }
        if (!Character.isLetter(supposedCoordinates.charAt(0)) || !Character.isDigit(supposedCoordinates.charAt(1))) {
            return false;
        }
        char letter = supposedCoordinates.charAt(0), digit = supposedCoordinates.charAt(1);
        if (letter != 'A' && letter != 'B' && letter != 'C') {
            return false;
        }
        int intDigit = Integer.parseInt(supposedCoordinates.substring(1, 2));
        return !(intDigit < 1 || intDigit > 3);
    }

    /**
     * Gets the index (0, 8) of coo£ttt galaxyoyodinates.
     *
     * @param coordinates The coordinates to take Index from.
     * @return Index calculated from the coordinates.
     */
    private int getPositionIndex(String coordinates) {
        if (!isValidLocation(coordinates)) return -1;

        char letter = Character.toUpperCase(coordinates.charAt(0));
        int digit = Integer.parseInt(coordinates.substring(1, 2)) - 1;
        int letterInt = 0;

        switch (letter) {
            case 'B':
                letterInt = 1;
                break;
            case 'C':
                letterInt = 2;
                break;
        }
        return (letterInt * 3) + digit;
    }

    /**
     * Called for nextMessage management.
     *
     * @param e The event triggered.
     */
    @Override
    public void onEvent(Event e) {
        if (e instanceof MessageReceivedEvent) {
            MessageReceivedEvent event = (MessageReceivedEvent) e;
            if (event.getTextChannel() != getMessageChannel()) return;
            if (event.getAuthor().isBot()) return;
            if ((opponentTurn && !event.getAuthor().equals(opponent))
                    || (!opponentTurn && !event.getAuthor().equals(getExecutor()))) return;
            setScannedMessaged(event.getMessage());
            if (isValidLocation(event.getMessage().getContent()))
                event.getMessage().deleteMessage();
        }
    }

    private void sendBoard(boolean opponentTurn) {
        String replacedPattern = PATTERN + "";
        for (int i = 0; i < 9; i++) {
            replacedPattern = replacedPattern.replace(PATTERN_LETTERS[i], board[i]);
        }
        String finalMessage = replacedPattern + "\n" +
                (opponentTurn ? opponent.getUsername() + " (O)" : getExecutor().getUsername() + " (X)") + "'s Turn\n" +
                "```";
        if (boardMessage == null) {
            boardMessage = (getMessageChannel().sendMessage(finalMessage));
        } else {
            boardMessage.updateMessage(finalMessage);
        }
    }
}
