package be.isach.samaritan.stream;

import com.google.common.util.concurrent.Futures;
import com.mb3364.twitch.api.Twitch;
import com.mb3364.twitch.api.handlers.StreamResponseHandler;
import com.mb3364.twitch.api.models.Channel;
import com.mb3364.twitch.api.models.Stream;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.TextChannel;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.BeamUser;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.response.users.UserSearchResponse;
import pro.beam.api.services.impl.ChannelsService;
import pro.beam.api.services.impl.UsersService;
import pro.beam.api.util.ResponseHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by sacha on 30-10-16.
 */
public class BeamModule extends StreamModule {

    public BeamModule(JDA jda, StreamData streamData) {
        super(jda, streamData);
        initChannels();
    }

    @Override
    public void initialize(String channel) {
        BeamAPI beam = new BeamAPI();

        Futures.addCallback(beam.use(UsersService.class).search(channel), new ResponseHandler<UserSearchResponse>() {
            @Override
            public void onSuccess(UserSearchResponse response) {
                if(response.size() > 0) {
                    BeamChannel beamChannel = response.get(0).channel;
                    Status currentStatus = beamChannel == null ? Status.OFFLINE : beamChannel.online ? Status.ONLINE : Status.OFFLINE;
                    streamersMap.put(channel, currentStatus);
                }
            }
        });
    }

    @Override
    void check(String channel) {
        BeamAPI beam = new BeamAPI();

        Futures.addCallback(beam.use(UsersService.class).search(channel), new ResponseHandler<UserSearchResponse>() {
            @Override
            public void onSuccess(UserSearchResponse response) {
                if(response.size() > 0) {
                    Status lastStatus = streamersMap.get(channel);
                    BeamChannel beamChannel = response.get(0).channel;
                    Status currentStatus = beamChannel == null ? Status.OFFLINE : beamChannel.online ? Status.ONLINE : Status.OFFLINE;

                    System.out.println("BEAM | Checking " + channel + ": " + currentStatus);

                    StreamerChannel streamerChannel = new StreamerChannel(null, beamChannel.name, channel);

                    if (currentStatus == Status.ONLINE && lastStatus == Status.OFFLINE) {
                        broadcastLive(streamerChannel);
                    }

                    streamersMap.put(channel, currentStatus);
                }
            }
        });
    }

    @Override
    void broadcastLive(StreamerChannel channel) {
        sendMessage("Hey! " + channel.getDisplayName() + " est en live sur Beam !");
        sendMessage("https://beam.pro/" + channel.getDisplayName());
    }
}