package be.isach.samaritan.command;

import net.dv8tion.jda.core.entities.MessageChannel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by sacha on 31-10-16.
 */
public class CommandBirthday extends Command {
    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandBirthday(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        if (args == null || args.length == 0) {
            help();
            return;
        }
        String arg = args[0];
        switch (arg) {
            default:
                help();
                break;
            case "add":
                add(args);
                break;
            case "remove":
                remove(args);
                break;
            case "list":
                list();
                break;
        }
    }

    private void add(String... args) {
        if (args.length < 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```");
            stringBuilder.append("-birthday add @User DD/MM/YYYY [HH:MM]");
            stringBuilder.append("```");
            getMessageChannel().sendMessage(stringBuilder.toString());
            return;
        }
        String streamer = args[1];
        JSONObject obj;
        try {
            obj = new JSONObject(new String(Files.readAllBytes(Paths.get("twitch.json"))));
            JSONArray array = obj.getJSONArray("streamers");
            final boolean[] existsYet = {false};
            array.forEach(userObject -> {
                if(userObject.toString().equalsIgnoreCase(streamer)) {
                    existsYet[0] = true;
                }
            });
            if(existsYet[0]) {
                getMessageChannel().sendMessage("Channel " + streamer + " already added");
                return;
            }
            array.put(streamer.toLowerCase());
            Files.write(Paths.get("twitch.json"), obj.toString(4).getBytes());
            getMessageChannel().sendMessage("Channel " + streamer + " added");
            getSamaritan().getTwitchModule().initialize(streamer);
        } catch (IOException e) {
            getMessageChannel().sendMessage("Error while adding " + streamer);
            e.printStackTrace();
        }
    }

    private void remove(String... args) {
        if (args.length < 2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```");
            stringBuilder.append("-twitch remove [streamer]");
            stringBuilder.append("```");
            getMessageChannel().sendMessage(stringBuilder.toString());
            return;
        }
        String streamer = args[1];
        JSONObject obj;
        try {
            obj = new JSONObject(new String(Files.readAllBytes(Paths.get("twitch.json"))));
            JSONArray userObject = obj.getJSONArray("streamers");
            boolean found = false;
            for(int i = 0; i < userObject.length(); i++) {
                Object o = userObject.get(i);
                if(o.toString().equalsIgnoreCase(streamer)) {
                    userObject.remove(i);
                    found = true;
                }
            }
            Files.write(Paths.get("twitch.json"), obj.toString(4).getBytes());
            if(found){
                getMessageChannel().sendMessage("Channel " + streamer + " removed");
            } else {
                getMessageChannel().sendMessage("Channel " + streamer + " is not an added streamer.");
            }
            getSamaritan().getTwitchModule().removeChannel(streamer);
        } catch (IOException e) {
            getMessageChannel().sendMessage("Error while adding " + streamer);
            e.printStackTrace();
        }
    }

    private void list() {
        JSONObject obj;
        try {
            obj = new JSONObject(new String(Files.readAllBytes(Paths.get("twitch.json"))));
            JSONArray array = obj.getJSONArray("streamers");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```");
            stringBuilder.append("Streamers:").append("\n");
            array.forEach(streamee -> stringBuilder.append(streamee).append("\n"));
            stringBuilder.append("```");
            getMessageChannel().sendMessage(stringBuilder.toString());
        } catch (IOException e) {
            getMessageChannel().sendMessage("Error while showing streamers");
            e.printStackTrace();
        }
    }

    private void help() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```");
        stringBuilder.append("Sub-Commands:").append("\n");
        ;
        stringBuilder.append("    -twitch add [streamer]").append("\n");
        stringBuilder.append("    -twitch remove [streamer]");
        stringBuilder.append("    -twitch list");
        ;
        stringBuilder.append("```");
        getMessageChannel().sendMessage(stringBuilder.toString());
    }
}
