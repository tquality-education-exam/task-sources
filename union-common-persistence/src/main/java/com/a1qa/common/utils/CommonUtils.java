package com.a1qa.common.utils;

import com.a1qa.model.constants.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by p.ordenko on 03.09.2015, 17:10.
 */
public final class CommonUtils {

    /**
     * Get duration between end time and start time
     * @param endTime End time
     * @param startTime Start time
     * @return String representation of duration in format {@link Config#DURATION_FORMAT}
     */
    public static String getDurationAsString(Date endTime, Date startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(Config.DURATION_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // To avoid +3h for Minsk time
        try {
            return sdf.format(new Date(endTime.getTime() - startTime.getTime()));
        } catch (NullPointerException npe) {
            return sdf.format(0L);
        }
    }

    /**
     * Get date calculated with specified days amount from now
     * @param offsetInDays Days amount from now
     * @return Calculated date
     */
    public static Date getDateFromNow(int offsetInDays) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, offsetInDays);
        return calendar.getTime();
    }

}
