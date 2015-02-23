package com.hofc.hofc.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hofc.hofc.constant.ServerConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CommonBDD {

    
	public static boolean isSynchroNeeded(SQLiteDatabase hofcDatabase, String bddName) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -ServerConstant.NOMBRE_JOUR_SYNCHRO);
		Cursor cursor = hofcDatabase.query("date_synchro", null, "nom='"+bddName+"' and date > "+sdf.format(calendar.getTime()), null, null, null, null);
		return cursor.getCount() <= 0;
	}

	public static Date getDateSynchro(SQLiteDatabase hofcDatabase, String bddName) {
		Date result = null;
		Cursor cursor = hofcDatabase.query("date_synchro", null, "nom='"+bddName+"'", null, null, null, null);
		if(cursor.moveToFirst()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				result = sdf.parse(cursor.getString(cursor.getColumnIndex("date")));
			} catch (ParseException e) {
                Log.e(CommonBDD.class.getName(),"Error while parsing sync date", e);
            }
			return result;
		} else {
			return null;
		}
	}
	
	public static void updateDateSynchro(SQLiteDatabase hofcDatabase, String bddName, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Cursor cursor = hofcDatabase.query("date_synchro", null, "nom='"+bddName+"'", null, null, null, null);
		ContentValues values = new ContentValues();
		values.put("date", sdf.format(date));
		if(cursor.getCount() > 0) {
			hofcDatabase.update("date_synchro", values, "nom='"+bddName+"'", null);
		} else {
			values.put("nom", bddName);
			hofcDatabase.insert("date_synchro", null, values);
		}
	}
}
