package com.hofc.hofc.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class CommonBDD<T> {

	private Context context;
	protected HOFCOpenHelper hofcOpenHelper;
	protected SQLiteDatabase hofcDatabase;
    protected String tableName;
	protected String whereCondition;
	protected String orderByValue;


	public CommonBDD() {}

	public CommonBDD(Context c) {
		if(hofcOpenHelper == null)
			hofcOpenHelper = new HOFCOpenHelper(c, null);

		this.context = c;
	}

	protected void openReadable() {
		if(hofcOpenHelper == null)
			hofcOpenHelper = new HOFCOpenHelper(context, null);

		if(hofcDatabase == null)
			hofcDatabase = hofcOpenHelper.getReadableDatabase();
	}

	protected void openWritable() throws SQLException {
		if(hofcOpenHelper == null)
			hofcOpenHelper = new HOFCOpenHelper(context, null);

		if ((hofcDatabase == null)|| hofcDatabase.isReadOnly()) {
			openWritable(true);
		}
	}

	/**
	 * Opens the database for writing
	 * @param foreignKeys State of Foreign Keys Constraint, true = ON, false = OFF
	 * @throws SQLException if the database cannot be opened for writing
	 */
	private void openWritable(boolean foreignKeys) throws SQLException{
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
	public void close() {
		if (hofcDatabase != null){
			hofcDatabase.close();
			hofcDatabase = null;
		}
		if (hofcOpenHelper != null){
			hofcOpenHelper.close();
			hofcOpenHelper = null;
		}
	}

	public Date getDateSynchro() {
        openReadable();
		Date result = null;
		Cursor cursor = hofcDatabase.query("date_synchro", null, "nom='"+tableName+"'", null, null, null, null);
		if(cursor.moveToFirst()){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				result = sdf.parse(cursor.getString(cursor.getColumnIndex("date")));
			} catch (ParseException e) {
                Log.e(CommonBDD.class.getName(),"Error while parsing sync date", e);
            }
            cursor.close();
			return result;
		} else {
            cursor.close();
			return null;
		}
	}
	
	public void updateDateSynchro(Date date) {
        openWritable();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Cursor cursor = hofcDatabase.query("date_synchro", null, "nom='"+tableName+"'", null, null, null, null);
		ContentValues values = new ContentValues();
		values.put("date", sdf.format(date));
		if(cursor.getCount() > 0) {
			hofcDatabase.update("date_synchro", values, "nom='" + tableName + "'", null);
		} else {
			values.put("nom", tableName);
			hofcDatabase.insert("date_synchro", null, values);
		}
        cursor.close();
	}

	public List<T> getAll(){
		return null;
	}

	public List<T> getWithKey(String key){
		return null;
	}

	public void insertElement(T element){}

	public void insertListWithKey(String key, List<T> list){}

	public void insertList(List<T> list){}
}
