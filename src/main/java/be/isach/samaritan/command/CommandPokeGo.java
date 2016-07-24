package be.isach.samaritan.command;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import be.isach.samaritan.pokemongo.LoginData;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import net.dv8tion.jda.entities.MessageChannel;
import okhttp3.OkHttpClient;

import com.google.appengine.api.urlfetch.URLFetchServiceFactory

/**
 * Package: be.isach.samaritan.command
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class CommandPokeGo extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandPokeGo(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        if (getSamaritan().getPokemonGo() == null) {
            getMessageChannel().sendMessage("Pokemon Go Instance is null.");
            return;
        }

        PokemonGo go = getSamaritan().getPokemonGo();

        if (args.length == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```");
            stringBuilder.append("Username:").append(" ").append(go.getPlayerProfile().getUsername());
            stringBuilder.append("\n");
            stringBuilder.append("Team:").append(" ").append(go.getPlayerProfile().getTeam());
            stringBuilder.append("\n");
            stringBuilder.append("Level:").append(" ").append(go.getPlayerProfile().getStats().getLevel());
            stringBuilder.append("\n");
            stringBuilder.append("Altitude:").append(" ").append(go.getAltitude());
            stringBuilder.append("\n");
            stringBuilder.append("Longitude:").append(" ").append(go.getLongitude());
            stringBuilder.append("\n");
            try {
                stringBuilder.append("Catchable Pokémons:").append(" ").append(go.getMap().getCatchablePokemon());
            } catch (LoginFailedException | RemoteServerException e) {
                e.printStackTrace();
            }
            stringBuilder.append("```");
            getMessageChannel().sendMessage(stringBuilder.toString());
        } else {
            switch (args[0]) {
                case "goto":
                    String s = buildStringFromArgs(1);
                    try {
                        System.out.println(s + " | " + getSamaritan().getGeoApiContext());
                        GeocodingResult result =  GeocodingApi.geocode(getSamaritan().getGeoApiContext(), s).await()[0];
                        double latitude = result.geometry.location.lat;
                        double longitude = result.geometry.location.lng;
                        go.setLatitude(latitude);
                        go.setLongitude(longitude);
                        getMessageChannel().sendMessage("Formatted Address: " + result.formattedAddress
                        + "\n" + go.getMap().getCatchablePokemon());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("c");
                    break;
            }
        }
    }
}
