package com.hofc.hofc.data;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.hofc.hofc.vo.ActuVO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

public class ActusBDD {

	// Base de donn�es
    protected static SQLiteDatabase hofcDatabase;
    protected static HOFCOpenHelper hofcOpenHelper;
    private static Context context = null;
    
    private static abstract class ActusEntry implements BaseColumns {
	    public static final String COLUMN_ID = "ID";
	    public static final int NUM_COLUMN_ID = 0;
	    public static final String ACTUS_TABLE_NAME = "actus";
	    public static final String COLUMN_TITLE = "titre";
	    public static final int NUM_COLUMN_TITLE = 1;
	    public static final String COLUMN_DESCRIPTION = "description";
	    public static final int NUM_COLUMN_DESCRIPTION = 2;
	    public static final String COLUMN_DATE = "date";
	    public static final int NUM_COLUMN_DATE = 3;
	    public static final String COLUMN_IMG = "image";
	    public static final int NUM_COLUMN_IMG = 4;
	    public static final String COLUMN_POST_ID = "postId";
	    public static final int NUM_COLUMN_POST_ID = 5;
	    public static final String COLUMN_URL = "url";
	    public static final int NUM_COLUMN_URL = 6;
    	
    }
	/**
	 * Constructeur
	 */
	private ActusBDD(){};

    /**
     * Constructeur par d�faut
     */
    public static void initiate(Context context) {
    	if(hofcOpenHelper == null)
    		hofcOpenHelper = new HOFCOpenHelper(context, null);
    	
    	if(ActusBDD.context == null) 
    		ActusBDD.context = context;
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
    
    public static List<ActuVO> getAll() {
    	openReadable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	ArrayList<ActuVO> list = null;
    	Cursor cursor = hofcDatabase.query(ActusEntry.ACTUS_TABLE_NAME, null, null, null, null, null, ActusEntry.COLUMN_DATE);
    	if(cursor.getCount() > 0){
    		list = new ArrayList<ActuVO>();
    		while(cursor.moveToNext()) {
    			ActuVO line = new ActuVO();
    			line.setTitre(cursor.getString(ActusEntry.NUM_COLUMN_TITLE));
    			line.setTexte(cursor.getString(ActusEntry.NUM_COLUMN_DESCRIPTION));
    			line.setUrl(cursor.getString(ActusEntry.NUM_COLUMN_URL));
    			line.setPostId(cursor.getInt(ActusEntry.NUM_COLUMN_POST_ID));
    			try {
    				if(cursor.getString(ActusEntry.NUM_COLUMN_DATE) != null)
    					line.setDate(sdf.parse(cursor.getString(ActusEntry.NUM_COLUMN_DATE)));
				} catch (ParseException e) {
					Log.e("HOFC", e.getMessage());
				}
    			byte[] bb = cursor.getBlob(ActusEntry.NUM_COLUMN_IMG);
    			line.setBitmapImage(BitmapFactory.decodeByteArray(bb, 0, bb.length));
    			list.add(line);
        	}
    	}
    	return list;
    }
    
    public static Cursor getAllInCursor(){
    	return hofcDatabase.query(ActusEntry.ACTUS_TABLE_NAME, null, null, null, null, null, ActusEntry.COLUMN_DATE);
    }
    
    public static void insertList(List<ActuVO> list) {
    	openWritable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	for(ActuVO line : list) {
    		Cursor cursor = hofcDatabase.query(ActusEntry.ACTUS_TABLE_NAME, null, ActusEntry.COLUMN_POST_ID + " ="+ line.getPostId(), null, null, null, null);
			ContentValues values = new ContentValues();
			values.put(ActusEntry.COLUMN_DATE, sdf.format(line.getDate()));
			values.put(ActusEntry.COLUMN_DESCRIPTION, line.getTexte());
			values.put(ActusEntry.COLUMN_TITLE, line.getTitre());
			values.put(ActusEntry.COLUMN_URL, line.getUrl());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			line.getBitmapImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
			values.put(ActusEntry.COLUMN_IMG, stream.toByteArray());
    		if(cursor.getCount() > 0) {
    			// UPDATE
    			hofcDatabase.update(ActusEntry.ACTUS_TABLE_NAME, values, ActusEntry.COLUMN_POST_ID + " ='"+ line.getPostId() + "'", null);
    		} else {
    			// INSERT
    			values.put(ActusEntry.COLUMN_POST_ID, line.getPostId());
    			hofcDatabase.insert(ActusEntry.ACTUS_TABLE_NAME, null, values);
    		}
    	}
    }

	public static boolean isSynchroNeeded() {
		openReadable();
		return CommonBDD.isSynchroNeeded(hofcDatabase, "actus");
	}

	public static Date getDateSynchro() {
		openReadable();
		return CommonBDD.getDateSynchro(hofcDatabase, "actus");
	}
	
	public static void updateDateSynchro(Date date) {
		openWritable();
		CommonBDD.updateDateSynchro(hofcDatabase, "actus", date);
	}
}
