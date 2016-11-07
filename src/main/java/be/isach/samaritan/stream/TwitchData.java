package be.isach.samaritan.stream;

import java.util.List;

/**
 * Created by sacha on 31-10-16.
 */
public class TwitchData extends StreamData {

    private String clientId;

    public TwitchData(String clientId, List<String> streamers) {
        super(streamers);
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}
