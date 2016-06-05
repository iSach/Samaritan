package be.isach.samaritan.util;

import org.joda.time.*;

/**
 * Project: samaritan
 * Package: be.isach.samaritan.util
 * Created by: Sacha
 * Created on: 28th May, 2016
 * at 00:16
 */
public class SamaritanStatus {

    /**
     * Instant when Samaritan started.
     */
    private Instant bootInstant;

    /**
     * Sets when Samaritan was started.
     *
     * @param bootInstant Instant when Samaritan was started.
     */
    public void setBootInstant(Instant bootInstant) {
        this.bootInstant = bootInstant;
    }

    /**
     * @return The Instant when it was started.
     */
    public Instant getBootInstant() {
        return bootInstant;
    }

    /**
     * @return the Uptime as a Period.
     */
    public Duration getUpTime() {
        Interval interval = new Interval(bootInstant, new Instant());
        return interval.toDuration();
    }

    /**
     * @return The Uptime as a built String.
     */
    public String getUptimeString() {
        Duration period = getUpTime();
        return String.valueOf((period.getStandardDays())) + " days " +
                (period.getStandardHours() % 24) + "h " +
                (period.getStandardMinutes() % 60) + "m " +
                (period.getStandardSeconds() % 60) + "s";
    }
}
