package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 29th mai, 2016
 * at 02:58
 * <p>
 * Executes JavaScript Code and returns the result, useful for easy calculations.
 */
public class CommandEval extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandEval(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        String toEval = buildStringFromArgs();
        if (toEval.isEmpty()) {
            getMessageChannel().sendMessage("What do you want to eval?");
            toEval = nextMessage().getContent();
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            Object result = engine.eval(toEval);
            System.out.println(toEval);
            if(result.toString().length() > 50) {
                getMessageChannel().sendMessage("Too big output.");
                return;
            }
            getMessageChannel().sendMessage(result.toString());
        } catch (NullPointerException | ScriptException e) {
            getMessageChannel().sendMessage("An error happened. Stacktrace printed to console.");
            System.out.println("------------------------------------");
            System.out.println("Stacktrace printed from: " + getClass().getPackage() + getClass().getName());
            System.out.println("");
            e.printStackTrace();
            System.out.println("------------------------------------");
        }
    }
}
