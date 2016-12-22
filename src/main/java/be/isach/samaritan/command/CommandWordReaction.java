package be.isach.samaritan.command;

import be.isach.samaritan.util.EmoteUnicodeUtil;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sacha on 18/12/16.
 */
public class CommandWordReaction extends Command {

    private static final Map<Character, String[]> CHAR_EMOTE_MAP = new HashMap<Character, String[]>() {
        {
            put('a', dankArray("regional_indicator_a", "a"));
            put('b', dankArray("regional_indicator_b", "b"));
            put('c', dankArray("regional_indicator_c"));
            put('d', dankArray("regional_indicator_d"));
            put('e', dankArray("regional_indicator_e"));
            put('f', dankArray("regional_indicator_f"));
            put('g', dankArray("regional_indicator_g"));
            put('h', dankArray("regional_indicator_h"));
            put('i', dankArray("regional_indicator_i", "information_source"));
            put('j', dankArray("regional_indicator_j"));
            put('k', dankArray("regional_indicator_k"));
            put('l', dankArray("regional_indicator_l"));
            put('m', dankArray("regional_indicator_m"));
            put('n', dankArray("regional_indicator_n"));
            put('o', dankArray("regional_indicator_o", "o2"));
            put('p', dankArray("regional_indicator_p", "parking"));
            put('q', dankArray("regional_indicator_q"));
            put('r', dankArray("regional_indicator_r"));
            put('s', dankArray("regional_indicator_s"));
            put('t', dankArray("regional_indicator_t"));
            put('u', dankArray("regional_indicator_u"));
            put('v', dankArray("regional_indicator_v"));
            put('w', dankArray("regional_indicator_w"));
            put('x', dankArray("regional_indicator_x"));
            put('y', dankArray("regional_indicator_y"));
            put('z', dankArray("regional_indicator_z"));
            put('0', dankArray("zero"));
            put('1', dankArray("one"));
            put('2', dankArray("two"));
            put('3', dankArray("three"));
            put('4', dankArray("four"));
            put('5', dankArray("five"));
            put('6', dankArray("six"));
            put('7', dankArray("seven"));
            put('8', dankArray("eight"));
            put('9', dankArray("nine"));
        }

        private String[] dankArray(String... s) {
            return s;
        }
    };

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandWordReaction(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        try {
            sleep(500);
            new MessageHistory(getMessageChannel()).retrievePast(1).queue(messages -> {
                String s = data.getMessage().getContent().replace(data.getMessage().getContent().split(" ")[0], "");
                for (char c : s.toCharArray()) {
                    try {
                        if (getEmote(c) != null) {
                            messages.get(0).addReaction(getEmote(c)).queue();
                            sleep(35);
                        }
                    } catch (InterruptedException e) {
                        return;
                        // xd
                    }
                }
            });
        } catch (Exception exc) {
            // xd
        }
    }

    private String getEmote(char character) {
        try {
            return EmoteUnicodeUtil.getUnicode(CHAR_EMOTE_MAP.get(character)[0]);
        } catch (Exception e) {
            return null;
        }
    }
}
