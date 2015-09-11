package com.hofc.hofc.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import com.hofc.hofc.vo.AgendaLineVO;
import com.hofc.hofc.vo.MatchVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by antho on 03/08/15.
 */
public class JourneeBdd extends CommonBDD<MatchVO> {

    public JourneeBdd(Context c) {
        super(c);
        this.tableName = JourneeEntry.JOURNEE_TABLE_NAME;
    }
    private static abstract class JourneeEntry implements BaseColumns {
        //public static final String COLUMN_ID = "ID";
        //public static final int NUM_COLUMN_ID = 0;
        public static final String JOURNEE_TABLE_NAME = "journee";
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
        public static final String COLUMN_IDENTIFIANT_JOURNEE = "ID_JOURNEE";
        public static final int NUM_COLUMN_IDENTIFIANT_JOURNEE = 6;
        public static final String COLUMN_IDENTIFIANT_INFOS = "ID_INFOS";
        public static final int NUM_COLUMN_IDENTIFIANT_INFOS = 7;
        public static final String COLUMN_CATEGORIE = "CATEGORIE";
        public static final int NUM_COLUMN_CATEGORIE = 8;

    }

    public List<MatchVO> getAll(String idJournee, String equipe) {
        openReadable();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        ArrayList<MatchVO> list = null;
        Cursor cursor = hofcDatabase.query(JourneeEntry.JOURNEE_TABLE_NAME, null, JourneeEntry.COLUMN_IDENTIFIANT_JOURNEE + "=? and " + JourneeEntry.COLUMN_CATEGORIE + "=?", new String[]{idJournee, equipe}, null, null, JourneeEntry.COLUMN_DATE);
        if(cursor.getCount() > 0){
            list = new ArrayList<>();
            while(cursor.moveToNext()) {
                AgendaLineVO line = new AgendaLineVO();
                line.setEquipe1(cursor.getString(JourneeEntry.NUM_COLUMN_EQUIPE_1));
                line.setEquipe2(cursor.getString(JourneeEntry.NUM_COLUMN_EQUIPE_2));
                line.setIdInfos(cursor.getString(JourneeEntry.NUM_COLUMN_IDENTIFIANT_INFOS));
                if(!cursor.isNull(JourneeEntry.NUM_COLUMN_SCORE_1)) {
                    line.setScore1(cursor.getInt(JourneeEntry.NUM_COLUMN_SCORE_1));
                    line.setScore2(cursor.getInt(JourneeEntry.NUM_COLUMN_SCORE_2));
                } else {
                    line.setScore1(null);
                    line.setScore2(null);
                }
                try {
                    if(cursor.getString(JourneeEntry.NUM_COLUMN_DATE) != null)
                        line.setDate(sdf.parse(cursor.getString(JourneeEntry.NUM_COLUMN_DATE)));
                } catch (ParseException e) {
                    Log.e("HOFC", e.getMessage());
                }
                list.add(line);
            }
        }
        cursor.close();
        return list;
    }


    public void insertList(String idJournee, String equipe, List<MatchVO> list) {
        openWritable();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        // On supprime avant d'insérer pour mettre a jour les données si il y a eu des suppressions
        hofcDatabase.delete(JourneeEntry.JOURNEE_TABLE_NAME, JourneeEntry.COLUMN_IDENTIFIANT_JOURNEE + "=? and " + JourneeEntry.COLUMN_CATEGORIE + "=?", new String[]{idJournee, equipe});
        for(MatchVO line : list) {
            ContentValues values = new ContentValues();
            values.put(JourneeEntry.COLUMN_SCORE_1, line.getScore1());
            values.put(JourneeEntry.COLUMN_SCORE_2, line.getScore2());
            values.put(JourneeEntry.COLUMN_IDENTIFIANT_INFOS, line.getIdInfos());
            values.put(JourneeEntry.COLUMN_IDENTIFIANT_JOURNEE, idJournee);
            if(line.getDate() != null) {
                values.put(JourneeEntry.COLUMN_DATE, sdf.format(line.getDate()));
            }
            // INSERT
            values.put(JourneeEntry.COLUMN_EQUIPE_1, line.getEquipe1());
            values.put(JourneeEntry.COLUMN_EQUIPE_2, line.getEquipe2());
            values.put(JourneeEntry.COLUMN_CATEGORIE, equipe);
            hofcDatabase.insert(JourneeEntry.JOURNEE_TABLE_NAME, null, values);
        }
    }
}
