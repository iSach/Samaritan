package be.isach.samaritan.birthday;

import be.isach.samaritan.Samaritan;
import com.google.common.collect.Maps;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class BirthdayTask extends TimerTask {

    private Map<User, DateTime> birthdays = Maps.newHashMap();

    private Samaritan samaritan;

    public BirthdayTask(Samaritan samaritan) {
        this.samaritan = samaritan;
        this.birthdays.put(samaritan.getJda().getUserById("93721838093352960"), new DateTime(2000, 6, 24, 13, 4, 0));
        this.birthdays.put(samaritan.getJda().getUserById("85704559401369600"), new DateTime(1998, 7, 8, 6, 45, 0));
    }

    @Override
    public void run() {
        System.out.println("[Birthday Manager]: Checking for birthdays...");
        DateTime dt = new DateTime();
        DateTime dateTime = dt.withZone(DateTimeZone.forID("Europe/Paris"));
        boolean atLeastOneFound = false;
        for (Map.Entry entry : birthdays.entrySet()) {
            User user = (User) entry.getKey();
            DateTime birthdayDate = (DateTime) entry.getValue();
            if (birthdayDate.getHourOfDay() == dateTime.getHourOfDay()
                    && birthdayDate.getMinuteOfHour() == dateTime.getMinuteOfHour()
                    && birthdayDate.getDayOfMonth() == dateTime.getDayOfMonth()
                    && birthdayDate.getMonthOfYear() == dateTime.getMonthOfYear()) {
                for (Guild guild : samaritan.getJda().getGuilds()) {
                    atLeastOneFound = true;
                    System.out.println("[Birthday Manager]: Birthday found! For: " + user.getUsername());
                    String stringBuilder = ("Happy birthday " + user.getAsMention() + " !\n") +
                            "You are now " + (dateTime.getYear() - birthdayDate.getYear()) + " years old!\n" +
                            "Birthday is at exactly: " + birthdayDate.toString("dd/MM/yyyy HH:mm") + "\n" +
                            "https://media.giphy.com/media/IQF90tVlBIByw/giphy.gif";
                    if (guild.getId().equals("186941943941562369")) {
                        guild.getTextChannels().get(1).sendMessage(stringBuilder);
                    } else {
                        guild.getTextChannels().get(0).sendMessage(stringBuilder);
                    }
                }
            }
        }
        if(!atLeastOneFound) {
            System.out.println("[Birthday Manager]: No Birthday found.");
        }
    }

}