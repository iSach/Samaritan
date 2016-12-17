package be.isach.samaritan.command;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.command
 * Created by: Sacha
 * Created on: 29th mai, 2016
 *             at 02:56
 */
class CommandSay extends Command{

    /**
     * Command Constructor.
     *
     * @param messageChannel The text Channel where command is called.
     * @param commandData    The Command Data, providing the Guild, the executor and the Samaritan instance.
     * @param args           The args provided when command was called.
     */
    CommandSay(MessageChannel messageChannel, CommandData commandData, String[] args) {
        super(messageChannel, commandData, args);
    }

    @Override
    void onExecute(String[] args) {
        String toSay = buildStringFromArgs();
        if (toSay.isEmpty()) {
            getMessageChannel().sendMessage("What do you want to say?").queue();
            toSay = nextMessage().getContent();
        }
        if(toSay.contains("#")) {
            char[] chars = toSay.toCharArray();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("#");
            checkloop:
            for(int i = 0; i < chars.length; i++) {
                if(chars[i] == '#') {
                    int j = i+1;
                    while(j < chars.length && Character.isDigit(chars[j])) {
                        stringBuilder.append(chars[j]);
                        j++;
                    }
                    break checkloop;
                }
            }
            if(!stringBuilder.toString().equals("#")) {
                String disc = stringBuilder.toString().replace("#", "");
                for(Member member : getGuild().getMembers()) {
                    if(member.getUser().getDiscriminator().equals(disc)) {
                        toSay = toSay.replace(stringBuilder.toString(), member.getAsMention());
                        break;
                    }
                }
            }
        }
        try {
            getMessageChannel().sendMessage(toSay).queue();
        } catch (Exception exc) {
            try {
                sleep(5000);
                getMessageChannel().sendMessage("oooh, calm down mate! Relax and stop spamming!").queue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
