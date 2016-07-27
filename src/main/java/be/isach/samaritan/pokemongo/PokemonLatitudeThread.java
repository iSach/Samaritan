package be.isach.samaritan.pokemongo;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.command.CommandPokeGo;
import com.google.maps.model.DirectionsStep;
import com.pokegoapi.api.PokemonGo;

import java.util.TimerTask;

/**
 * Package: be.isach.samaritan.pokemongo
 * Created by: sachalewin
 * Date: 27/07/16
 * Project: samaritan
 */
public class PokemonLatitudeThread extends TimerTask {

    private PokemonGo go;
    private boolean catchingPokemons = false;
    private CommandPokeGo commandPokeGo;
    private double latitude;

    public PokemonLatitudeThread(DirectionsStep[] steps, Samaritan samaritan, CommandPokeGo commandPokeGo) {
        this.commandPokeGo = commandPokeGo;
        this.go = samaritan.getPokemonGo();
        this.latitude = go.getLatitude();
    }

    @Override
    public void run() {
        try {
            go.setLatitude(latitude);
            commandPokeGo.catchPokemon(true);

            if (catchingPokemons) {
                commandPokeGo.catchPoke(go.getMap().getCatchablePokemon().get(0).getEncounterId());
            }

            catchingPokemons = !go.getMap().getCatchablePokemon().isEmpty();

            if (!catchingPokemons) {
                latitude -= 0.0008;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
