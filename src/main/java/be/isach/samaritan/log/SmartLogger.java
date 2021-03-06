package be.isach.samaritan.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.log
 * Created by: Sacha
 * Created on: 15th May, 2016
 * <p>
 * Description: Represents a Smart Logger.
 */
public class SmartLogger {

    private enum LogLevel {
        INFO,
        WARNING,
        ERROR
    }

    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    public String format(String logRecord, LogLevel logLevel) {
        StringBuilder builder = new StringBuilder(1000);
        builder.append(df.format(new Date(System.currentTimeMillis()))).append(" - ");
        builder.append("[").append(logLevel).append("]: ");
        builder.append(logRecord);
        return builder.toString();
    }

    public void write(LogLevel logLevel, Object... objects) {
        if (objects.length == 0)
            System.out.println(format("", logLevel));
        for (Object object : objects)
            System.out.println(format(object.toString(), logLevel));
    }

    public void writeFrom(String provider, Object... objects) {
        for (Object o : objects) {
            write("[" + provider.toUpperCase() + "]: " + o);
        }
    }

    public void write(Object... objects) {
        write(LogLevel.INFO, objects);
    }

}
