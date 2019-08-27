package etf.santorini.km150066d;

import java.util.Calendar;

public class DateTimeHelper {

    public static String getCurrentDateTimeString() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String stringMonth, stringDay, stringHour, stringMinute, stringSecond;
        if (month < 10)
            stringMonth = "0" + month;
        else
            stringMonth = "" + month;

        if (day < 10)
            stringDay = "0" + day;
        else
            stringDay = "" + day;

        if (hour < 10)
            stringHour = "0" + hour;
        else
            stringHour = "" + hour;

        if (minute < 10)
            stringMinute = "0" + minute;
        else
            stringMinute = "" + minute;

        if (second < 10)
            stringSecond = "0" + second;
        else
            stringSecond = "" + second;

        return year + "_" + stringMonth + "_" + stringDay + "_" + stringHour + ":" + stringMinute + ":" + stringSecond;
    }
}
