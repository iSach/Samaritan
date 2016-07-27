package be.isach.samaritan.pokemongo;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.command.CommandPokeGo;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.TimerTask;

/**
 * Package: be.isach.samaritan.pokemongo
 * Created by: sachalewin
 * Date: 27/07/16
 * Project: samaritan
 */
public class TravelTask extends TimerTask {

    private DirectionsStep[] steps;
    private int currentStep;
    private Samaritan samaritan;
    private PokemonGo go;
    private boolean startLoc = true, catchingPokemons = false;
    private CommandPokeGo commandPokeGo;

    public TravelTask(DirectionsStep[] steps, Samaritan samaritan, CommandPokeGo commandPokeGo) {
        this.steps = steps;
        this.samaritan = samaritan;
        this.commandPokeGo = commandPokeGo;
        this.currentStep = 0;
        this.go = samaritan.getPokemonGo();
    }

    @Override
    public void run() {
        if (currentStep >= steps.length) {
            cancel();
            return;
        }


        try {
            DirectionsStep step = steps[currentStep];

            LatLng latLng = startLoc ? step.startLocation : step.endLocation;

            go.setLatitude(latLng.lat);
            go.setLongitude(latLng.lng);
            commandPokeGo.catchPokemon(true);

            if(catchingPokemons) {
                commandPokeGo.catchPoke(go.getMap().getCatchablePokemon().get(0).getEncounterId());
            }

            catchingPokemons = !go.getMap().getCatchablePokemon().isEmpty();

            if (!catchingPokemons) {
                if(!startLoc) {
                    currentStep++;
                }
                startLoc = !startLoc;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
