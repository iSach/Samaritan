package be.isach.samaritan.command;

import POGOProtos.Inventory.Item.ItemAwardOuterClass;
import POGOProtos.Networking.Responses.CatchPokemonResponseOuterClass;
import be.isach.samaritan.pokemongo.NameRegistry;
import be.isach.samaritan.util.TextUtil;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.inventory.Item;
import com.pokegoapi.api.inventory.Pokeball;
import com.pokegoapi.api.map.fort.Pokestop;
import com.pokegoapi.api.map.fort.PokestopLootResult;
import com.pokegoapi.api.map.pokemon.CatchResult;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.player.PlayerProfile;
import com.pokegoapi.api.pokemon.Pokemon;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import net.dv8tion.jda.entities.MessageChannel;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

/**
 * Package: be.isach.samaritan.command
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class CommandPokeGo extends Command {

    private static final DecimalFormat format = new DecimalFormat("##.##");

    private PokemonGo go;

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

        go = getSamaritan().getPokemonGo();

        if (args.length == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```");
            stringBuilder.append("Username:").append(" ").append(go.getPlayerProfile().getUsername());
            stringBuilder.append("\n");
            stringBuilder.append("Team:").append(" ").append(go.getPlayerProfile().getTeam());
            stringBuilder.append("\n");
            stringBuilder.append("Level:").append(" ").append(makeExpBar(go.getPlayerProfile()));
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
                    goTo();
                    break;
                case "catch":
                    catchPokemon();
                    break;
                case "bank":
                    showPokeBank();
                    break;
                case "inv":
                    showPokeInv();
                    break;
                case "stoplist":
                    showStopsNearby();
                    break;
                case "stop":
                    lootStopsNearby();
                    break;
                default:
                    getMessageChannel().sendMessage("subcommand not found.");
                    break;
            }
        }
    }

    private void goTo() {
        String loc = "Liege, Belgium";
        if (buildStringFromArgs(1).isEmpty()) {
            getMessageChannel().sendMessage("Where to go?");
            loc = nextMessage().getContent();
        } else {
            loc = buildStringFromArgs(1);
        }
        try {
            GeocodingResult result = GeocodingApi.geocode(getSamaritan().getGeoApiContext(), loc).await()[0];
            if (!result.formattedAddress.contains("Belgi")) {
                getMessageChannel().sendMessage("Not in Belgium.");
                return;
            }

            double lat = result.geometry.location.lat;
            double lng = result.geometry.location.lng;
            go.setLatitude(lat);
            go.setLongitude(lng);

            List<CatchablePokemon> catchablePokemons = go.getMap().getCatchablePokemon();
            getMessageChannel().sendMessage("Okay, so we are at: " + result.formattedAddress);

            StringBuilder stringBuilder = new StringBuilder();

            for (CatchablePokemon p : catchablePokemons) {
                stringBuilder.append("  (" + distance(lat,
                        lng, p.getLatitude(), p.getLongitude()) + "m) -> " +
                        NameRegistry.getFrenchName(p.getPokemonId().name()) + "" +
                        " [ID:" + p.getPokemonId().getNumber() + " | " + p.getEncounterId() + "]");
                stringBuilder.append("\n");
            }
            getMessageChannel().sendMessage("```Catchable Pokémons there:" + "\n" + stringBuilder.toString() + "```");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void catchPokemon() {
        String encId = "";
        if (buildStringFromArgs(1).isEmpty()) {
            getMessageChannel().sendMessage("What is its Encounter Id?");
            encId = nextMessage().getContent();
        } else {
            encId = buildStringFromArgs(1);
        }
        long encounterId = 0L;
        try {
            encounterId = Long.valueOf(encId);
        } catch (Exception exc) {
            getMessageChannel().sendMessage("Invalid Encounter Id.");
            return;
        }
        try {
            List<CatchablePokemon> catchablePokemons = go.getMap().getCatchablePokemon();
            for (CatchablePokemon catchablePokemon : catchablePokemons) {
                if (catchablePokemon.getEncounterId() == encounterId) {
                    getMessageChannel().sendMessage("Attempting to catch: " + NameRegistry.getFrenchName(catchablePokemon.getPokemonId().name()) + " | " + encounterId);
                    catchablePokemon.encounterPokemon();
                    CatchResult catchResult = catchablePokemon.catchPokemon(Pokeball.POKEBALL, 5, 0);
                    getMessageChannel().sendMessage(formatCatchResult(catchResult));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPokeBank() {
        List<Pokemon> pokemons = go.getInventories().getPokebank().getPokemons();
        StringBuilder stringBuilder = new StringBuilder();
        for (Pokemon p : pokemons) {
            try {
                stringBuilder.append("  ").append(p.getCp()).append("pc ").append((NameRegistry.getFrenchName(p.getPokemonId().name()) + "" +
                        " [ID:" + p.getPokemonId().getNumber() + "]"));
                stringBuilder.append("\n");
            } catch (Exception exc) {
                continue;
            }
        }
        getMessageChannel().sendMessage("```PokéBank:\n" + stringBuilder.toString() + "```");
    }

    private void showPokeInv() {
        Collection<Item> items = go.getInventories().getItemBag().getItems();
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append("  ");
            sb.append(item.getCount());
            sb.append("x ");
            sb.append(TextUtil.beautifyString(item.getItemId().name()));
            sb.append("\n");
        }
        getMessageChannel().sendMessage("```Items:\n" + sb.toString() + "```");
    }

    private void showStopsNearby() {
        try {
            if (go.getMap().getMapObjects().getPokestops().isEmpty()) {
                getMessageChannel().sendMessage("No stops.");
                return;
            }
            StringBuilder sbbb = new StringBuilder();
            for (Pokestop pokestop : go.getMap().getMapObjects().getPokestops()) {
                if(pokestop.canLoot() && pokestop.inRange())
                sbbb.append(pokestop.getDetails().getName() + "\n");
            }
            getMessageChannel().sendMessage("stops:");
            getMessageChannel().sendMessage(sbbb.toString());
        } catch (Exception exc) {
        }
    }

    private void lootStopsNearby() {
        try {
            StringBuilder sbb = new StringBuilder();
            sbb.append("```Pokéstops looted:" + "\n");
            for (Pokestop pokestop : go.getMap().getMapObjects().getPokestops()) {
                if (pokestop.canLoot()) {
                    PokestopLootResult result = pokestop.loot();
                    if (!result.wasSuccessful()) continue;
                    sbb.append("  ").append(pokestop.getDetails().getName()).append(":").append("\n");
                    sbb.append("    EXP dropped: ").append(result.getExperience()).append("\n");
                    sbb.append("    Items: ").append("\n");
                    for (ItemAwardOuterClass.ItemAward item : result.getItemsAwarded()) {
                        sbb.append("      ").append(item.getItemCount()).append("x ").append(TextUtil.beautifyString(item.getItemId().name()) + "\n");
                    }
                }
            }
            sbb.append("```");
            getMessageChannel().sendMessage(sbb.toString());
        } catch (LoginFailedException | RemoteServerException e) {
            e.printStackTrace();
        }
    }

    private String formatCatchResult(CatchResult catchResult) {
        StringBuilder stringBuilder = new StringBuilder();

        String trhow = "", status = "";

        switch (catchResult.getActivityTypeList().get(1)) {
            default:
                trhow = TextUtil.beautifyString(catchResult.getActivityTypeList().get(1).name());
                break;
            case ACTIVITY_CATCH_EXCELLENT_THROW:
                trhow = "Tir excellent !";
                break;
            case ACTIVITY_CATCH_FIRST_THROW:
                trhow = "Premier Tir !";
                break;
            case ACTIVITY_CATCH_GREAT_THROW:
                trhow = "Super tir !";
                break;
            case ACTIVITY_CATCH_NICE_THROW:
                trhow = "Bon tir !";
                break;
        }
        switch (catchResult.getStatus()) {
            case CATCH_ERROR:
                status = "Erreur";
                break;
            case CATCH_ESCAPE:
                status = "Le Pokémon s'est echappé.";
                break;
            case CATCH_FLEE:
                status = "Pokémon échappé. (Flee: Softban Possible).";
                break;
            case CATCH_MISSED:
                status = "Tir raté.";
                break;
            case CATCH_SUCCESS:
                status = "Pokémon capturé !";
                break;
        }
        int totalExp = 0, candies = 0, stardusts = 0;
        for(int xp : catchResult.getXpList()) {
            totalExp += xp;
        }
        for(int candy : catchResult.getCandyList()) {
            candies += candy;
        }
        for(int stardust : catchResult.getStardustList()) {
            stardusts += stardust;
        }

        stringBuilder.append("```");
        stringBuilder.append("Result:").append("\n");
        stringBuilder.append("  Status: ").append(status).append("\n");
        stringBuilder.append("  Throw: ").append(trhow).append("\n");
        stringBuilder.append("  Total Exp Dropped: ").append(totalExp).append("\n");
        stringBuilder.append("  Candies dropped: ").append(candies).append("\n");
        stringBuilder.append("  Stardust dropped: ").append(stardusts).append("\n");
        if(catchResult.getStatus() != CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus.CATCH_SUCCESS) {
            stringBuilder.append("  Miss Percent: ").append(catchResult.getMissPercent()).append("\n");
        }
        stringBuilder.append("```");

        return stringBuilder.toString();
    }

    private String distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return format.format(d * 1000);
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    private String makeExpBar(PlayerProfile playerProfile) {
        int lvl = playerProfile.getStats().getLevel();
        double min = ((double) playerProfile.getStats().getExperience()) - playerProfile.getStats().getPrevLevelXp() - ((lvl - 1) * 1000);
        double max = (double) lvl * 1000;
        double ratio = min / max;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(playerProfile.getStats().getLevel());
        stringBuilder.append(" ");
        for (int i = 1; i <= 15; i++) stringBuilder.append((ratio * 15d) >= i ? "■" : "□");
        stringBuilder.append(" ");
        stringBuilder.append(playerProfile.getStats().getLevel() + 1);
        stringBuilder.append("   (");
        stringBuilder.append((ratio * 100d));
        stringBuilder.append("% | ");
        stringBuilder.append(((int) min) + "/" + ((int) max) + ")");
        return stringBuilder.toString();
    }
}
