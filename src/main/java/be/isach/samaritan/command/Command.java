package be.isach.samaritan.command;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.message.MessageScanningThread;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 19th mai, 2016
 * at 19:14
 * <p>
 * Description: Command Super-class.
 */
abstract class Command extends MessageScanningThread {

    /**
     * Args provided by user when the command was called.
     */
    private String[] args;

    /**
     * Samaritan Instance.
     */
    protected CommandData data;

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    Command(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData.getSamaritan().getJda(), commandData.getExecutor());

        this.data = commandData;
        this.args = args;
    }

    /**
     * Executed when command is called.
     */
    @Override
    protected void onRun() {
        onExecute(args);
    }

    /**
     * Retrieves easily a String containing the args.
     * <p>
     * Won't add a space after last arg.
     * <p>
     * e.g:
     * ["Hello", "How", "Are", "You"]
     * ->
     * "Hello How Are You"
     *
     * @return easily a String containing the args
     */
    final String buildStringFromArgs(int j) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = j; i < args.length; i++)
            stringBuilder.append(args[i] + (i == args.length - 1 ? "" : " "));
        return stringBuilder.toString();
    }

    /**
     * Retrieves easily a String containing the args.
     * <p>
     * Won't add a space after last arg.
     * <p>
     * e.g:
     * ["Hello", "How", "Are", "You"]
     * ->
     * "Hello How Are You"
     *
     * @return easily a String containing the args
     */
    final String buildStringFromArgs() {
        return buildStringFromArgs(0);
    }

    /**
     * @return Command Data.
     */
    private CommandData getData() {
        return data;
    }

    /**
     * @return Samaritan Instance.
     */
    final Samaritan getSamaritan() {
        return getData().getSamaritan();
    }

    /**
     * @return Samaritan Instance.
     */
    final User getExecutor() {
        return getData().getExecutor();
    }

    final Member getMember() {
        return getGuild().getMember(getExecutor());
    }

    /**
     * @return Guild where command was executed.
     */
    final Guild getGuild() {
        return getData().getGuild();
    }

    /**
     * Made for subclasses, called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    abstract void onExecute(String[] args);
}
