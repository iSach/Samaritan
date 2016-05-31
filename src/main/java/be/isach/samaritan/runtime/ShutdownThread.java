package be.isach.samaritan.runtime;

import be.isach.samaritan.Samaritan;
import be.isach.samaritan.chat.PrivateMessageChatThread;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.PrivateChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.runtime
 * Created by: Sacha
 * Created on: 30th mai, 2016
 * at 07:24
 * <p>
 * This thread is called by Runtime when the program shuts down.
 */
public class ShutdownThread extends Thread {

    /**
     * Samaritan instance.
     */
    private Samaritan samaritan;

    /**
     * Shutdown Thread constructor.
     *
     * @param samaritan Samaritan instance.
     */
    public ShutdownThread(Samaritan samaritan) {
        this.samaritan = samaritan;
    }

    /**
     * Called when Samaritan shuts down.
     */
    @Override
    public void run() {
        samaritan.shutdown(false);
    }
}
