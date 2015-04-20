package com.hofc.hofc.utils;

import com.hofc.hofc.constant.ServerConstant;

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
    public static String getCurrentWeekMonday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(cal.getTime());
    }

    public static String buildUrl(String context, String[] params) {
        StringBuilder stringBuilder = new StringBuilder(ServerConstant.SERVER_URL_PREFIX);
        stringBuilder.append(ServerConstant.SERVER_URL);
        if(ServerConstant.SERVER_PORT != 0) {
            stringBuilder.append(":");
            stringBuilder.append(ServerConstant.SERVER_PORT);
        }
        stringBuilder.append("/");
        stringBuilder.append(context);
        if(params != null && params.length > 0) {
            for(String param: params) {
                stringBuilder.append("/").append(param);
            }
        }
        return stringBuilder.toString();
    }
}
