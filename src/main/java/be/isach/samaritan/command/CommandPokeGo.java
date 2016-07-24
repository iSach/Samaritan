package be.isach.samaritan.command;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import be.isach.samaritan.pokemongo.LoginData;
import be.isach.samaritan.util.TextUtil;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import net.dv8tion.jda.entities.MessageChannel;
import okhttp3.OkHttpClient;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Package: be.isach.samaritan.command
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class CommandPokeGo extends Command {

    private static final DecimalFormat format = new DecimalFormat("##.##");

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
                        GeocodingResult result = GeocodingApi.geocode(getSamaritan().getGeoApiContext(), s).await()[0];
                        double lat = result.geometry.location.lat;
                        double lng = result.geometry.location.lng;
                        go.setLatitude(lat);
                        go.setLongitude(lng);
                        List<CatchablePokemon> catchablePokemons = go.getMap().getCatchablePokemon();
                        getMessageChannel().sendMessage("Okay, so we are at: " + result.formattedAddress);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (CatchablePokemon p : catchablePokemons) {
                            stringBuilder.append("  (" + getDistanceFromLatLonInKm(lat, lng, p.getLatitude(), p.getLongitude()) + "m) -> " + TextUtil.beautifyString(p.getPokemonId().name()) + "[ID:" + p.getPokemonId().getNumber() + "]");
                            stringBuilder.append("\n");
                        }
                        getMessageChannel().sendMessage("```Catchable Pokémons there:" + "\n" +  stringBuilder.toString() + "```");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "catch":
                    String locString = buildStringFromArgs(1);
                    try {
                        System.out.println(locString + " | " + getSamaritan().getGeoApiContext());
                        GeocodingResult result = GeocodingApi.geocode(getSamaritan().getGeoApiContext(), locString).await()[0];
                        double lat = result.geometry.location.lat;
                        double lng = result.geometry.location.lng;
                        go.setLatitude(lat);
                        go.setLongitude(lng);
                        List<CatchablePokemon> catchablePokemons = go.getMap().getCatchablePokemon();
                        getMessageChannel().sendMessage("Okay, so we are at: " + result.formattedAddress);
                        if(catchablePokemons.isEmpty()) {
                            getMessageChannel().sendMessage("No Pokémon there m8.");
                            return;
                        } else {
                            CatchablePokemon p = catchablePokemons.get(0);
                            getMessageChannel().sendMessage("Trying to catch: " + TextUtil.beautifyString(p.getPokemonId().name()));
                            CatchResult catchResult = p.catchPokemon();
                            getMessageChannel().sendMessage("Result: " + TextUtil.beautifyString(catchResult.toString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("subcommand not found.");
                    break;
            }
        }
    }
    private String getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        double  R = 6371;
        double  dLat = deg2rad(lat2-lat1);
        double  dLon = deg2rad(lon2-lon1);
        double  a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        double  c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double  d = R * c;
        return format.format(d * 1000);
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
}
