package be.isach.samaritan.command;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.entities.*;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 19th mai, 2016
 * at 20:20
 * <p>
 * Makes Samaritan join the executor.
 */
class CommandJoinMe extends Command {

    /**
     * Command Constructor.
     *
     * @param textChannel The text Channel where command is called.
     * @param args        The args provided when command was called.
     */
    CommandJoinMe(MessageChannel textChannel, CommandData commandData, String[] args) {
        super(textChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        User user = getJda().getUsersByName(getSamaritan().admin).get(0);
        VoiceChannel voiceChannel = null;
        for (Guild guild : getJda().getGuilds())
            for (VoiceChannel voiceChannell : guild.getVoiceChannels())
                if (voiceChannell.getUsers().contains(user)) voiceChannel = voiceChannell;
        if (voiceChannel == null) {
            getMessageChannel().sendMessage("I couldn't find you in any voice channel, please join one.");
            return;
        }
        voiceChannel.getGuild().getAudioManager().openAudioConnection(voiceChannel);
    }
}
