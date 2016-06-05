package be.isach.samaritan.start;

import be.isach.samaritan.Samaritan;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

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

        logsFolder.mkdir();
        musicFolder.mkdir();

        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("config.json"))));
            String botToken = obj.getString("bot-token");
            boolean webUi = obj.getBoolean("web-ui");
            int uiWebSocketPort = obj.getInt("web-ui-websocket-port");
            long ownerId = obj.getLong("bot-owner-id");
            new Samaritan(args, botToken, webUi, uiWebSocketPort, ownerId, workingDirectory);
        } catch (IOException e) {
            System.out.println("---------------------------");
            System.out.println("");
            System.out.println("Samaritan vBeta 2.0.");
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
    }
}
