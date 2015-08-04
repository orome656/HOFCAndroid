package com.hofc.hofc.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by antho on 04/08/15.
 */
public class ParamsBDD extends CommonBDD<Params> {

    public ParamsBDD(Context c) {
        super(c);
        this.tableName = ParamsEntry.TABLE_NAME;
    }
    private static abstract class ParamsEntry implements BaseColumns {
        public static final String TABLE_NAME = "params";
        public static final String COLUMN_NOM = "NOM";
        public static final int NUM_COLUMN_NOM = 0;
        public static final String COLUMN_VALEUR = "VALEUR";
        public static final int NUM_COLUMN_VALEUR = 1;
    }


    public Params getParams() {
        openReadable();
        Cursor cursor = hofcDatabase.query(ParamsEntry.TABLE_NAME, null, ParamsEntry.COLUMN_NOM +"=?" , new String[]{"seasonMatchCount"}, null, null, null);
        Params result = new Params();
        if(cursor.moveToNext()) {
            int seasonMatchCount = cursor.getInt(ParamsEntry.NUM_COLUMN_VALEUR);
            result.setSeasonMatchCount(seasonMatchCount);
        }
        cursor.close();
        return new Params();
    }

    public void saveParam(String name, String value) {
        openWritable();
        Cursor cursor = hofcDatabase.query(ParamsEntry.TABLE_NAME, null, ParamsEntry.COLUMN_NOM + " =?", new String[]{name}, null, null, null);
        ContentValues values = new ContentValues();
        values.put(ParamsEntry.COLUMN_VALEUR, value);
        if(cursor.getCount() > 0) {
            // UPDATE
            hofcDatabase.update(ParamsEntry.TABLE_NAME, values, ParamsEntry.COLUMN_NOM + " =?", new String[]{name});
        } else {
            // INSERT
            values.put(ParamsEntry.COLUMN_NOM, name);
            hofcDatabase.insert(ParamsEntry.TABLE_NAME, null, values);
        }
    }

}
