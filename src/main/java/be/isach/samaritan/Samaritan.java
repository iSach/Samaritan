package be.isach.samaritan;

import be.isach.samaritan.util.brainfuck.BrainfuckInterpreter;
import be.isach.samaritan.chat.PrivateMessageChatThread;
import be.isach.samaritan.command.console.ConsoleListenerThread;
import be.isach.samaritan.level.AccessLevelManager;
import be.isach.samaritan.listener.CleverBotListener;
import be.isach.samaritan.listener.CommandListener;
import be.isach.samaritan.listener.PrivateMessageListener;
import be.isach.samaritan.listener.QuoteHandler;
import be.isach.samaritan.log.SmartLogger;
import be.isach.samaritan.runtime.ShutdownThread;
import be.isach.samaritan.stream.BeamModule;
import be.isach.samaritan.stream.StreamData;
import be.isach.samaritan.stream.TwitchData;
import be.isach.samaritan.stream.TwitchModule;
import be.isach.samaritan.util.GifFactory;
import be.isach.samaritan.util.SamaritanStatus;
import be.isach.samaritan.websocket.SamaritanWebsocketServer;
import com.mashape.unirest.http.Unirest;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.WebSocketClient;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.joda.time.Instant;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Timer;

/**
 * Project: samaritan
 * Package: be.isach.samaritan
 * Created by: Sacha
 * Created on: 15th May, 2016
 * <p>
 * Description: Represents a Samaritan Instance.
 */
public class Samaritan {

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
     * Brainfuck Code Interpreter
     */
    private BrainfuckInterpreter brainfuckInterpreter;

    /**
     * Stream Module.
     */
    private TwitchModule twitchModule;

    /**
     * Stream Module.
     */
    private BeamModule beamModule;

    /**
     * UI WebSocket Server.
     */
    private SamaritanWebsocketServer samaritanWebsocketServer;

    /**
     * Private Message Listener.
     */
    private PrivateMessageListener pmListener;

    /**
     * Private Message Listener.
     */
    private CleverBotListener cleverBotListener;

    /**
     * Bot Token.
     */
    private String botToken;

    /**
     * Main Admin. Owner. As Discord User ID.
     */
    private String ownerId;

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
     * Timer.
     */
    private Timer timer;

    /**
     * Users Acess Level Manager.
     */
    private AccessLevelManager accessLevelManager;

    /**
     * Manages quotes.
     */
    private QuoteHandler quoteHandler;

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
    public Samaritan(String[] args, String botToken, boolean webUi, int uiWebSocketPort, long ownerId, File workingDirectory) {
        this.botToken = botToken;
        this.logger = new SmartLogger();
        this.status = new SamaritanStatus();
        this.gifFactory = new GifFactory();
        this.ownerId = String.valueOf(ownerId);
        this.workingDirectory = workingDirectory;
        this.brainfuckInterpreter = new BrainfuckInterpreter();
        this.accessLevelManager = new AccessLevelManager(this);
        this.webUi = webUi;

        status.setBootInstant(new Instant());

        logger.write("Hello.");
        logger.write();
        logger.write("I am Samaritan.");
        logger.write();
        logger.write("Initiating...");
        logger.write();
        logger.write("Boot Instant: " + new Instant().toString());
        logger.write();

        if (!initJda()) {
            logger.write("Invalid token! Please change it in samaritan.properties");
            System.exit(1);
            return;
        }

        this.quoteHandler = new QuoteHandler(jda, this);
        this.timer = new Timer();

        quoteHandler.start();

        this.accessLevelManager.loadUsers();

        Runtime.getRuntime().addShutdownHook(new ShutdownThread(this));

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
            SimpleLog.Level level = JDAImpl.LOG.getLevel();
            SimpleLog.Level socketLevel = WebSocketClient.LOG.getLevel();
            JDAImpl.LOG.setLevel(SimpleLog.Level.OFF);
            WebSocketClient.LOG.setLevel(SimpleLog.Level.OFF);

            jda = new JDABuilder(AccountType.BOT).setToken(botToken).buildBlocking();
            jda.getPresence().setGame(Game.of("2.0.2"));
            logger.writeFrom("jda", "Successfully connected!");
            logger.writeFrom("jda WebSocket", "Connected to WebSocket!");

            JDAImpl.LOG.setLevel(level);
            WebSocketClient.LOG.setLevel(socketLevel);
        } catch (LoginException | InterruptedException | RateLimitedException e) {
            logger.writeFrom("jda", "Couldn't connect!");
            e.printStackTrace();
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
            privateChannel.sendMessage("I must go, a reboot is in the queue!\nYou can try speaking to me again in a few moments.\nGood bye, my dear " + privateChannel.getUser().getName() + ".");
        }
        try {
            jda.getSelfUser().getManager().setName("Samaritan");
        } catch (Exception exc) {

        }
        jda.shutdown();
        if (exitSystem)
            System.exit(0);
    }

    /**
     * Starts WebSocket Server (for UI).
     */
    private void startWebSocketServer() {
        try {
            samaritanWebsocketServer = new SamaritanWebsocketServer(new InetSocketAddress(11350), this);
            samaritanWebsocketServer.start();
            getLogger().writeFrom("Web Socket Server", "Server started.");
        } catch (UnknownHostException e) {
            getLogger().writeFrom("Web Socket Server", "Server couldn't be started, error:");
            e.printStackTrace();
        }
    }

    /**
     * Set up Listeners.
     */
    private void setUpListeners() {
        this.pmListener = new PrivateMessageListener();
        this.cleverBotListener = new CleverBotListener(getJda());

        jda.addEventListener(new CommandListener(this));
        jda.addEventListener(pmListener);
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

    /**
     * @return Access Level Manager.
     */
    public AccessLevelManager getAccessLevelManager() {
        return accessLevelManager;
    }

    /**
     * @return Brainfuck code Interpreter.
     */
    public BrainfuckInterpreter getBrainfuckInterpreter() {
        return brainfuckInterpreter;
    }

    /**
     * @return Directory where Samaritan is currently running.
     */
    public File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * @return Main Admin ID.
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * @return Quote Handler.
     */
    public QuoteHandler getQuoteHandler() {
        return quoteHandler;
    }

    public User getOwner() {
        return getJda().getUserById(getOwnerId());
    }

    public Timer getTimer() {
        return timer;
    }

    public void initTwitchModule(TwitchData twitchData) {
        this.twitchModule = new TwitchModule(getJda(), twitchData);
        this.timer.schedule(twitchModule, 0L, 25000L);
    }

    public void initBeamModule(StreamData streamData) {
        this.beamModule = new BeamModule(getJda(), streamData);
        this.timer.schedule(beamModule, 0L, 25000L);
    }

    public TwitchModule getTwitchModule() {
        return twitchModule;
    }

    public BeamModule getBeamModule() {
        return beamModule;
    }
}


