package com.hofc.hofc.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class HOFCOpenHelper extends SQLiteOpenHelper {
	   // Version de la base de données
    private static final int DATABASE_VERSION = 1;
 
    // Nom de la base
    private static final String HOFC_BASE_NAME = "hofc.db";
 
    // Nom de la table
    public static final String CLASSEMENT_TABLE_NAME = "classement";
    public static final String CALENDRIER_TABLE_NAME = "calendrier";
    public static final String ACTUS_TABLE_NAME = "actus";
    public static final String DATE_SYNCHRO_TABLE_NAME= "date_synchro";

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
 
    // Description des colonnes actus
    public static final String COLUMN_TITLE = "TITRE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_DATE_ACTUS = "DATE";
    public static final String COLUMN_IMG = "IMAGE";
    public static final String COLUMN_POST_ID = "POST_ID";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_IMAGE_URL = "IMAGE_URL";
    
    // Synchro
    public static final String COLUMN_NOM_SYNCHRO = "NOM";
    public static final int NUM_COLUMN_NOM_SYNCHRO = 1;
    public static final String COLUMN_DATE = "date";
    public static final int NUM_DATE = 2;
    
    
    // Requête SQL pour la création da la base Calendrier
    private static final String REQUETE_CREATION_BDD_CLASSEMENT = "CREATE TABLE IF NOT EXISTS "
            + CLASSEMENT_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOM
            + " TEXT NOT NULL, " + COLUMN_POINTS + " INTEGER, "
            + COLUMN_JOUE + " INTEGER, "
            + COLUMN_GAGNE + " INTEGER, " + COLUMN_NUL + " INTEGER, "
            + COLUMN_PERDU + " INTEGER, " + COLUMN_BP + " INTEGER, "
            + COLUMN_BC + " INTEGER, " + COLUMN_DIFF + " INTEGER);";
    

    // Requête SQL pour la création da la base CLASSEMENT
    private static final String REQUETE_CREATION_BDD_CALENDRIER = "CREATE TABLE IF NOT EXISTS "
            + CALENDRIER_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EQUIPE_1
            + " TEXT NOT NULL, " + COLUMN_SCORE_1 + " INTEGER, "
            + COLUMN_SCORE_2 + " INTEGER, "
            + COLUMN_EQUIPE_2 + " TEXT NOT NULL, "
            + COLUMN_DATE + " DATE );";
    
    private static final String REQUETE_CREATION_BDD_DATE = "CREATE TABLE IF NOT EXISTS "
    		+ DATE_SYNCHRO_TABLE_NAME + "(" + COLUMN_ID
    		+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOM_SYNCHRO
    		+ " TEXT NOT NULL, " + COLUMN_DATE + " DATE)";
    
    private static final String REQUETE_CREATION_BDD_ACTUS = "CREATE TABLE IF NOT EXISTS "
            + ACTUS_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE
            + " TEXT NOT NULL, " + COLUMN_POST_ID + " INTEGER, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_URL + " TEXT, "
            + COLUMN_DATE_ACTUS + " DATE, "
            + COLUMN_IMG + " BLOB, "
            + COLUMN_IMAGE_URL + " TEXT);";
    
	public HOFCOpenHelper(Context context, CursorFactory factory) {
		super(context, HOFC_BASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REQUETE_CREATION_BDD_CALENDRIER);
		db.execSQL(REQUETE_CREATION_BDD_CLASSEMENT);
		db.execSQL(REQUETE_CREATION_BDD_ACTUS);
		db.execSQL(REQUETE_CREATION_BDD_DATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CALENDRIER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CLASSEMENT_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ACTUS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DATE_SYNCHRO_TABLE_NAME);
		onCreate(db);

	}

}
