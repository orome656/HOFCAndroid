package com.hofc.hofc.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.hofc.hofc.vo.AgendaLineVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anthony on 02/08/2015.
 */
public class AgendaBDD extends CommonBDD<AgendaLineVO> {

    public AgendaBDD(Context c) {
        super(c);
        this.tableName = AgendaEntry.AGENDA_TABLE_NAME;
    }
    private static abstract class AgendaEntry implements BaseColumns {
        //public static final String COLUMN_ID = "ID";
        //public static final int NUM_COLUMN_ID = 0;
        public static final String AGENDA_TABLE_NAME = "agenda";
        public static final String COLUMN_TITLE = "TITRE";
        public static final int NUM_COLUMN_TITLE = 1;
        public static final String COLUMN_EQUIPE_1 = "EQUIPE_1";
        public static final int NUM_COLUMN_EQUIPE_1 = 2;
        public static final String COLUMN_SCORE_1 = "SCORE_1";
        public static final int NUM_COLUMN_SCORE_1 = 3;
        public static final String COLUMN_SCORE_2 = "SCORE_2";
        public static final int NUM_COLUMN_SCORE_2 = 4;
        public static final String COLUMN_EQUIPE_2 = "EQUIPE_2";
        public static final int NUM_COLUMN_EQUIPE_2 = 5;
        public static final String COLUMN_DATE = "DATE";
        public static final int NUM_COLUMN_DATE = 6;
        public static final String COLUMN_IDENTIFIANT_DATE = "ID_DATE";
        public static final int NUM_COLUMN_IDENTIFIANT_DATE = 7;
        public static final String COLUMN_IDENTIFIANT_INFOS = "ID_INFOS";
        public static final int NUM_COLUMN_IDENTIFIANT_INFOS = 8;

    }

    public List<AgendaLineVO> getAll(String date) {
        openReadable();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        ArrayList<AgendaLineVO> list = null;
        Cursor cursor = hofcDatabase.query(AgendaEntry.AGENDA_TABLE_NAME, null, AgendaEntry.COLUMN_IDENTIFIANT_DATE + "=?", new String[]{date}, null, null, AgendaEntry.COLUMN_DATE);
        if(cursor.getCount() > 0){
            list = new ArrayList<>();
            while(cursor.moveToNext()) {
                AgendaLineVO line = new AgendaLineVO();
                line.setTitle(cursor.getString(AgendaEntry.NUM_COLUMN_TITLE));
                line.setEquipe1(cursor.getString(AgendaEntry.NUM_COLUMN_EQUIPE_1));
                line.setEquipe2(cursor.getString(AgendaEntry.NUM_COLUMN_EQUIPE_2));
                line.setIdInfos(cursor.getString(AgendaEntry.NUM_COLUMN_IDENTIFIANT_INFOS));
                if(!cursor.isNull(AgendaEntry.NUM_COLUMN_SCORE_1)) {
                    line.setScore1(cursor.getInt(AgendaEntry.NUM_COLUMN_SCORE_1));
                    line.setScore2(cursor.getInt(AgendaEntry.NUM_COLUMN_SCORE_2));
                } else {
                    line.setScore1(null);
                    line.setScore2(null);
                }
                try {
                    if(cursor.getString(AgendaEntry.NUM_COLUMN_DATE) != null)
                        line.setDate(sdf.parse(cursor.getString(AgendaEntry.NUM_COLUMN_DATE)));
                } catch (ParseException e) {
                    Log.e("HOFC", e.getMessage());
                }
                list.add(line);
            }
        }
        cursor.close();
        return list;
    }


    public void insertList(String date, List<AgendaLineVO> list) {
        openWritable();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        // On supprime avant d'insérer pour mettre a jour les données si il y a eu des suppressions
        hofcDatabase.delete(AgendaEntry.AGENDA_TABLE_NAME, null, null);
        for(AgendaLineVO line : list) {
            Cursor cursor = hofcDatabase.query(AgendaEntry.AGENDA_TABLE_NAME, null, AgendaEntry.COLUMN_EQUIPE_1 + " ='"+ line.getEquipe1() +"' and " + AgendaEntry.COLUMN_EQUIPE_2 + " ='"+line.getEquipe2()+"'", null, null, null, null);
            ContentValues values = new ContentValues();
            values.put(AgendaEntry.COLUMN_SCORE_1, line.getScore1());
            values.put(AgendaEntry.COLUMN_SCORE_2, line.getScore2());
            values.put(AgendaEntry.COLUMN_TITLE, line.getTitle());
            values.put(AgendaEntry.COLUMN_IDENTIFIANT_INFOS, line.getIdInfos());
            values.put(AgendaEntry.COLUMN_IDENTIFIANT_DATE, date);
            if(line.getDate() != null) {
                values.put(AgendaEntry.COLUMN_DATE, sdf.format(line.getDate()));
            }
            if(cursor.getCount() > 0) {
                // UPDATE
                hofcDatabase.update(AgendaEntry.AGENDA_TABLE_NAME, values, AgendaEntry.COLUMN_EQUIPE_1 + " ='"+ line.getEquipe1() +"' and " + AgendaEntry.COLUMN_EQUIPE_2 + " ='"+line.getEquipe2()+"' and " + AgendaEntry.COLUMN_IDENTIFIANT_DATE + " ='" + date + "'", null);
            } else {
                // INSERT
                values.put(AgendaEntry.COLUMN_EQUIPE_1, line.getEquipe1());
                values.put(AgendaEntry.COLUMN_EQUIPE_2, line.getEquipe2());
                hofcDatabase.insert(AgendaEntry.AGENDA_TABLE_NAME, null, values);
            }
            cursor.close();
        }
    }

}
