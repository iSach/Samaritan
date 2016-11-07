package be.isach.samaritan.stream;

import org.luaj.vm2.ast.Str;

/**
 * Created by sacha on 07-11-16.
 */
public class StreamerChannel {

    private String game;
    private String displayName;
    private String channelName;

    public StreamerChannel(String game, String displayName, String channelName) {
        this.game = game;
        this.displayName = displayName;
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGame() {
        return game;
    }
}
