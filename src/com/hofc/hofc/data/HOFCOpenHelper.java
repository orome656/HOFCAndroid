package com.hofc.hofc.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HOFCOpenHelper extends SQLiteOpenHelper {
	   // Version de la base de donn�es
    private static final int DATABASE_VERSION = 1;
 
    // Nom de la base
    private static final String HOFC_BASE_NAME = "hofc.db";
 
    // Nom de la table
    public static final String CLASSEMENT_TABLE_NAME = "classement";
    public static final String CALENDRIER_TABLE_NAME = "calendrier";

    // Description des colonnes commun
    public static final String COLUMN_ID = "ID";
    public static final int NUM_COLUMN_ID = 0;
    
    // Description des colonnes Classement
    public static final String COLUMN_NOM = "NOM";
    public static final String COLUMN_POINTS = "POINTS";
    public static final String COLUMN_JOUE = "JOUE";
    public static final String COLUMN_GAGNE = "GAGNE";
    public static final String COLUMN_NUL = "NUL";
    public static final String COLUMN_PERDU = "PERDU";
    public static final String COLUMN_BP = "BP";
    public static final String COLUMN_BC = "BC";
    public static final String COLUMN_DIFF = "DIFF";
 
    // Description des colonnes calendrier
    public static final String COLUMN_EQUIPE_1 = "EQUIPE_1";
    public static final int NUM_COLUMN_EQUIPE_1 = 1;
    public static final String COLUMN_SCORE_1 = "SCORE_1";
    public static final int NUM_COLUMN_SCORE_1 = 2;
    public static final String COLUMN_SCORE_2 = "SCORE_2";
    public static final int NUM_COLUMN_SCORE_2 = 3;
    public static final String COLUMN_EQUIPE_2 = "EQUIPE_2";
    public static final int NUM_COLUMN_EQUIPE_2 = 4;
 
    
    // Requ�te SQL pour la cr�ation da la base Calendrier
    private static final String REQUETE_CREATION_BDD_CALENDRIER = "CREATE TABLE "
            + CALENDRIER_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOM
            + " TEXT NOT NULL, " + COLUMN_POINTS + "INTEGER, "
            + COLUMN_JOUE + " INTEGER, " + COLUMN_POINTS + "INTEGER, "
            + COLUMN_GAGNE + " INTEGER, " + COLUMN_NUL + "INTEGER, "
            + COLUMN_PERDU + " INTEGER, " + COLUMN_BP + "INTEGER, "
            + COLUMN_BC + " INTEGER, " + COLUMN_DIFF + "INTEGER);";
    

    // Requ�te SQL pour la cr�ation da la base CLASSEMENT
    private static final String REQUETE_CREATION_BDD_CLASSEMENT = "CREATE TABLE "
            + CLASSEMENT_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EQUIPE_1
            + " TEXT NOT NULL, " + COLUMN_SCORE_1 + " INTEGER, "
            + COLUMN_SCORE_2 + " INTEGER, "
            + COLUMN_EQUIPE_2 + " TEXT NOT NULL);";
    
	public HOFCOpenHelper(Context context, CursorFactory factory) {
		super(context, HOFC_BASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REQUETE_CREATION_BDD_CALENDRIER);
		db.execSQL(REQUETE_CREATION_BDD_CLASSEMENT);
		onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CALENDRIER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CLASSEMENT_TABLE_NAME);

	}

}
