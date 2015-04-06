package com.hofc.hofc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HOFCUtils {
	public static String getTabTitle(int weekDiff) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.WEEK_OF_YEAR, weekDiff);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return sdf.format(cal.getTime());
    }

    public static boolean isDateInCurrentWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        Calendar calParam = Calendar.getInstance();
        calParam.setTime(date);



        return cal.get(Calendar.YEAR) == calParam.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == calParam.get(Calendar.DAY_OF_YEAR);

    }
}
