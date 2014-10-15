package com.hofc.hofc.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hofc.hofc.vo.ClassementLineVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ClassementBDD {
	
	// Base de données
    protected static SQLiteDatabase hofcDatabase;
    protected static HOFCOpenHelper hofcOpenHelper;
    private static Context context = null;
    
	public static abstract class ClassementEntry implements BaseColumns {
	    public static final String COLUMN_ID = "ID";
	    public static final int NUM_COLUMN_ID = 0;
	    public static final String CLASSEMENT_TABLE_NAME = "classement";
	    public static final String COLUMN_NOM = "NOM";
	    public static final int NUM_COLUMN_NOM = 1;
	    public static final String COLUMN_POINTS = "POINTS";
	    public static final int NUM_COLUMN_POINTS = 2;
	    public static final String COLUMN_JOUE = "JOUE";
	    public static final int NUM_COLUMN_JOUE = 3;
	    public static final String COLUMN_GAGNE = "GAGNE";
	    public static final int NUM_COLUMN_GAGNE = 4;
	    public static final String COLUMN_NUL = "NUL";
	    public static final int NUM_COLUMN_NUL = 5;
	    public static final String COLUMN_PERDU = "PERDU";
	    public static final int NUM_COLUMN_PERDU = 6;
	    public static final String COLUMN_BP = "BP";
	    public static final int NUM_COLUMN_BP = 7;
	    public static final String COLUMN_BC = "BC";
	    public static final int NUM_COLUMN_BC = 8;
	    public static final String COLUMN_DIFF = "DIFF";
	    public static final int NUM_COLUMN_DIFF = 9;
		
	}
	/**
	 * Constructeur
	 */
	private ClassementBDD(){}
	
    /**
     * Constructeur par défaut
     */
    public static void initiate(Context context) {
    	if(hofcOpenHelper == null)
    		hofcOpenHelper = new HOFCOpenHelper(context, null);
    	
    	if(ClassementBDD.context == null) 
    		ClassementBDD.context = context;
    }
    
    public static void openReadable() {
    	if(hofcDatabase == null) 
    		hofcDatabase = hofcOpenHelper.getReadableDatabase();
    }
 
    public static void openWritable() throws SQLException{
        if ((hofcDatabase == null) || hofcDatabase.isReadOnly()) {
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
    
    public static List<ClassementLineVO> getAll() {
    	openReadable();
    	ArrayList<ClassementLineVO> list = null;
    	Cursor cursor = hofcDatabase.query(ClassementEntry.CLASSEMENT_TABLE_NAME, null, null, null, null, null, ClassementEntry.COLUMN_POINTS + " DESC");
    	if(cursor.getCount() > 0){
    		list = new ArrayList<ClassementLineVO>();
    		while(cursor.moveToNext()) {
    			ClassementLineVO line = new ClassementLineVO();
    			line.setNom(cursor.getString(ClassementEntry.NUM_COLUMN_NOM));
    			line.setPoints(cursor.getInt(ClassementEntry.NUM_COLUMN_POINTS));
    			line.setJoue(cursor.getInt(ClassementEntry.NUM_COLUMN_JOUE));
    			line.setGagne(cursor.getInt(ClassementEntry.NUM_COLUMN_GAGNE));
    			line.setNul(cursor.getInt(ClassementEntry.NUM_COLUMN_NUL));
    			line.setPerdu(cursor.getInt(ClassementEntry.NUM_COLUMN_PERDU));
    			line.setBp(cursor.getInt(ClassementEntry.NUM_COLUMN_BP));
    			line.setBc(cursor.getInt(ClassementEntry.NUM_COLUMN_BC));
    			line.setDiff(cursor.getInt(ClassementEntry.NUM_COLUMN_DIFF));
    			list.add(line);
        	}
    	}
    	return list;
    }
    
    public static Cursor getAllInCursor(){
    	return hofcDatabase.query(ClassementEntry.CLASSEMENT_TABLE_NAME, null, null, null, null, null, ClassementEntry.COLUMN_POINTS + " DESC");
    }
    
    public static void insertList(List<ClassementLineVO> list) {
    	openWritable();
    	for(ClassementLineVO line : list) {
    		Cursor cursor = hofcDatabase.query(ClassementEntry.CLASSEMENT_TABLE_NAME, null, ClassementEntry.COLUMN_NOM + " ='"+ line.getNom() + "'", null, null, null, null);
			ContentValues values = new ContentValues();
			values.put(ClassementEntry.COLUMN_POINTS, line.getPoints());
			values.put(ClassementEntry.COLUMN_JOUE, line.getJoue());
			values.put(ClassementEntry.COLUMN_GAGNE, line.getGagne());
			values.put(ClassementEntry.COLUMN_NUL, line.getNul());
			values.put(ClassementEntry.COLUMN_PERDU, line.getPerdu());
			values.put(ClassementEntry.COLUMN_BP, line.getBp());
			values.put(ClassementEntry.COLUMN_BC, line.getBc());
			values.put(ClassementEntry.COLUMN_DIFF, line.getDiff());
    		if(cursor.getCount() > 0) {
    			// UPDATE
    			hofcDatabase.update(ClassementEntry.CLASSEMENT_TABLE_NAME, values, ClassementEntry.COLUMN_NOM + " ='"+ line.getNom() + "'", null);
    		} else {
    			// INSERT
    			values.put(ClassementEntry.COLUMN_NOM, line.getNom());
    			hofcDatabase.insert(ClassementEntry.CLASSEMENT_TABLE_NAME, null, values);
    		}
    	}
    }

	public static boolean isSynchroNeeded() {
		openReadable();
		return CommonBDD.isSynchroNeeded(hofcDatabase, "classement");
	}

	public static Date getDateSynchro() {
		openReadable();
		return CommonBDD.getDateSynchro(hofcDatabase, "classement");
	}
	
	public static void updateDateSynchro(Date date) {
		openWritable();
		CommonBDD.updateDateSynchro(hofcDatabase, "classement", date);
	}
}
