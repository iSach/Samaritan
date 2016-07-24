package be.isach.samaritan.command;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.util.MathUtils;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 29th mai, 2016
 * at 02:56
 */
class CommandMeme extends Command {

    private static final String[] MEMES = new String[] {
              "pepe the frog",
            "lenny face",
            "kappa",
            "know that feel",
            "forever alone",
            "trollface",
            "me gusta",
            "dolan",
            "bitch please",
            "Y U NO",
            "not bad"
    };

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandMeme(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        getMessageChannel().sendMessage(getSamaritan().getGifFactory().getRandomGif(MEMES[MathUtils.getRandom().nextInt(MEMES.length)]));
    }
}
