package com.hofc.hofc.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

class HOFCOpenHelper extends SQLiteOpenHelper {
	   // Version de la base de données
    private static final int DATABASE_VERSION = 3;
 
    // Nom de la base
    private static final String HOFC_BASE_NAME = "hofc.db";
 
    // Nom de la table
    private static final String CLASSEMENT_TABLE_NAME = "classement";
    private static final String CALENDRIER_TABLE_NAME = "calendrier";
    private static final String ACTUS_TABLE_NAME = "actus";
    private static final String AGENDA_TABLE_NAME = "agenda";
    private static final String DATE_SYNCHRO_TABLE_NAME= "date_synchro";
    private static final String JOURNEE_TABLE_NAME= "journee";
    private static final String PARAMS_TABLE_NAME= "params";

    // Description des colonnes commun
    private static final String COLUMN_ID = "ID";
    //public static final int NUM_COLUMN_ID = 0;
    
    // Description des colonnes Classement
    private static final String COLUMN_NOM = "NOM";
    private static final String COLUMN_POINTS = "POINTS";
    private static final String COLUMN_JOUE = "JOUE";
    private static final String COLUMN_GAGNE = "GAGNE";
    private static final String COLUMN_NUL = "NUL";
    private static final String COLUMN_PERDU = "PERDU";
    private static final String COLUMN_BP = "BP";
    private static final String COLUMN_BC = "BC";
    private static final String COLUMN_DIFF = "DIFF";
 
    // Description des colonnes calendrier
    private static final String COLUMN_EQUIPE_1 = "EQUIPE_1";
    //public static final int NUM_COLUMN_EQUIPE_1 = 1;
    private static final String COLUMN_SCORE_1 = "SCORE_1";
    //public static final int NUM_COLUMN_SCORE_1 = 2;
    private static final String COLUMN_SCORE_2 = "SCORE_2";
    //public static final int NUM_COLUMN_SCORE_2 = 3;
    private static final String COLUMN_EQUIPE_2 = "EQUIPE_2";
    //public static final int NUM_COLUMN_EQUIPE_2 = 4;
 
    // Description des colonnes actus
    private static final String COLUMN_TITLE = "TITRE";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_DATE_ACTUS = "DATE";
    private static final String COLUMN_IMG = "IMAGE";
    private static final String COLUMN_POST_ID = "POST_ID";
    private static final String COLUMN_URL = "URL";
    private static final String COLUMN_IMAGE_URL = "IMAGE_URL";

    // Description des colonnes agenda
    private static final String COLUMN_ID_INFORMATION = "ID_INFOS";
    private static final String COLUMN_ID_DATE = "ID_DATE";

    // Description des colonnes journee
    private static final String COLUMN_ID_JOURNEE = "ID_JOURNEE";

    // Description des colonnes params
    private static final String COLUMN_VALEUR = "VALEUR";

    // Synchro
    private static final String COLUMN_NOM_SYNCHRO = "NOM";
    //public static final int NUM_COLUMN_NOM_SYNCHRO = 1;
    private static final String COLUMN_DATE = "date";
    //public static final int NUM_DATE = 2;
    
    
    // Requéte SQL pour la création da la base Calendrier
    private static final String REQUETE_CREATION_BDD_CLASSEMENT = "CREATE TABLE IF NOT EXISTS "
            + CLASSEMENT_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOM
            + " TEXT NOT NULL, " + COLUMN_POINTS + " INTEGER, "
            + COLUMN_JOUE + " INTEGER, "
            + COLUMN_GAGNE + " INTEGER, " + COLUMN_NUL + " INTEGER, "
            + COLUMN_PERDU + " INTEGER, " + COLUMN_BP + " INTEGER, "
            + COLUMN_BC + " INTEGER, " + COLUMN_DIFF + " INTEGER);";
    

    // Requéte SQL pour la création da la base CLASSEMENT
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

    private static final String REQUETE_CREATION_BDD_AGENDA = "CREATE TABLE IF NOT EXISTS "
            + AGENDA_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EQUIPE_1
            + " TEXT NOT NULL, " + COLUMN_SCORE_1 + " INTEGER, "
            + COLUMN_SCORE_2 + " INTEGER, "
            + COLUMN_EQUIPE_2 + " TEXT NOT NULL, "
            + COLUMN_DATE + " DATE, "+ COLUMN_ID_INFORMATION + " TEXT, "
            + COLUMN_ID_DATE + " TEXT);";

    private static final String REQUETE_CREATION_BDD_JOURNEE = "CREATE TABLE IF NOT EXISTS "
            + JOURNEE_TABLE_NAME + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EQUIPE_1
            + " TEXT NOT NULL, " + COLUMN_SCORE_1 + " INTEGER, "
            + COLUMN_SCORE_2 + " INTEGER, "
            + COLUMN_EQUIPE_2 + " TEXT NOT NULL, "
            + COLUMN_DATE + " DATE, "+ COLUMN_ID_JOURNEE + " TEXT, "
            + COLUMN_ID_INFORMATION + " TEXT);";


    private static final String REQUETE_CREATION_BDD_PARAMS = "CREATE TABLE IF NOT EXISTS "
            + PARAMS_TABLE_NAME + " (" + COLUMN_NOM
            + " TEXT PRIMARY KEY , " + COLUMN_VALEUR
            + " TEXT NOT NULL);";

    public HOFCOpenHelper(Context context, CursorFactory factory) {
		super(context, HOFC_BASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(REQUETE_CREATION_BDD_CALENDRIER);
		db.execSQL(REQUETE_CREATION_BDD_CLASSEMENT);
		db.execSQL(REQUETE_CREATION_BDD_ACTUS);
		db.execSQL(REQUETE_CREATION_BDD_DATE);
        db.execSQL(REQUETE_CREATION_BDD_AGENDA);
        db.execSQL(REQUETE_CREATION_BDD_JOURNEE);
        db.execSQL(REQUETE_CREATION_BDD_PARAMS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CALENDRIER_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CLASSEMENT_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + ACTUS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DATE_SYNCHRO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AGENDA_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + JOURNEE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARAMS_TABLE_NAME);
		onCreate(db);

	}

}
