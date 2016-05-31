package be.isach.samaritan.start;

import be.isach.samaritan.Samaritan;

import java.io.*;
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
     * Properties Object for samaritan.properties file.
     */
    private static Properties properties = new Properties();

    /**
     * Output Stream for samaritan.properties file.
     */
    private static OutputStream output = null;

    /**
     * Input Stream for samaritan.properties file.
     */
    private static InputStream input = null;

    /**
     * Checks file, and generates them.
     *
     * @param args
     */
    public static void main(String[] args) {
        if (!checkFiles()) {
            System.out.println("---------------------------");
            System.out.println("");
            System.out.println("Samaritan vBeta 2.0.");
            System.out.println("");
            System.out.println("No samaritan.properties detected. Generating new one.");
            System.out.println("");
            System.out.println("Please set a valid token in this file.");
            System.out.println("");
            System.out.println("Stopping...");
            System.out.println("");
            System.exit(0);
            return;
        }
        String botToken = properties.getProperty("bot_token");
        boolean webUi = Boolean.parseBoolean((String) properties.get("web_ui"));
        int uiWebSocketPort = Integer.parseInt((String) properties.get("ui_websocket_port"));
        String admin = properties.getProperty("admin");
        new Samaritan(args, botToken, webUi, uiWebSocketPort, admin);
    }

    /**
     * Checks and generate Samaritan files.
     *
     * @return {@code true} if samaritan.properties existed, {@code false} otherwise.
     */
    private static boolean checkFiles() {
        File folder = new File("");
        File logsFolder = new File("logs");
        File musicFolder = new File("music");
        File propertiesFile = new File("samaritan.properties");

        boolean existed = true;

        try {
            logsFolder.mkdir();
            musicFolder.mkdir();
            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
                existed = false;
            } else {
                input = new FileInputStream("samaritan.properties");
                properties.load(input);
            }
            output = new FileOutputStream(propertiesFile);
            addDefault("bot_token", "REPLACE_THIS");
            addDefault("ui_websocket_port", "13350");
            addDefault("web_ui", "false");
            addDefault("admin", "admin");
            properties.save(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return existed;
    }

    private static void addDefault(String key, String value) {
        if (!properties.containsKey(key)) {
            properties.setProperty(key, value);
        }
    }

}
