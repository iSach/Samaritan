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
        this.birthdays.put(samaritan.getJda().getUserById("93721838093352960"), new DateTime(2000, 6, 24, 1, 6, 0));
    }

    @Override
    public void run() {
        System.out.println("Checking for birthdays.");
        // Hard coding lelele | .toString("dd/MM/yyyy HH:mm:ss")
        DateTime dt = new DateTime();
        DateTime dateTime = dt.withZone(DateTimeZone.forID("Europe/Paris")).plusMinutes(1);
        System.out.println(birthdays.entrySet());
        for (Map.Entry entry : birthdays.entrySet()) {
            User user = (User) entry.getKey();
            DateTime birthdayDate = (DateTime) entry.getValue();
            System.out.println(birthdayDate.getHourOfDay() + " | " + dateTime.getHourOfDay());
            System.out.println(birthdayDate.getMinuteOfHour() + " | " + dateTime.getMinuteOfHour());
            System.out.println(birthdayDate.getDayOfMonth() + " | " + dateTime.getDayOfMonth());
            System.out.println(birthdayDate.getMonthOfYear() + " | " + dateTime.getMonthOfYear());
            if (birthdayDate.getHourOfDay() == dateTime.getHourOfDay()
                    && birthdayDate.getMinuteOfHour() == dateTime.getMinuteOfHour()
                    && birthdayDate.getDayOfMonth() == dateTime.getDayOfMonth()
                    && birthdayDate.getMonthOfYear() == dateTime.getMonthOfYear()) {
                samaritan.getJda().getGuilds().stream().filter(guild -> guild.getId().equals("184045680245997568")).forEachOrdered(guild -> {
                    String stringBuilder = ("Happy birthday " + user.getAsMention() + " !\n") +
                            "You are now " + (dateTime.getYear() - birthdayDate.getYear()) + " years old!\n" +
                            "Birthday is at exactly: " + birthdayDate.toString("dd/MM/yyyy HH:mm:ss");
                    guild.getTextChannels().get(0).sendMessage(stringBuilder);
                });
            }
        }
    }

}