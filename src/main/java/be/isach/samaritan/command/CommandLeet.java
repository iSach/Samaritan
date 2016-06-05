package be.isach.samaritan.command;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.deploy.net.URLEncoder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import org.luaj.vm2.ast.Str;

import java.io.UnsupportedEncodingException;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 16:02
 */
public class CommandLeet extends Command {

    private final static String[] ENGLISH_ARRAY = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Z"};
    private final static String[] LEET_ARRAY = {"4", "8", "(", "[)", "3", "|=", "6", "#", "1", "_|", "X", "1", "|v|", "^/", "0", "|*", "(_,)", "2", "5", "7", "(_)", "\\/", "\\/\\/", "><", "7", "≥"};

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandLeet(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        getMessageChannel().sendTyping();
        String str = buildStringFromArgs();
        String leetMessage = "";
        for (int i = 0; i < str.length(); ++i)
            leetMessage += getLeetFromChar(str.charAt(i) + "");
        getMessageChannel().sendMessage(leetMessage);
    }

    private String getLeetFromChar(String s) {
        for (int i = 0; i < ENGLISH_ARRAY.length; i++) {
            if (s.equals(ENGLISH_ARRAY[i].toLowerCase())) return LEET_ARRAY[i];
        }
        return s;
    }
}
