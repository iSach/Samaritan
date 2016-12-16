package be.isach.samaritan.command;

import at.mukprojects.giphy4j.Giphy;
import be.isach.samaritan.util.MathUtils;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 18th mai, 2016
 * at 23:41
 * <p>
 * Fetches a Gif on Giphy about a given subject.
 * If no subject is given, a random subject between
 * the ones defined in SUBJECT is chosen.
 */
public class CommandGif extends Command {

    /**
     * Subject Array.
     * Contains the subjects to choose in, if no subject is given.
     */
    private static final String[] SUBJECTS = new String[]{
            "cat",
            "simpson",
            "donut",
            "chocolate",
            "pizzas",
            "programming",
            "cs:go",
            "gaben"
    };

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandGif(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        String gifSubject = buildStringFromArgs().split(" ")[0];
        if (gifSubject.isEmpty()) gifSubject = getRandomSubject();
        Giphy giphy = new Giphy("dc6zaTOxFJmzC");
        getMessageChannel().sendMessage("Here is a gif about " + gifSubject);
        getMessageChannel().sendMessage(getSamaritan().getGifFactory().getRandomGif(gifSubject));
    }

    /**
     * @return a random subject in the SUBJECTS static String array.
     */
    private static String getRandomSubject() {
        return SUBJECTS[MathUtils.getRandom().nextInt(SUBJECTS.length)];
    }
}
