package be.isach.samaritan.command;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.utils.PermissionUtil;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sacha on 14/12/16.
 */
public class CommandCleanAccueil extends Command {

    private static final int COMMANDS_PER_PAGE = 10;

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandCleanAccueil(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    /**
     * Called when the command is executed.
     *
     * @param args Arguments provided by user.
     */
    @Override
    void onExecute(String[] args) {
        if (!getGuild().getId().equals("186941943941562369")) {
            getMessageChannel().sendMessage("Not allowed");
            return;
        }

        if (!PermissionUtil.checkPermission(getExecutor(), Permission.ADMINISTRATOR, getGuild())) {
            getMessageChannel().sendMessage("Not allowed");
            return;
        }

        cleanChannel();
    }

    private void cleanChannel() {
        TextChannel messageChannel = getJda().getTextChannelById("258546506636853248");
        MessageHistory messageHistory = new MessageHistory(messageChannel);
        List<Message> messages = messageHistory.retrieveAll();
        Collections.reverse(messages);
        int a = messages.size();
        Iterator<Message> itr = messages.iterator();
        while (itr.hasNext()) {
            itr.next().deleteMessage();
            itr.remove();

            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}