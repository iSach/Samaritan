package be.isach.samaritan.stream;

import be.isach.samaritan.Samaritan;
import com.google.common.util.concurrent.Futures;
import net.dv8tion.jda.core.JDA;
import pro.beam.api.BeamAPI;
import pro.beam.api.resource.channel.BeamChannel;
import pro.beam.api.response.users.UserSearchResponse;
import pro.beam.api.services.impl.UsersService;
import pro.beam.api.util.ResponseHandler;

/**
 * Created by sacha on 30-10-16.
 */
public class BeamModule extends StreamModule {

    public BeamModule(JDA jda, StreamData streamData, Samaritan samaritan) {
        super(jda, streamData, samaritan);
        initChannels();
    }

    @Override
    public void initialize(String channel) {
//        BeamAPI beam = new BeamAPI();
//
//        Futures.addCallback(beam.use(UsersService.class).search(channel), new ResponseHandler<UserSearchResponse>() {
//            @Override
//            public void onSuccess(UserSearchResponse response) {
//                if(response.size() > 0) {
//                    BeamChannel beamChannel = response.get(0).channel;
//                    Status currentStatus = beamChannel == null ? Status.OFFLINE : beamChannel.online ? Status.ONLINE : Status.OFFLINE;
//                    streamersMap.put(channel, currentStatus);
//                }
//            }
//        });
    }

    @Override
    void check(String channel) {
//        BeamAPI beam = new BeamAPI();
//
//        Futures.addCallback(beam.use(UsersService.class).search(channel), new ResponseHandler<UserSearchResponse>() {
//            @Override
//            public void onSuccess(UserSearchResponse response) {
//                if(response.size() > 0) {
//                    Status lastStatus = streamersMap.get(channel);
//                    BeamChannel beamChannel = response.get(0).channel;
//                    Status currentStatus = beamChannel == null ? Status.OFFLINE : beamChannel.online ? Status.ONLINE : Status.OFFLINE;
//
//                    getSamaritan().getLogger().write("Bealm Channel: \"" + channel + "\" is: " + currentStatus);
//
//                    StreamerChannel streamerChannel = new StreamerChannel(null, beamChannel.name, channel);
//
//                    if (currentStatus == Status.ONLINE && lastStatus == Status.OFFLINE) {
//                        broadcastLive(streamerChannel);
//                    }
//
//                    streamersMap.put(channel, currentStatus);
//                }
//            }
//        });
    }

    @Override
    void broadcastLive(StreamerChannel channel) {
        sendMessage("Hey! " + channel.getChannelName() + " est en live sur Beam !");
        sendMessage(channel.getDisplayName() + " | https://beam.pro/" + channel.getChannelName());
    }
}