package be.isach.samaritan.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sacha on 18/12/16.
 */
public class EmoteUnicodeUtil {

    private static final Map<String, String> EMOTE_UNICODE_MAP = new HashMap<String, String>() {
        {
            put("regional_indicator_a", "\uD83C\uDDE6");
            put("a", "\uD83C\uDD70");
            put("regional_indicator_b", "\uD83C\uDDE7");
            put("b", "\uD83C\uDD71");
            put("regional_indicator_c", "\uD83C\uDDE8");
            put("regional_indicator_d", "\uD83C\uDDE9");
            put("regional_indicator_e", "\uD83C\uDDEA");
            put("regional_indicator_f", "\uD83C\uDDEB");
            put("regional_indicator_g", "\uD83C\uDDEC");
            put("regional_indicator_h", "\uD83C\uDDED");
            put("regional_indicator_i", "\uD83C\uDDEE");
            put("information_source", "\\u2139");
            put("regional_indicator_j", "\uD83C\uDDEF");
            put("regional_indicator_k", "\uD83C\uDDF0");
            put("regional_indicator_l", "\uD83C\uDDF1");
            put("regional_indicator_m", "\uD83C\uDDF2");
            put("m", "\u24C2");
            put("regional_indicator_n", "\uD83C\uDDF3");
            put("regional_indicator_o", "\uD83C\uDDF4");
            put("o2", "\uD83C\uDD7E");
            put("regional_indicator_p", "\uD83C\uDDF5");
            put("parking", "\uD83C\uDD7F");
            put("regional_indicator_q", "\uD83C\uDDF6");
            put("regional_indicator_r", "\uD83C\uDDF7");
            put("regional_indicator_s", "\uD83C\uDDF8");
            put("regional_indicator_t", "\uD83C\uDDF9");
            put("regional_indicator_u", "\uD83C\uDDFA");
            put("regional_indicator_v", "\uD83C\uDDFB");
            put("regional_indicator_w", "\uD83C\uDDFC");
            put("regional_indicator_x", "\uD83C\uDDFD");
            put("regional_indicator_y", "\uD83C\uDDFE");
            put("regional_indicator_z", "\uD83C\uDDFF");
            put("zero", "\u0030\u20E3");
            put("one", "\u0031\u20E3");
            put("two", "\u0032\u20E3");
            put("three", "\u0033\u20E3");
            put("four", "\u0034\u20E3");
            put("five", "\u0035\u20E3");
            put("six", "\u0036\u20E3");
            put("seven", "\u0037\u20E3");
            put("eight", "\u0038\u20E3");
            put("nine", "\u0039\u20E3");
            put("arrow_left", "\u25C0");
            put("arrow_right", "\u25B6");
            put("track_previous", "\u23EA");
            put("track_next", "\u23E9");
        }
    };

    public static String getUnicode(String emoteName) {
        return EMOTE_UNICODE_MAP.get(emoteName);
    }
}

