package com.hofc.hofc.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.util.Log;

import com.hofc.hofc.vo.ActuVO;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActusBDD extends CommonBDD<ActuVO> {

	public ActusBDD(Context c) {
		super(c);
		this.tableName = ActusEntry.ACTUS_TABLE_NAME;
	}

    private static abstract class ActusEntry implements BaseColumns {
	    //public static final String COLUMN_ID = "ID";
	    //public static final int NUM_COLUMN_ID = 0;
	    public static final String ACTUS_TABLE_NAME = "actus";
	    public static final String COLUMN_TITLE = "TITRE";
	    public static final int NUM_COLUMN_TITLE = 1;
	    public static final String COLUMN_POST_ID = "POST_ID";
	    public static final int NUM_COLUMN_POST_ID = 2;
	    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
	    public static final int NUM_COLUMN_DESCRIPTION = 3;
	    public static final String COLUMN_URL = "URL";
	    public static final int NUM_COLUMN_URL = 4;
	    public static final String COLUMN_DATE = "DATE";
	    public static final int NUM_COLUMN_DATE = 5;
	    public static final String COLUMN_IMG = "IMAGE";
	    public static final int NUM_COLUMN_IMG = 6;
	    public static final String COLUMN_IMAGE_URL = "IMAGE_URL";
	    public static final int NUM_COLUMN_IMAGE_URL = 7;
    	
    }

    public List<ActuVO> getAll() {
    	openReadable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	ArrayList<ActuVO> list = null;
    	Cursor cursor = hofcDatabase.query(ActusEntry.ACTUS_TABLE_NAME, null, null, null, null, null, ActusEntry.COLUMN_DATE + " DESC");
    	if(cursor.getCount() > 0){
    		list = new ArrayList<>();
    		while(cursor.moveToNext()) {
    			ActuVO line = new ActuVO();
    			line.setTitre(cursor.getString(ActusEntry.NUM_COLUMN_TITLE));
    			line.setTexte(cursor.getString(ActusEntry.NUM_COLUMN_DESCRIPTION));
    			line.setUrl(cursor.getString(ActusEntry.NUM_COLUMN_URL));
    			line.setPostId(cursor.getInt(ActusEntry.NUM_COLUMN_POST_ID));
    			line.setImageUrl(cursor.getString(ActusEntry.NUM_COLUMN_IMAGE_URL));
    			try {
    				if(cursor.getString(ActusEntry.NUM_COLUMN_DATE) != null)
    					line.setDate(sdf.parse(cursor.getString(ActusEntry.NUM_COLUMN_DATE)));
				} catch (ParseException e) {
					Log.e("HOFC", "Problem when parsing date", e);
				}
    			byte[] bb = cursor.getBlob(ActusEntry.NUM_COLUMN_IMG);
                if(bb != null)
    			    line.setBitmapImage(BitmapFactory.decodeByteArray(bb, 0, bb.length));
    			list.add(line);
        	}
    	}
        cursor.close();
    	return list;
    }

	@Override
    public void insertList(List<ActuVO> list) {
    	openWritable();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    	for(ActuVO line : list) {
    		Cursor cursor = hofcDatabase.query(ActusEntry.ACTUS_TABLE_NAME, null, ActusEntry.COLUMN_POST_ID + " ="+ line.getPostId(), null, null, null, null);
			ContentValues values = new ContentValues();
			values.put(ActusEntry.COLUMN_DATE, sdf.format(line.getDate()));
			values.put(ActusEntry.COLUMN_DESCRIPTION, line.getTexte());
			values.put(ActusEntry.COLUMN_TITLE, line.getTitre());
			values.put(ActusEntry.COLUMN_URL, line.getUrl());
			values.put(ActusEntry.COLUMN_IMAGE_URL, line.getImageUrl());
			if(line.getBitmapImage() != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				line.getBitmapImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
				values.put(ActusEntry.COLUMN_IMG, stream.toByteArray());
			}
    		if(cursor.getCount() > 0) {
    			// UPDATE
    			hofcDatabase.update(ActusEntry.ACTUS_TABLE_NAME, values, ActusEntry.COLUMN_POST_ID + " ='"+ line.getPostId() + "'", null);
    		} else {
    			// INSERT
    			values.put(ActusEntry.COLUMN_POST_ID, line.getPostId());
    			hofcDatabase.insert(ActusEntry.ACTUS_TABLE_NAME, null, values);
    		}
            cursor.close();
    	}

    }
}
