package be.isach.samaritan.start;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.json.AdvancedJSONObject;
import be.isach.samaritan.stream.StreamData;
import be.isach.samaritan.stream.TwitchData;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.start
 * Created by: Sacha
 * Created on: 30th mai, 2016
 * at 00:29
 */
public class SamaritanMain {

    /**
     * Working Directory.
     */
    private static File workingDirectory;

    /**
     * Checks file, and generates them.
     *
     * @param args
     */
    public static void main(String[] args) {
        workingDirectory = new File("./");
        if (args.length > 0 && !args[0].isEmpty())
            workingDirectory = new File(args[0]);

        File folder = new File(workingDirectory, "");
        File logsFolder = new File(workingDirectory, "logs");
        File musicFolder = new File(workingDirectory, "music");
        File configFile = new File(workingDirectory, "config.json");
        File usersFile = new File(workingDirectory, "users.json");
        File twitchConfigFile = new File(workingDirectory, "twitch.json");
        File beamConfigFile = new File(workingDirectory, "beam.json");

        logsFolder.mkdir();
        musicFolder.mkdir();

        Samaritan samaritan = null;

        // Config.json
        try {
            AdvancedJSONObject object = new AdvancedJSONObject(new String(Files.readAllBytes(Paths.get("config.json"))));
            object.addDefault("bot-token", "");
            object.addDefault("web-ui", false);
            object.addDefault("web-ui-websocket-port", 11350);
            object.addDefault("bot-owner-id", 93721838093352960L);
            Files.write(Paths.get("config.json"), object.toString(4).getBytes());
            String botToken = object.getString("bot-token");
            boolean webUi = object.getBoolean("web-ui");
            int uiWebSocketPort = object.getInt("web-ui-websocket-port");
            long ownerId = object.getLong("bot-owner-id");
            String googleMapsApiKey = object.getString("google-maps-api-key");
            samaritan = new Samaritan(args, botToken, webUi, uiWebSocketPort, ownerId, workingDirectory);
        } catch (IOException e) {
            System.out.println("---------------------------");
            System.out.println("");
            System.out.println("Samaritan vBeta 2.0.1.");
            System.out.println("");
            System.out.println("No config.json detected. Generating new one.");
            System.out.println("");
            JSONObject object = new JSONObject();
            object.put("bot-token", "");
            object.put("web-ui", false);
            object.put("web-ui-websocket-port", 11350);
            object.put("bot-owner-id", 93721838093352960L);
            try {
                Files.write(Paths.get("config.json"), object.toString(4).getBytes());
                System.out.println("Please set a valid token in this file.");
                System.out.println("");
                System.out.println("Stopping...");
                System.out.println("");
                System.exit(0);
            } catch (IOException e1) {
                System.out.println("No config was found, and Samaritan failed to generate a new one.");
                e1.printStackTrace();
                System.exit(0);
            }
        }

        // Twitch.json
        createJsonFileStream("twitch", samaritan);

        // Beam.json
        createJsonFileStream("beam", samaritan);
    }

    private static void createJsonFileStream(String platform, Samaritan samaritan) {
        try {
            AdvancedJSONObject object = new AdvancedJSONObject(new String(Files.readAllBytes(Paths.get(platform + ".json"))));
            JSONArray array = object.getJSONArray("streamers");
            List<String> streamers = new ArrayList<>();
            array.forEach(streamer -> streamers.add(streamer.toString().toLowerCase()));
            if(platform.equals("twitch")) {
                String clientId = object.getString("twitch-client-id");
                TwitchData twitchData = new TwitchData(clientId, streamers);
                samaritan.initTwitchModule(twitchData);
            } else {
                StreamData streamData = new StreamData(streamers);
                samaritan.initBeamModule(streamData);
            }
        } catch (IOException e) {
            System.out.println("No " + platform + ".json detected. Generating new one.");
            System.out.println("");
            JSONObject object = new JSONObject();
            if (platform.equalsIgnoreCase("twitch")) {
                object.put("twitch-client-id", "yourclientid");
            }
            JSONArray array = new JSONArray();
            array.put("isachhh");
            object.put("streamers", array);
            try {
                Files.write(Paths.get(platform + ".json"), object.toString(4).getBytes());
            } catch (IOException e1) {
                System.out.println("No twitch.json was found, and Samaritan failed to generate a new one.");
                e1.printStackTrace();
                System.exit(0);
            }
        }
    }
}
