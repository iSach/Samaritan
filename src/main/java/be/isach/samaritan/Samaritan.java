package be.isach.samaritan;

import be.isach.samaritan.chat.PrivateMessageChatThread;
import be.isach.samaritan.command.console.ConsoleListenerThread;
import be.isach.samaritan.listener.CommandListener;
import be.isach.samaritan.listener.PrivateMessageListener;
import be.isach.samaritan.listener.QuoteListener;
import be.isach.samaritan.log.SmartLogger;
import be.isach.samaritan.music.SongPlayer;
import be.isach.samaritan.runtime.ShutdownThread;
import be.isach.samaritan.util.GifFactory;
import be.isach.samaritan.util.SamaritanStatus;
import be.isach.samaritan.websocket.SamaritanWebsocketServer;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.PrivateChannel;
import org.joda.time.Instant;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Project: samaritan
 * Package: be.isach.samaritan
 * Created by: Sacha
 * Created on: 15th May, 2016
 * Description: Represents a Samaritan Instance.
 */
public class Samaritan {

    /**
     * Song Players map with their corresponding guilds.
     */
    private Map<Guild, SongPlayer> songPlayers;

    /**
     * Samaritan Status.
     */
    private SamaritanStatus status;

    /**
     * The Jda of this Samaritan Instance.
     */
    private JDA jda;

    /**
     * The Smart Logger of this Samaritan Instance.
     */
    private SmartLogger logger;

    /**
     * Absolute File!
     */
    private File workingDirectory;

    /**
     * UI WebSocket Server.
     */
    private SamaritanWebsocketServer samaritanWebsocketServer;

    /**
     * Private Message Listener.
     */
    private PrivateMessageListener pmListener;

    /**
     * Quote Listener.
     */
    private QuoteListener quoteListener;

    /**
     * Bot Token.
     */
    private String botToken;

    /**
     * Admin, TEMPORARY.
     * TODO: Permissions with commands and saving.
     */
    public String admin;

    /**
     * WEB Interface using WebSockets?
     */
    private boolean webUi;

    /**
     * Web Interface WebSocket Server Port.
     */
    private int uiWebSocketPort;

    /**
     * Gif Factory.
     */
    private GifFactory gifFactory;

    /**
     * Samaritan Constructor.
     *
     * @param args            Program Arguments.
     *                        Given when program is started
     * @param botToken        Bot Token.
     *                        From samaritan.properties
     * @param webUi           Use Web UI or not.
     *                        From samaritan.properties.
     * @param uiWebSocketPort Web UI Port.
     *                        From <samaritan.properties.
     */
    public Samaritan(String[] args, String botToken, boolean webUi, int uiWebSocketPort, String admin, File workingDirectory) {
        this.botToken = botToken;
        this.logger = new SmartLogger();
        this.status = new SamaritanStatus();
        this.songPlayers = new HashMap<>();
        this.gifFactory = new GifFactory();
        this.admin = admin;
        this.workingDirectory = workingDirectory;

        status.setBootInstant(new Instant());

        logger.write("--------------------------------------------------------");
        logger.write();
        logger.write("Hello.");
        logger.write();
        logger.write("I am Samaritan.");
        logger.write();
        logger.write("Starting...");
        logger.write();
        logger.write("Boot Instant: " + new Instant().toString());
        logger.write();

        if (!initJda()) {
            logger.write("Invalid token! Please change it in samaritan.properties");
            System.exit(1);
            return;
        }

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));

        startSongPlayers();

        setUpListeners();

        if (webUi) startWebSocketServer();

        new ConsoleListenerThread(this).start();
    }

    /**
     * Starts JDA.
     *
     * @return {@code true} if everything went well, {@code false} otherwise.
     */
    private boolean initJda() {
        try {
            jda = new JDABuilder().setBotToken(botToken).buildBlocking();
            jda.getAccountManager().setGame("Beta 2.0");
            jda.getAccountManager().setUsername("Samaritan");
            jda.getAccountManager().update();
        } catch (LoginException | InterruptedException e) {
            logger.write("Couldn't connect!");
            return false;
        }
        return true;
    }

    /**
     * Shuts Samaritan down.
     *
     * @param exitSystem If true, will execute a 'System.exit(0);'
     */
    public final void shutdown(boolean exitSystem) {
        for (PrivateMessageChatThread chatThread : getPrivateMessageListener().getChatThreads().values()) {
            PrivateChannel privateChannel = (PrivateChannel) chatThread.getMessageChannel();
            privateChannel.sendMessage("I must go, a reboot is in the queue!\nYou can try speaking to me again in a few moments.\nGood bye, my dear " + privateChannel.getUser().getUsername() + ".");
        }
        jda.getAccountManager().reset();
        jda.getAccountManager().update();
        jda.shutdown();
        if (exitSystem)
            System.exit(0);
    }

    /**
     * Starts WebSocket Server (for UI).
     */
    private void startWebSocketServer() {
        try {
            samaritanWebsocketServer = new SamaritanWebsocketServer(new InetSocketAddress(11350));
            samaritanWebsocketServer.start();
            System.out.println("WS Server started.");
        } catch (UnknownHostException e) {
            System.out.println("WS Server couldn't start.");
            e.printStackTrace();
        }
    }

    /**
     * Set up Listeners.
     */
    private void setUpListeners() {
        this.pmListener = new PrivateMessageListener();
        this.quoteListener = new QuoteListener(this);

        jda.addEventListener(new CommandListener(this));
        jda.addEventListener(pmListener);
        jda.addEventListener(quoteListener);
    }

    /**
     * Start Song Players.
     */
    private void startSongPlayers() {
        for (Guild guild : jda.getGuilds()) {
            SongPlayer songPlayer = new SongPlayer(guild, this);
            songPlayers.put(guild, songPlayer);
        }
    }

    /**
     * @param guild The Guild.
     * @return A song Player by Guild.
     */
    public SongPlayer getSongPlayer(Guild guild) {
        return songPlayers.get(guild);
    }

    /**
     * @return The SongPlayers map.
     */
    public Map<Guild, SongPlayer> getSongPlayers() {
        return songPlayers;
    }

    /**
     * @return The UI WebSocket Server.
     */
    public SamaritanWebsocketServer getWebSocketServer() {
        return samaritanWebsocketServer;
    }

    /**
     * @return The JDA of this Samaritan Instance.
     */
    public JDA getJda() {
        return jda;
    }

    /**
     * @return The Smart Logger.
     */
    public SmartLogger getLogger() {
        return logger;
    }

    /**
     * @return The Private Message Listener
     */
    public PrivateMessageListener getPrivateMessageListener() {
        return pmListener;
    }

    /**
     * @return The Private Message Listener
     */
    public QuoteListener getQuoteListener() {
        return quoteListener;
    }

    /**
     * @return Samaritan's status.
     */
    public SamaritanStatus getStatus() {
        return status;
    }

    /**
     * @return The Gif Factory.
     */
    public GifFactory getGifFactory() {
        return gifFactory;
    }

    /**
     * @return webUi value.
     */
    public boolean useWebUi() {
        return webUi;
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }
}