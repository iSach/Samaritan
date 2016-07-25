package be.isach.samaritan.pokemongo;

import be.isach.samaritan.util.TextUtil;
import com.pokegoapi.api.pokemon.Pokemon;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: be.isach.samaritan.pokemongo
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class NameRegistry {

    /**
     * key: English Name.
     * value: French Name.
     */
    private static Map<String, String> NAMES = new HashMap<>();

    static {
        NAMES.put("bulbasaur", "bulbizarre");
        NAMES.put("ivysaur", "herbizarre");
        NAMES.put("venusaur", "florizarre");
        NAMES.put("charmander", "salamèche");
        NAMES.put("charmeleon", "reptincel");
        NAMES.put("charizard", "dracaufeu");
        NAMES.put("squirtle", "carapuce");
        NAMES.put("wartortle", "carabaffe");
        NAMES.put("blastoise", "tortank");
        NAMES.put("caterpie", "chenipan");
        NAMES.put("metapod", "chrysacier");
        NAMES.put("butterfree", "papilusion");
        NAMES.put("weedle", "aspicot");
        NAMES.put("kakuna", "coconfort");
        NAMES.put("beedrill", "dardargnan");
        NAMES.put("pidgey", "roucool");
        NAMES.put("pidgeotto", "roucoups");
        NAMES.put("pidgeot", "roucarnage");
        NAMES.put("rattata", "rattata");
        NAMES.put("raticate", "rattatac");
        NAMES.put("spearow", "piafabec");
        NAMES.put("fearow", "rapasdepic");
        NAMES.put("ekans", "abo");
        NAMES.put("arbok", "arbok");
        NAMES.put("pikachu", "pikachu");
        NAMES.put("raichu", "raichu");
        NAMES.put("sandshrew", "sabelette");
        NAMES.put("sandslash", "sablaireau");
        NAMES.put("nidoran", "nidoran");
        NAMES.put("nidorina", "nidorina");
        NAMES.put("nidoqueen", "nidoqueen");
        NAMES.put("nidoran", "nidoran");
        NAMES.put("nidorino", "nidorino");
        NAMES.put("nidoking", "nidoking");
        NAMES.put("clefairy", "mélofée");
        NAMES.put("clefable", "mélodelfe");
        NAMES.put("vulpix", "goupix");
        NAMES.put("ninetales", "feunard");
        NAMES.put("jigglypuff", "rondoudou");
        NAMES.put("wigglytuff", "grodoudou");
        NAMES.put("zubat", "nosferapti");
        NAMES.put("golbat", "nosferalto");
        NAMES.put("oddish", "mystherbe");
        NAMES.put("gloom", "ortide");
        NAMES.put("vileplume", "rafflesia");
        NAMES.put("paras", "paras");
        NAMES.put("parasect", "parasect");
        NAMES.put("venonat", "mimitoss");
        NAMES.put("venomoth", "aéromite");
        NAMES.put("diglett", "taupiqueur");
        NAMES.put("dugtrio", "triopikeur");
        NAMES.put("meowth", "miaouss");
        NAMES.put("persian", "persian");
        NAMES.put("psyduck", "psykokwak");
        NAMES.put("golduck", "akwakwak");
        NAMES.put("mankey", "férosinge");
        NAMES.put("primeape", "colossinge");
        NAMES.put("growlithe", "caninos");
        NAMES.put("arcanine", "arcanin");
        NAMES.put("poliwag", "ptitard");
        NAMES.put("poliwhirl", "têtarte");
        NAMES.put("poliwrath", "tartard");
        NAMES.put("abra", "abra");
        NAMES.put("kadabra", "kadabra");
        NAMES.put("alakazam", "alakazam");
        NAMES.put("machop", "machoc");
        NAMES.put("machoke", "machopeur");
        NAMES.put("machamp", "mackogneur");
        NAMES.put("bellsprout", "chétiflor");
        NAMES.put("weepinbell", "boustiflor");
        NAMES.put("victreebel", "empiflor");
        NAMES.put("tentacool", "tentacool");
        NAMES.put("tentacruel", "tentacruel");
        NAMES.put("geodude", "racaillou");
        NAMES.put("graveler", "gravalanch");
        NAMES.put("golem", "grolem");
        NAMES.put("ponyta", "ponyta");
        NAMES.put("rapidash", "galopa");
        NAMES.put("slowpoke", "ramoloss");
        NAMES.put("slowbro", "flagadoss");
        NAMES.put("magnemite", "magnéti");
        NAMES.put("magneton", "magnéton");
        NAMES.put("farfetch'd", "canarticho");
        NAMES.put("doduo", "doduo");
        NAMES.put("dodrio", "dodrio");
        NAMES.put("seel", "otaria");
        NAMES.put("dewgong", "lamantine");
        NAMES.put("grimer", "tadmorv");
        NAMES.put("muk", "grotadmorv");
        NAMES.put("shellder", "kokiyas");
        NAMES.put("cloyster", "crustabri");
        NAMES.put("gastly", "fantominus");
        NAMES.put("haunter", "spectrum");
        NAMES.put("gengar", "ectoplasma");
        NAMES.put("onix", "onix");
        NAMES.put("drowzee", "soporifik");
        NAMES.put("hypno", "hypnomade");
        NAMES.put("krabby", "krabby");
        NAMES.put("kingler", "krabboss");
        NAMES.put("voltorb", "voltorbe");
        NAMES.put("electrode", "électrode");
        NAMES.put("exeggcute", "nœunœuf");
        NAMES.put("exeggutor", "noadkoko");
        NAMES.put("cubone", "osselait");
        NAMES.put("marowak", "ossatueur");
        NAMES.put("hitmonlee", "kicklee");
        NAMES.put("hitmonchan", "tygnon");
        NAMES.put("lickitung", "excelangue");
        NAMES.put("koffing", "smogo");
        NAMES.put("weezing", "smogogo");
        NAMES.put("rhyhorn", "rhinocorne");
        NAMES.put("rhydon", "rhinoféros");
        NAMES.put("chansey", "leveinard");
        NAMES.put("tangela", "saquedeneu");
        NAMES.put("kangaskhan", "kangourex");
        NAMES.put("horsea", "hypotrempe");
        NAMES.put("seadra", "hypocéan");
        NAMES.put("goldeen", "poissirène");
        NAMES.put("seaking", "poissoroy");
        NAMES.put("staryu", "stari");
        NAMES.put("starmie", "staross");
        NAMES.put("mr. mime", "m.mime");
        NAMES.put("scyther", "insécateur");
        NAMES.put("jynx", "lippoutou");
        NAMES.put("electabuzz", "élektek");
        NAMES.put("magmar", "magmar");
        NAMES.put("pinsir", "scarabrute");
        NAMES.put("tauros", "tauros");
        NAMES.put("magikarp", "magicarpe");
        NAMES.put("gyarados", "léviator");
        NAMES.put("lapras", "lokhlass");
        NAMES.put("ditto", "métamorph");
        NAMES.put("eevee", "évoli");
        NAMES.put("vaporeon", "aquali");
        NAMES.put("jolteon", "voltali");
        NAMES.put("flareon", "pyroli");
        NAMES.put("porygon", "porygon");
        NAMES.put("omanyte", "amonita");
        NAMES.put("omastar", "amonistar");
        NAMES.put("kabuto", "kabuto");
        NAMES.put("kabutops", "kabutops");
        NAMES.put("aerodactyl", "ptéra");
        NAMES.put("snorlax", "ronflex");
        NAMES.put("articuno", "artikodin");
        NAMES.put("zapdos", "électhor");
        NAMES.put("moltres", "sulfura");
        NAMES.put("dratini", "minidraco");
        NAMES.put("dragonair", "draco");
        NAMES.put("dragonite", "dracolosse");
        NAMES.put("mewtwo", "mewtwo");
        NAMES.put("mew", "mew");
    }

    public static String getFrenchName(Pokemon pokemon) {
        return getFrenchName(pokemon.getPokemonId().name());
    }

    public static String getFrenchName(String englishName) {
        if(englishName.contains("nidoran")) return englishName;
        try {
            englishName = englishName.toLowerCase();
            return TextUtil.beautifyString(NAMES.get(englishName));
        } catch (Exception exc) {
            return englishName;
        }
    }

}
