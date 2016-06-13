package be.isach.samaritan.command;

import net.dv8tion.jda.entities.MessageChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 13th juin, 2016
 * at 21:45
 */
public class CommandBash extends Command {

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandBash(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        execProcess(args);
    }

    private void execProcess(String[] args) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Process p = Runtime.getRuntime().exec(args);
            BufferedReader output = getOutput(p);
            BufferedReader error = getError(p);
            String line = "";

            stringBuilder.append("```bash\nExecuted in Bash as ROOT:\n\n");
            while ((line = output.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            while ((line = error.readLine()) != null) {
                stringBuilder.append("ERR: ").append(line).append("\n");
            }
            stringBuilder.append("```");

            p.waitFor();

            getMessageChannel().sendMessage(stringBuilder.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BufferedReader getOutput(Process p) {
        return new BufferedReader(new InputStreamReader(p.getInputStream()));
    }

    private BufferedReader getError(Process p) {
        return new BufferedReader(new InputStreamReader(p.getErrorStream()));
    }

}
