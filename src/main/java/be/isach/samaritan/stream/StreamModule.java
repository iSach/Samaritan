package be.isach.samaritan.stream;

import be.isach.samaritan.Samaritan;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by sacha on 07-11-16.
 */
public abstract class StreamModule extends TimerTask {

    protected enum Status {
        ONLINE, OFFLINE
    }

    protected Map<String, Status> streamersMap;
    protected JDA jda;
    private Samaritan samaritan;

    public StreamModule(JDA jda, StreamData streamData, Samaritan samaritan) {
        this.jda = jda;
        this.streamersMap = new HashMap<>();
        this.samaritan = samaritan;

        streamData.getStreamers().forEach(s -> streamersMap.put(s, null));
    }

    abstract public void initialize(String channel);

    abstract void check(String channel);

    abstract void broadcastLive(StreamerChannel channel);

    @Override
    public void run() {
        streamersMap.keySet().stream().filter(streamer -> streamer != null).forEach(this::check);
    }

    public final void removeChannel(String channel) {
        streamersMap.remove(channel);
    }

    protected final void sendMessage(String... messages) {
        TextChannel textChannel = jda.getTextChannelById("242305313527562240");
        for (String message : messages) {
            textChannel.sendMessage(message);
        }
    }

    protected final void initChannels() {
        streamersMap.keySet().forEach(this::initialize);
    }

    public Samaritan getSamaritan() {
        return samaritan;
    }
}
