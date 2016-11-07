package be.isach.samaritan.stream;

import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.StreamResponseHandler;
import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Stream;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.TextChannel;

import java.util.*;

/**
 * Created by sacha on 30-10-16.
 */
public class TwitchModule extends StreamModule {

    private Twitch twitch;

    public TwitchModule(JDA jda, TwitchData twitchData) {
        super(jda, twitchData);

        this.twitch = new Twitch();
        twitch.setClientId(twitchData.getClientId());
    }

    @Override
    public void initialize(String channel) {
        twitch.streams().get(channel, new StreamResponseHandler() {
            public void onSuccess(Stream stream) {
                Status currentStatus = stream == null ? Status.OFFLINE : stream.isOnline() ? Status.ONLINE : Status.OFFLINE;
                streamersMap.put(channel.toLowerCase(), currentStatus);
            }

            public void onFailure(int i, String s, String s1) {
            }

            public void onFailure(Throwable throwable) {
            }
        });
    }

    @Override
    void check(String channel) {
        Status lastStatus = streamersMap.get(channel);
        twitch.streams().get(channel, new StreamResponseHandler() {
            public void onSuccess(Stream stream) {
                Status currentStatus = stream == null ? Status.OFFLINE : stream.isOnline() ? Status.ONLINE : Status.OFFLINE;

                System.out.println("Checking " + channel + ": " + currentStatus);

                String displayName = stream.getChannel().getStatus();
                String game = stream.getGame();

                StreamerChannel streamerChannel = new StreamerChannel(game, displayName, channel);

                // Goes Online.
                if (currentStatus == Status.ONLINE && lastStatus == Status.OFFLINE) {
                    broadcastLive(streamerChannel);
                }

                streamersMap.put(channel, currentStatus);
            }

            public void onFailure(int i, String s, String s1) {

            }

            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    void broadcastLive(StreamerChannel channel) {
        sendMessage("Hey! " + channel.getDisplayName() + " est en live sur Twitch !");
        sendMessage("Joue à : " + channel.getGame());
        sendMessage("\"" + channel.getDisplayName() + "\" https://twitch.tv/" + channel.getChannelName());
    }

    public void removeMap(String channel) {
        streamersMap.remove(channel);
    }
}