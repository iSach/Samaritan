package be.isach.samaritan.login;

import POGOProtos.Networking.Envelopes.RequestEnvelopeOuterClass;
import be.isach.samaritan.Samaritan;
import com.pokegoapi.auth.GoogleAuthJson;
import com.pokegoapi.auth.GoogleAuthTokenJson;
import com.pokegoapi.auth.GoogleLogin;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.util.Log;
import com.squareup.moshi.Moshi;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.User;
import okhttp3.*;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Package: be.isach.samaritan.login
 * Created by: sachalewin
 * Date: 26/07/16
 * Project: samaritan
 */
public class SamaritanLogin extends GoogleLogin {

    private OkHttpClient clientt;
    private Samaritan samaritan;

    public SamaritanLogin(OkHttpClient client, Samaritan samaritan) {
        super(client);
        clientt = client;
        this.samaritan = samaritan;
    }

    @Override
    public RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo login(String username, String password) throws LoginFailedException {
        try {
            User admin = samaritan.getJda().getUserById(samaritan.getOwnerId());
            PrivateChannel privateChannel = admin.getPrivateChannel();
            HttpUrl e = HttpUrl.parse("https://accounts.google.com/o/oauth2/device/code").newBuilder().addQueryParameter("client_id", "848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com").addQueryParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email").build();
            RequestBody reqBody = RequestBody.create( null, new byte[0]);
            Request request = (new okhttp3.Request.Builder()).url(e).method("POST", reqBody).build();
            Response response = clientt.newCall(request).execute();
            Moshi moshi = (new com.squareup.moshi.Moshi.Builder()).build();
            GoogleAuthJson googleAuth = (GoogleAuthJson) moshi.adapter(GoogleAuthJson.class).fromJson(response.body().string());
            privateChannel.sendMessage("Session Google expired.");
            privateChannel.sendMessage("Go to " + googleAuth.getVerificationUrl());
            privateChannel.sendMessage(googleAuth.getUserCode());
            GoogleAuthTokenJson token;
            while ((token = this.pol(googleAuth)) == null) {
                Thread.sleep((long) (googleAuth.getInterval() * 1000));
            }

            System.out.println("Got token: " + token.getIdToken());
            privateChannel.sendMessage("Got token: " + token.getIdToken());
            RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo.Builder authbuilder = RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo.newBuilder();
            authbuilder.setProvider("google");
            authbuilder.setToken(RequestEnvelopeOuterClass.RequestEnvelope.AuthInfo.JWT.newBuilder().setContents(token.getIdToken()).setUnknown2(59).build());
            return authbuilder.build();
        } catch (Exception var11) {
            throw new LoginFailedException();
        }
    }

    private GoogleAuthTokenJson pol(GoogleAuthJson json) throws URISyntaxException, IOException {
        HttpUrl url = HttpUrl.parse("https://www.googleapis.com/oauth2/v4/token").newBuilder().addQueryParameter("client_id", "848232511240-73ri3t7plvk96pj4f85uj8otdat2alem.apps.googleusercontent.com").addQueryParameter("client_secret", "NCjF1TLi2CcY6t5mt0ZveuL7").addQueryParameter("code", json.getDeviceCode()).addQueryParameter("grant_type", "http://oauth.net/grant_type/device/1.0").addQueryParameter("scope", "openid email https://www.googleapis.com/auth/userinfo.email").build();
        RequestBody reqBody = RequestBody.create( null, new byte[0]);
        Request request = (new okhttp3.Request.Builder()).url(url).method("POST", reqBody).build();
        Response response = clientt.newCall(request).execute();
        Moshi moshi = (new com.squareup.moshi.Moshi.Builder()).build();
        GoogleAuthTokenJson token = moshi.adapter(GoogleAuthTokenJson.class).fromJson(response.body().string());
        return token.getError() == null ? token : null;
    }
}
