package be.isach.samaritan.birthday;

import be.isach.samaritan.Samaritan;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.TimerTask;

public class BirthdayTask extends TimerTask {

    private Samaritan samaritan;

    private static DateTimeFormatter dateFormat = null;

    public BirthdayTask(Samaritan samaritan) {
        dateFormat = new DateTimeFormatterBuilder().appendPattern("").toFormatter();
        this.samaritan = samaritan;
    }

    @Override
    public void run() {
        // Hard coding lelele
        DateTime dateTime = new DateTime();
        String message = dateTime.withZone(DateTimeZone.forID("Europe/Paris")).toString("dd/MM/yyyy HH:mm:ss");
        samaritan.getJda().getGuildById("184045680245997568").getTextChannels().get(0).sendMessage(message);
    }

}