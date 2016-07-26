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
import java.util.ArrayList;
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
        try {
            if (getSamaritan().getPokemonGo() == null) {
                getMessageChannel().sendMessage("Pokemon Go Session is invalid.");
                return;
            }

            go = getSamaritan().getPokemonGo();

            if (args.length == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("```");
                stringBuilder.append("Username:").append(" ").append(go.getPlayerProfile().getUsername());
                stringBuilder.append("\n");
                String team = "Aucune";
                switch (go.getPlayerProfile().getTeam()) {
                    case TEAM_MYSTIC:
                        team = "Sagesse (mystic)";
                        break;
                    case TEAM_INSTINCT:
                        team = "Intuition (instinct)";
                        break;
                    case TEAM_VALOR:
                        team = "Bravoure (valor)";
                        break;
                    case TEAM_NONE:
                        team = "Aucune";
                        break;
                }
                stringBuilder.append("Team:").append(" ").append(team);
                stringBuilder.append("\n");
                stringBuilder.append("Level:").append(" ").append(makeExpBar(go.getPlayerProfile()));
                stringBuilder.append("\n");
                stringBuilder.append("Altitude:").append(" ").append(go.getAltitude());
                stringBuilder.append("\n");
                stringBuilder.append("Longitude:").append(" ").append(go.getLongitude());
                stringBuilder.append("\n");
                stringBuilder.append("```");
                getMessageChannel().sendMessage(stringBuilder.toString());
                catchPokemon();
            } else {
                switch (args[0]) {
                    case "goto":
                        goTo();
                        break;
                    case "catch":
                        catchPokemon();
                        break;
                    case "bank":
                        int page = 1;
                        if (args.length > 1) {
                            try {
                                page = Integer.parseInt(args[1]);
                            } catch (Exception ignored) {

                            }
                        }
                        showPokeBank(page);
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
                    case "next":
                        goNextLoc();
                        break;
                    default:
                        getMessageChannel().sendMessage("subcommand not found.");
                        break;
                }
            }
        } catch (LoginFailedException exc) {
            getMessageChannel().sendMessage("Session expired. Generating a new one.");
            getSamaritan().connectToPokemonGo();
        }
    }

    private void goTo() throws LoginFailedException {
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

    private void catchPokemon() throws LoginFailedException {
        String encId = "";
        if (buildStringFromArgs(1).isEmpty()) {
            try {
                List<CatchablePokemon> pokemons = go.getMap().getCatchablePokemon();
                if (pokemons.isEmpty()) {
                    getMessageChannel().sendMessage("`No pokémons catchable nearby.`");
                    return;
                }
                pokemons.sort((o1, o2) -> o2.getPokemonId().getNumber() - o1.getPokemonId().getNumber());
                int totalScale = longestName() + 7;
                int totalScaleDesc = longestIdd() + 4;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("```");
                stringBuilder.append("Catchable Pokémons: \n\n\n");
                stringBuilder.append("Name").append(TextUtil.getSpaces(totalScale - "Name".length()));
                stringBuilder.append("ID").append(TextUtil.getSpaces(totalScaleDesc - "ID".length()));
                stringBuilder.append("Encounter ID");
                stringBuilder.append("\n\n");
                for (CatchablePokemon pokemon : pokemons) {
                    String encounterId = pokemon.getEncounterId() + "";
                    String id = pokemon.getPokemonId().getNumber() + TextUtil.getSpaces(totalScaleDesc - String.valueOf(pokemon.getPokemonId().getNumber()).length());
                    String name = NameRegistry.getFrenchName(pokemon.getPokemonId().name()) + TextUtil.getSpaces(totalScale - NameRegistry.getFrenchName(pokemon.getPokemonId().name()).length());
                    stringBuilder.append(name);
                    stringBuilder.append(id);
                    stringBuilder.append(encounterId);
                    stringBuilder.append("\n");
                }
                stringBuilder.append("```");
                getMessageChannel().sendMessage(stringBuilder.toString());
            } catch (LoginFailedException | RemoteServerException e) {
                e.printStackTrace();
            }
            return;
        } else {
            encId = buildStringFromArgs(1);
        }
        long encounterId = 0L;
        boolean found = false;
        try {
            encounterId = Long.valueOf(encId);
        } catch (Exception exc) {
            try {
                for (CatchablePokemon catchablePokemon : go.getMap().getCatchablePokemon()) {
                    if (NameRegistry.getFrenchName(catchablePokemon.getPokemonId().name()).equalsIgnoreCase(buildStringFromArgs(1))) {
                        encounterId = catchablePokemon.getEncounterId();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    getMessageChannel().sendMessage("No pokémon of that name is catchable.");
                    return;
                }
            } catch (RemoteServerException | LoginFailedException e) {
                getMessageChannel().sendMessage("Invalid Encounter Id.");
            }
            if (!found)
                getMessageChannel().sendMessage("Invalid Encounter Id.");
        }
        if (encounterId == 0L) {
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

    private void showPokeBank(int page) {
        if (page > getMaxPages()) page = getMaxPages();
        if (page < 1) page = 1;
        List<Pokemon> pokemonSs = go.getInventories().getPokebank().getPokemons();
        pokemonSs.sort((o1, o2) -> o2.getCp() - o1.getCp());
        List<Pokemon> pokemons = new ArrayList<>();
        int from = 1;
        if (page > 1)
            from = 30 * (page - 1) + 1;
        int to = 30 * page;
        for (int h = from; h <= to; h++) {
            if (h > pokemonSs.size())
                break;
            pokemons.add(pokemonSs.get(h - 1));
        }
        pokemons.sort((o1, o2) -> o2.getCp() - o1.getCp());
        int totalScale = longestName() + 7;
        int totalScaleDesc = longestCp() + 6;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("```");
        stringBuilder.append(" \nPoké Bank (Page " + page + "/" + getMaxPages() + "): \n\n");
        stringBuilder.append("Name").append(TextUtil.getSpaces(totalScale - "Name".length()));
        stringBuilder.append("CP").append(TextUtil.getSpaces(totalScaleDesc - "CP".length()));
        stringBuilder.append("ID");
        stringBuilder.append("\n\n");
        for (Pokemon pokemon : pokemons) {
            String id = pokemon.getPokemonId().getNumber() + "";
            String cp = pokemon.getCp() + TextUtil.getSpaces(totalScaleDesc - String.valueOf(pokemon.getCp()).length());
            String name = NameRegistry.getFrenchName(pokemon) + TextUtil.getSpaces(totalScale - NameRegistry.getFrenchName(pokemon).length());
            stringBuilder.append(name);
            stringBuilder.append(cp);
            stringBuilder.append(id);
            stringBuilder.append("\n");
        }
        stringBuilder.append("```");
        getMessageChannel().sendMessage(stringBuilder.toString());
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
                if (pokestop.canLoot() && pokestop.inRange())
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

    private void goNextLoc() {

        double lat = go.getLatitude();
        double lng = go.getLongitude() - 0.0008;
        GeocodingResult result = null;
        try {
            result = GeocodingApi.geocode(getSamaritan().getGeoApiContext(), lat + ", " + lng).await()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (!result.formattedAddress.contains("Belgi")) {
//            getMessageChannel().sendMessage("Not in Belgium.");
//            return;
//        }
        go.setLatitude(lat);
        go.setLongitude(lng);

        getMessageChannel().sendMessage("Okay, so we are at: " + result.formattedAddress);
        try {
            catchPokemon();
        } catch (LoginFailedException e) {
            getMessageChannel().sendMessage("Session expired. Generating a new one.");
            getSamaritan().connectToPokemonGo();
        }
    }

    private String formatCatchResult(CatchResult catchResult) {
        StringBuilder stringBuilder = new StringBuilder();

        String trhow = "", status = "";

        if (catchResult.getActivityTypeList().size() > 2)
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
        for (int xp : catchResult.getXpList()) {
            totalExp += xp;
        }
        for (int candy : catchResult.getCandyList()) {
            candies += candy;
        }
        for (int stardust : catchResult.getStardustList()) {
            stardusts += stardust;
        }

        stringBuilder.append("```");
        stringBuilder.append("Result:").append("\n");
        stringBuilder.append("  Status: ").append(status).append("\n");
        if (catchResult.getActivityTypeList().size() > 2)
            stringBuilder.append("  Activity: ").append(trhow).append("\n");
        stringBuilder.append("  Total Exp Dropped: ").append(totalExp).append("\n");
        stringBuilder.append("  Candies dropped: ").append(candies).append("\n");
        stringBuilder.append("  Stardust dropped: ").append(stardusts).append("\n");
        if (catchResult.getStatus() != CatchPokemonResponseOuterClass.CatchPokemonResponse.CatchStatus.CATCH_SUCCESS) {
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

    private int getMaxPages() {
        int max = 30;
        List<Pokemon> commands = go.getInventories().getPokebank().getPokemons();
        int i = commands.size();
        if (i % max == 0) return Math.max(1, i / max);
        double j = i / max;
        int h = (int) Math.floor(j * 100) / 100;
        return Math.max(1, h + 1);
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
        stringBuilder.append(format.format(ratio * 100d));
        stringBuilder.append("% | ");
        stringBuilder.append(((int) min) + "/" + ((int) max) + ")");
        return stringBuilder.toString();
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public int longestIdd() {
        int longest = 0;
        try {
            for (CatchablePokemon pokemon : go.getMap().getCatchablePokemon())
                longest = Math.max(longest, String.valueOf(pokemon.getPokemonId().getNumber()).length());
        } catch (LoginFailedException | RemoteServerException e) {
            e.printStackTrace();
        }
        return longest;
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public int longestNameE() {
        int longest = 0;
        try {
            for (CatchablePokemon pokemon : go.getMap().getCatchablePokemon())
                longest = Math.max(longest, NameRegistry.getFrenchName(pokemon.getPokemonId().name()).length());
        } catch (LoginFailedException | RemoteServerException e) {
            e.printStackTrace();
        }
        return longest;
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public int longestCp() {
        int longest = 0;
        for (Pokemon pokemon : go.getInventories().getPokebank().getPokemons())
            longest = Math.max(longest, String.valueOf(pokemon.getCp()).length());
        return longest;
    }

    /**
     * @return The length of the longest Command complete alias.
     */
    public int longestName() {
        int longest = 0;
        for (Pokemon pokemon : go.getInventories().getPokebank().getPokemons())
            longest = Math.max(longest, NameRegistry.getFrenchName(pokemon.getPokemonId().name()).length());
        return longest;
    }
}
