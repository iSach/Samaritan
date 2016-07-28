package be.isach.samaritan.pokemongo;

import POGOProtos.Inventory.Item.ItemIdOuterClass;
import be.isach.samaritan.Samaritan;
import be.isach.samaritan.command.CommandPokeGo;
import com.google.maps.model.DirectionsStep;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.*;

/**
 * Package: be.isach.samaritan.pokemongo
 * Created by: sachalewin
 * Date: 27/07/16
 * Project: samaritan
 */
public class PokemonLatitudeThread extends TimerTask {

    private static final int[] TO_AVOID = new int[]{
            19, 13, 16
    };

    private PokemonGo go;
    private boolean catchingPokemons = false, lootingPokestops = false;
    private CommandPokeGo commandPokeGo;
    private double latitude;

    public PokemonLatitudeThread(Samaritan samaritan, CommandPokeGo commandPokeGo) {
        this.commandPokeGo = commandPokeGo;
        this.go = samaritan.getPokemonGo();
        this.latitude = go.getLatitude();
    }

    @Override
    public void run() {
        if(go.getInventories().getPokebank().getPokemons().size() > 220
                || go.getInventories().getItemBag().getItem(ItemIdOuterClass.ItemId.ITEM_POKE_BALL).getCount() < 20) {
            cancel();
            return;
        }

        try {
            go.setLatitude(latitude);
            commandPokeGo.catchPokemon(true);

            if (catchingPokemons) {
                commandPokeGo.catchPoke(getCatchablePokemons().get(0).getEncounterId());
            }

            if(lootingPokestops) {
                commandPokeGo.lootStopsNearby();
            }

            catchingPokemons = !getCatchablePokemons().isEmpty();
            lootingPokestops = !getLootablePokestops().isEmpty();

            if (!catchingPokemons && !lootingPokestops) {
                latitude -= 0.0008;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<CatchablePokemon> getCatchablePokemons() {
        try {
            List<CatchablePokemon> catchablePokemons = go.getMap().getCatchablePokemon();
            for (Iterator<CatchablePokemon> iterator = catchablePokemons.iterator(); iterator.hasNext();) {
                CatchablePokemon catchablePokemon = iterator.next();
                if (Arrays.asList(TO_AVOID).contains(catchablePokemon.getPokemonId().getNumber())) {
                    iterator.remove();
                }
            }
            return catchablePokemons;
        } catch (LoginFailedException | RemoteServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Collection<Pokestop> getLootablePokestops() {
        try {
            Collection<Pokestop> catchablePokemons = go.getMap().getMapObjects().getPokestops();
            for (Iterator<Pokestop> iterator = catchablePokemons.iterator(); iterator.hasNext();) {
                Pokestop pokestop = iterator.next();
                if (!pokestop.inRange() || !pokestop.canLoot()) {
                    iterator.remove();
                }
            }
            return catchablePokemons;
        } catch (LoginFailedException | RemoteServerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
