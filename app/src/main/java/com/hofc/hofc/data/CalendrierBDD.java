package com.hofc.hofc.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.vo.CalendrierLineVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class CalendrierBDD {
	
	// Base de données
    protected static SQLiteDatabase hofcDatabase;
    protected static HOFCOpenHelper hofcOpenHelper;
    private static Context context = null;
    
	public static abstract class CalendrierEntry implements BaseColumns {
	    public static final String COLUMN_ID = "ID";
	    public static final int NUM_COLUMN_ID = 0;
	    public static final String CALENDRIER_TABLE_NAME = "calendrier";
	    public static final String COLUMN_EQUIPE_1 = "EQUIPE_1";
	    public static final int NUM_COLUMN_EQUIPE_1 = 1;
	    public static final String COLUMN_SCORE_1 = "SCORE_1";
	    public static final int NUM_COLUMN_SCORE_1 = 2;
	    public static final String COLUMN_SCORE_2 = "SCORE_2";
	    public static final int NUM_COLUMN_SCORE_2 = 3;
	    public static final String COLUMN_EQUIPE_2 = "EQUIPE_2";
	    public static final int NUM_COLUMN_EQUIPE_2 = 4;
	    public static final String COLUMN_DATE = "DATE";
	    public static final int NUM_COLUMN_DATE = 5;
		
	}
	/**
	 * Constructeur
	 */
	private CalendrierBDD(){};
	
    /**
     * Constructeur par défaut
     */
    public static void initiate(Context context) {
    	if(hofcOpenHelper == null)
    		hofcOpenHelper = new HOFCOpenHelper(context, null);
    	
    	if(CalendrierBDD.context == null) 
    		CalendrierBDD.context = context;
    }
    
    public static void openReadable() {
    	if(hofcDatabase == null) 
    		hofcDatabase = hofcOpenHelper.getReadableDatabase();
    }
 
    public static void openWritable() throws SQLException{
        if ((hofcDatabase == null)? true : hofcDatabase.isReadOnly()) {
            openWritable(true);
        }
    }
    
    /**
     * Opens the database for writing
     * @param foreignKeys State of Foreign Keys Constraint, true = ON, false = OFF
     * @throws SQLException if the database cannot be opened for writing
     */
    public static void openWritable(boolean foreignKeys) throws SQLException{
    	hofcDatabase = hofcOpenHelper.getWritableDatabase();
        if (foreignKeys) {
        	hofcDatabase.execSQL("PRAGMA foreign_keys = ON;");
        } else {
        	hofcDatabase.execSQL("PRAGMA foreign_keys = OFF;");
        }
    }
    
    /**
     * Closes the database
     */
    public static void close(){
        if (hofcDatabase != null){
        	hofcDatabase.close();
        	hofcDatabase = null;
        }
        if (hofcOpenHelper != null){
        	hofcOpenHelper.close();
        	hofcOpenHelper = null;
        }
    }
    
    public static List<CalendrierLineVO> getAll() {
    	openReadable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	ArrayList<CalendrierLineVO> list = null;
    	Cursor cursor = hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, null, null, null, null, CalendrierEntry.COLUMN_DATE);
    	if(cursor.getCount() > 0){
    		list = new ArrayList<CalendrierLineVO>();
    		while(cursor.moveToNext()) {
        		CalendrierLineVO line = new CalendrierLineVO();
    			line.setEquipe1(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_1));
    			line.setEquipe2(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_2));
    			line.setScore1(cursor.getInt(CalendrierEntry.NUM_COLUMN_SCORE_1));
    			line.setScore2(cursor.getInt(CalendrierEntry.NUM_COLUMN_SCORE_2));
    			try {
    				if(cursor.getString(CalendrierEntry.NUM_COLUMN_DATE) != null)
    					line.setDate(sdf.parse(cursor.getString(CalendrierEntry.NUM_COLUMN_DATE)));
				} catch (ParseException e) {
					Log.e("HOFC", e.getMessage());
				}
    			list.add(line);
        	}
    	}
    	return list;
    }
    
    public static Cursor getAllInCursor(){
    	return hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, null, null, null, null, CalendrierEntry.COLUMN_DATE);
    }
    public static void insertList(List<CalendrierLineVO> list) {
    	openWritable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	for(CalendrierLineVO line : list) {
    		Cursor cursor = hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, CalendrierEntry.COLUMN_EQUIPE_1 + " ='"+ line.getEquipe1() +"' and " + CalendrierEntry.COLUMN_EQUIPE_2 + " ='"+line.getEquipe2()+"'", null, null, null, null);
			ContentValues values = new ContentValues();
			values.put(CalendrierEntry.COLUMN_SCORE_1, line.getScore1());
			values.put(CalendrierEntry.COLUMN_SCORE_2, line.getScore2());
			if(line.getDate() != null) {
				values.put(CalendrierEntry.COLUMN_DATE, sdf.format(line.getDate()));
			}
    		if(cursor.getCount() > 0) {
    			// UPDATE
    			hofcDatabase.update(CalendrierEntry.CALENDRIER_TABLE_NAME, values, CalendrierEntry.COLUMN_EQUIPE_1 + " ='"+ line.getEquipe1() +"' and " + CalendrierEntry.COLUMN_EQUIPE_2 + " ='"+line.getEquipe2()+"'", null);
    		} else {
    			// INSERT
    			values.put(CalendrierEntry.COLUMN_EQUIPE_1, line.getEquipe1());
    			values.put(CalendrierEntry.COLUMN_EQUIPE_2, line.getEquipe2());
    			hofcDatabase.insert(CalendrierEntry.CALENDRIER_TABLE_NAME, null, values);
    		}
    	}
    }

	public static boolean isSynchroNeeded() {
		openReadable();
		return CommonBDD.isSynchroNeeded(hofcDatabase, "calendrier");
	}

	public static Date getDateSynchro() {
		openReadable();
		return CommonBDD.getDateSynchro(hofcDatabase, "calendrier");
	}
	
	public static void updateDateSynchro(Date date) {
		openWritable();
		CommonBDD.updateDateSynchro(hofcDatabase, "calendrier", date);
	}
}
