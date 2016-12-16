package be.isach.samaritan.command;

import be.isach.samaritan.util.MathUtils;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 31th mai, 2016
 * at 18:58
 */
public class CommandFindTheNumber extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandFindTheNumber(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        int lives = 6, max = 100, randomNumber = MathUtils.getRandom().nextInt(max + 1);
        int currentGuess = -1;

        getMessageChannel().sendMessage("Hey!\n" +
                "Welcome to the Find The Number game!\n" +
                "You have " + lives + " tries.\n" +
                "Try to find the correct number between 0 and " + max);

        do {
            String s = nextMessage().getContent();
            if (!MathUtils.isInteger(s)) {
                getMessageChannel().sendMessage("This isn't a number, please enter a number.");
                continue;
            }
            currentGuess = Integer.parseInt(s);

            if (currentGuess == randomNumber) break;

            lives--;

            if (lives < 1) break;

            getMessageChannel().sendMessage("Wrong guess. You have " + lives + " tries left.\nThe correct number is " +
                    (randomNumber > currentGuess ? "higher" : "lower") +
                    " than " + currentGuess + ".");
        } while (currentGuess != randomNumber);

        if (lives < 1)
            getMessageChannel().sendMessage("You lost :( The correct number was: " + randomNumber + ".");
        else {
            getMessageChannel().sendMessage("GG! You won!\n" +
                    "You receive one cookie!");
        }
    }
}
