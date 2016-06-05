package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;
import org.luaj.vm2.Lua;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 05th juin, 2016
 * at 15:16
 */
public class CommandLua extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandLua(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }


    @Override
    void onExecute(String[] args) {

    }
}
