package be.isach.samaritan.command;

import be.isach.samaritan.util.UrbanData;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by sacha on 25/12/2016.
 */
public class CommandUrban extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandUrban(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        UrbanData data = getData(buildStringFromArgs());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Definition for: " + data.getWord());
        embedBuilder.setDescription(data.getDefinition());
        embedBuilder.setThumbnail("https://upload.wikimedia.org/wikipedia/en/b/b7/Urban_dictionary_--_logo.jpg");
        embedBuilder.setFooter("Information requested by " + getExecutor().getName(), data.getLink());
        getMessageChannel().sendMessage(embedBuilder.build()).queue();
    }

    private UrbanData getData(String unfilteredQuery) {
        String query = unfilteredQuery.replace(" ", "%20");
        try {
            URL url = new URL("http://api.urbandictionary.com/v0/define?term=" + query);
            InputStream in = url.openStream();
            Scanner scan = new Scanner(in);
            String jsonstring = "";
            while (scan.hasNext()) {
                jsonstring += scan.next() + " ";
            }
            scan.close();
            Gson gson = new GsonBuilder().create();
            JsonObject json = gson.fromJson(jsonstring, JsonElement.class).getAsJsonObject();
            if (json.get("result_type").getAsString().equals("no_results")) {
                String title = "No results found for " + query;
            }
            JsonObject result = json.get("list").getAsJsonArray().get(0).getAsJsonObject();
            String word = result.get("word").getAsString();
            String permalink = result.get("permalink").getAsString();
            String definition = result.get("definition").getAsString();
            return new UrbanData(word, definition, permalink);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }
}
