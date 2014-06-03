package com.hofc.hofc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hofc.hofc.vo.CalendrierLineVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class CalendrierBDD {
	
	// Base de données
    protected SQLiteDatabase hofcDatabase;
 
    protected HOFCOpenHelper hofcOpenHelper;
    
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
     * Constructeur par défaut
     */
    public CalendrierBDD(Context context) {
    	hofcOpenHelper = new HOFCOpenHelper(context, null);
    }
 
    /**
     * Ouverture de la connection
     */
    public void Open() {
    	hofcDatabase = hofcOpenHelper.getWritableDatabase();
    }
 
    /**
     * Fermeture de la connection
     */
    public void Close() {
    	hofcDatabase.close();
    }
    
    public List<CalendrierLineVO> getAll() {
    	ArrayList<CalendrierLineVO> list = null;
    	Cursor cursor = hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, null, null, null, null, CalendrierEntry.COLUMN_DATE);
    	if(cursor.getCount() > 0){
    		list = new ArrayList<CalendrierLineVO>();
    		while(cursor.moveToNext()) {
        		CalendrierLineVO line = new CalendrierLineVO();
    			line.setEquipe1(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_1));
    			line.setEquipe2(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_1));
    			line.setScore1(cursor.getInt(CalendrierEntry.NUM_COLUMN_EQUIPE_1));
    			line.setScore2(cursor.getInt(CalendrierEntry.NUM_COLUMN_EQUIPE_1));
    			list.add(line);
        	}
    	}
    	return list;
    }
    
    public Cursor getAllInCursor(){
    	return hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, null, null, null, null, CalendrierEntry.COLUMN_DATE);
    }
    public void insertList(List<CalendrierLineVO> list) {
    	for(CalendrierLineVO line : list) {
    		Cursor cursor = hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, CalendrierEntry.COLUMN_EQUIPE_1 + " ='"+ line.getEquipe1() +"' and " + CalendrierEntry.COLUMN_EQUIPE_2 + " ='"+line.getEquipe2()+"'", null, null, null, null);
			ContentValues values = new ContentValues();
			values.put(CalendrierEntry.COLUMN_SCORE_1, line.getScore1());
			values.put(CalendrierEntry.COLUMN_SCORE_2, line.getScore2());
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
}
