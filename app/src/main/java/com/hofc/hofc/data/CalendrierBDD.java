package com.hofc.hofc.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.hofc.hofc.vo.CalendrierLineVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalendrierBDD extends CommonBDD<CalendrierLineVO> {

	public CalendrierBDD(Context c) {
		super(c);
        this.tableName = CalendrierEntry.CALENDRIER_TABLE_NAME;
	}
    
	public static abstract class CalendrierEntry implements BaseColumns {
	    //public static final String COLUMN_ID = "ID";
	    //public static final int NUM_COLUMN_ID = 0;
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
		public static final String COLUMN_CATEGORIE = "CATEGORIE";
		public static final int NUM_COLUMN_CATEGORIE = 6;
		
	}
    
    public List<CalendrierLineVO> getAll() {
    	openReadable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	ArrayList<CalendrierLineVO> list = null;
    	Cursor cursor = hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, null, null, null, null, CalendrierEntry.COLUMN_DATE);
    	if(cursor.getCount() > 0){
    		list = new ArrayList<>();
    		while(cursor.moveToNext()) {
        		CalendrierLineVO line = new CalendrierLineVO();
    			line.setEquipe1(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_1));
    			line.setEquipe2(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_2));
                if(!cursor.isNull(CalendrierEntry.NUM_COLUMN_SCORE_1)) {
                    line.setScore1(cursor.getInt(CalendrierEntry.NUM_COLUMN_SCORE_1));
                    line.setScore2(cursor.getInt(CalendrierEntry.NUM_COLUMN_SCORE_2));
                } else {
                    line.setScore1(null);
                    line.setScore2(null);
                }
    			try {
    				if(cursor.getString(CalendrierEntry.NUM_COLUMN_DATE) != null)
    					line.setDate(sdf.parse(cursor.getString(CalendrierEntry.NUM_COLUMN_DATE)));
				} catch (ParseException e) {
					Log.e("HOFC", e.getMessage());
				}
    			list.add(line);
        	}
    	}
        cursor.close();
    	return list;
    }

	public List<CalendrierLineVO> getWithKey(final String key) {
		openReadable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		ArrayList<CalendrierLineVO> list = null;
		Cursor cursor = hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, CalendrierEntry.NUM_COLUMN_CATEGORIE+"=?", new String[]{key}, null, null, CalendrierEntry.COLUMN_DATE);
		if(cursor.getCount() > 0){
			list = new ArrayList<>();
			while(cursor.moveToNext()) {
				CalendrierLineVO line = new CalendrierLineVO();
				line.setEquipe1(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_1));
				line.setEquipe2(cursor.getString(CalendrierEntry.NUM_COLUMN_EQUIPE_2));
				if(!cursor.isNull(CalendrierEntry.NUM_COLUMN_SCORE_1)) {
					line.setScore1(cursor.getInt(CalendrierEntry.NUM_COLUMN_SCORE_1));
					line.setScore2(cursor.getInt(CalendrierEntry.NUM_COLUMN_SCORE_2));
				} else {
					line.setScore1(null);
					line.setScore2(null);
				}
				try {
					if(cursor.getString(CalendrierEntry.NUM_COLUMN_DATE) != null)
						line.setDate(sdf.parse(cursor.getString(CalendrierEntry.NUM_COLUMN_DATE)));
				} catch (ParseException e) {
					Log.e("HOFC", e.getMessage());
				}
				list.add(line);
			}
		}
		cursor.close();
		return list;
	}

	@Override
    public void insertList(List<CalendrierLineVO> list) {
    	openWritable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		// On supprime avant d'insérer pour mettre a jour les données si il y a eu des suppressions
		hofcDatabase.delete(CalendrierEntry.CALENDRIER_TABLE_NAME, null, null);
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
            cursor.close();
    	}
    }
	@Override
	public void insertListWithKey(String key, List<CalendrierLineVO> list) {
		openWritable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		// On supprime avant d'insérer pour mettre a jour les données si il y a eu des suppressions
		hofcDatabase.delete(CalendrierEntry.CALENDRIER_TABLE_NAME, CalendrierEntry.COLUMN_CATEGORIE + "=?", new String[]{key});
		for(CalendrierLineVO line : list) {
			Cursor cursor = hofcDatabase.query(CalendrierEntry.CALENDRIER_TABLE_NAME, null, CalendrierEntry.COLUMN_EQUIPE_1 + " ='"+ line.getEquipe1() +"' and " + CalendrierEntry.COLUMN_EQUIPE_2 + " ='"+line.getEquipe2() +"' and " + CalendrierEntry.COLUMN_CATEGORIE + " ='"+key+"'", null, null, null, null);
			ContentValues values = new ContentValues();
			values.put(CalendrierEntry.COLUMN_SCORE_1, line.getScore1());
			values.put(CalendrierEntry.COLUMN_SCORE_2, line.getScore2());
			if(line.getDate() != null) {
				values.put(CalendrierEntry.COLUMN_DATE, sdf.format(line.getDate()));
			}
			values.put(CalendrierEntry.COLUMN_CATEGORIE, key);
			if(cursor.getCount() > 0) {
				// UPDATE
				hofcDatabase.update(CalendrierEntry.CALENDRIER_TABLE_NAME, values, CalendrierEntry.COLUMN_EQUIPE_1 + " ='"+ line.getEquipe1() +"' and " + CalendrierEntry.COLUMN_EQUIPE_2 + " ='"+line.getEquipe2()+"'", null);
			} else {
				// INSERT
				values.put(CalendrierEntry.COLUMN_EQUIPE_1, line.getEquipe1());
				values.put(CalendrierEntry.COLUMN_EQUIPE_2, line.getEquipe2());
				hofcDatabase.insert(CalendrierEntry.CALENDRIER_TABLE_NAME, null, values);
			}
			cursor.close();
		}
	}
}
