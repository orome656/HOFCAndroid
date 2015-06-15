package com.hofc.hofc.utils;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.fragment.FragmentCallback;

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

    public static void handleDownloadError(VolleyError error, FragmentCallback callback) {
        if(callback != null) {
            if (error instanceof TimeoutError) {
                callback.onError(R.string.timeout_error);
            } else if (error instanceof NoConnectionError) {
                callback.onError(R.string.connexion_error);
            } else if (error instanceof ServerError) {
                callback.onError(R.string.server_error);
            } else if (error instanceof NetworkError) {
                callback.onError(R.string.connexion_error);
            } else {
                callback.onError(R.string.internal_error);
            }
        }
    }
}
