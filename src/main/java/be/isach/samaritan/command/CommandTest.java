package be.isach.samaritan.command;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.options.Option;
import com.mashape.unirest.http.options.Options;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by sacha on 22/12/16.
 */
public class CommandTest extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandTest(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {

    }
}
