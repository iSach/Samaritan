package be.isach.samaritan.birthday;

import be.isach.samaritan.Samaritan;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.TimerTask;

public class BirthdayTask extends TimerTask {

    private Samaritan samaritan;

    private static DateTimeFormatter dateFormat = null;

    public BirthdayTask(Samaritan samaritan) {
        dateFormat = new DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy HH:mm:ss").toFormatter();
        this.samaritan = samaritan;
    }

    @Override
    public void run() {
        // Hard coding lelele
        Instant instant = new Instant();
        instant.plus(1000 * 60 * 60 * 2 - 1000 * 60);
        samaritan.getJda().getGuildById("184045680245997568").getTextChannels().get(0).sendMessage(instant.toString(dateFormat));
    }

}