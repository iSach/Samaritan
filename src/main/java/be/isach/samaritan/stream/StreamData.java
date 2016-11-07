package be.isach.samaritan.stream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sacha on 07-11-16.
 */
public class StreamData {

    private List<String> streamers = new ArrayList<>();

    public StreamData(List<String> streamers) {
        this.streamers = streamers;
    }

    public List<String> getStreamers() {
        return streamers;
    }

}
