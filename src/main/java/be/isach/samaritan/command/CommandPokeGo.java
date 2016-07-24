package be.isach.samaritan.command;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;
import net.dv8tion.jda.entities.MessageChannel;
import okhttp3.OkHttpClient;

/**
 * Package: be.isach.samaritan.command
 * Created by: sachalewin
 * Date: 24/07/16
 * Project: samaritan
 */
public class CommandPokeGo extends Command {

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
            OkHttpClient httpClient = new OkHttpClient();
            RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo auth = new GoogleLogin(httpClient).login("token");
            PokemonGo go = null;
            go = new PokemonGo(auth, httpClient);
            getMessageChannel().sendMessage(go.getPlayerProfile().getUsername());
        } catch (LoginFailedException | RemoteServerException e) {
            e.printStackTrace();
        }
    }
}
