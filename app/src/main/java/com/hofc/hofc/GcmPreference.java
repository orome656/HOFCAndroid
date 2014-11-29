package com.hofc.hofc;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Anthony on 29/11/2014.
 */
public class GcmPreference {
    private static final String PREFERENCE_NAME = "HOFC";
    private static final String PROPERTY_REG_ID = "GCMregId";

    static String getRegistrationId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        return registrationId;
    }

    static void setRegistrationId(Context context, String regId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.commit();
    }
}
