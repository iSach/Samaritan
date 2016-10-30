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
public class StreamModule extends TimerTask {

    private enum Status {
        ONLINE, OFFLINE
    }

    private Map<String, Status> streamersMap;
    private Twitch twitch;
    private JDA jda;

    public StreamModule(JDA jda, TwitchData twitchData) {
        this.jda = jda;
        this.streamersMap = new HashMap<>();
        this.twitch = new Twitch();
        twitch.setClientId(twitchData.getClientId());

        twitchData.getStreamers().forEach(s -> streamersMap.put(s, null));

        streamersMap.keySet().forEach(channel -> twitch.streams().get(channel, new StreamResponseHandler() {
            public void onSuccess(Stream stream) {
                Status currentStatus = stream == null ? Status.OFFLINE : stream.isOnline() ? Status.ONLINE : Status.OFFLINE;
                streamersMap.put(channel, currentStatus);
            }
            public void onFailure(int i, String s, String s1) {
                streamersMap.put(channel, Status.OFFLINE);
            }
            public void onFailure(Throwable throwable) {
                streamersMap.put(channel, Status.OFFLINE);}
        }));
    }

    // Triggers each 30 seconds.
    public void run() {
        System.out.println(streamersMap);
        streamersMap.keySet().forEach(channel -> {
            Status lastStatus = streamersMap.get(channel);
            twitch.streams().get("iSachhh", new StreamResponseHandler() {
                public void onSuccess(Stream stream) {
                    Status currentStatus = stream == null ? Status.OFFLINE : stream.isOnline() ? Status.ONLINE : Status.OFFLINE;

                    System.out.println(channel + ": " + currentStatus);

                    // Goes Online.
                    if (currentStatus == Status.ONLINE && lastStatus == Status.OFFLINE) {
                        broadcastLive(stream);
                    }

                    streamersMap.put(channel, currentStatus);
                }

                public void onFailure(int i, String s, String s1) {

                }

                public void onFailure(Throwable throwable) {

                }
            });
        });
    }

    private void broadcastLive(Stream stream) {
        Channel channel = stream.getChannel();
        TextChannel textChannel = jda.getTextChannelById("242305313527562240");
        textChannel.sendMessage("Hey! " + channel.getDisplayName() + " est en live !");
        textChannel.sendMessage("Joue à : " + stream.getGame());
        textChannel.sendMessage("\"" + channel.getStatus() + "\" https://twitch.tv/" + channel.getName());
    }
}