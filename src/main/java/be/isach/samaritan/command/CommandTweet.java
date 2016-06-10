package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 10th juin, 2016
 * at 13:10
 */
class CommandTweet extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandTweet(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        Twitter twitter = TwitterFactory.getSingleton();
        try {
            Status status = twitter.updateStatus(buildStringFromArgs());
            String url = "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
            getMessageChannel().sendMessage("I tweeted:\n`" + buildStringFromArgs() + "`\nsuccessfully on the account: @" + twitter.getScreenName() + "\n\nTweet Link: " + url);
        } catch (TwitterException e) {
            getMessageChannel().sendMessage("Oh, something went wrong. (" + e.getMessage());
        }
    }
}
