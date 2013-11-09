package com.sebasta2.expensedairy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "EXPENSES";
	public static Context context;
	public static SharedPreferences sharedprefs;
	public static Editor editor;
	public static String[] expenses = {"electricity","groceries","insurance","internet",
									   "leisure","loans","other","rent","telephone",
									   "transport","utilities"};
	
	private String command = CreateCommand();		
		
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+ DATABASE_NAME + " (id INTEGER PRIMARY KEY, date TEXT, "+command+")");		
	}
		
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
		
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Log.w(TAG, "Upgrading database, this will drop tables and recreate.");
		db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME);	
	}		
		
	//method that generate database CREATE statement according to the items picked
	public static String CreateCommand() {
		String command = "";
		for(String s: expenses) {
			command += s+" FLOAT(10,2), "+s+"C TEXT, "+s+"R LONGBLOB,"; //Receipt LONGBLOB
		}
		
		command = command.substring(0, command.lastIndexOf(","));
		Log.i("Database", command);		
		return command;
	}

}