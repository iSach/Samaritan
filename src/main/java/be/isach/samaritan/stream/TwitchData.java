package be.isach.samaritan.stream;

import com.mb3364.twitch.api.Twitch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sacha on 31-10-16.
 */
public class TwitchData {

    private String clientId;
    private List<String> streamers = new ArrayList<>();

    public TwitchData(String clientId, List<String> streamers) {
        this.clientId = clientId;
        this.streamers = streamers;
    }

    public String getClientId() {
        return clientId;
    }

    public List<String> getStreamers() {
        return streamers;
    }
}
