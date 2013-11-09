package com.sebasta2.expensedairy;

import java.util.Vector;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SearchResultsPage extends ListActivity {

	Button back;
	SQLiteDatabase db;
	Cursor cursor;
	private static final String Search_All = "SELECT * FROM EXPENSES";
	
	Vector<Result> datavector;
	Result[] results;
	ListView listview;
	Bitmap sample;
	String date = "";
	String start="";
	String finish ="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_results_layout);
		
		sample = BitmapFactory.decodeFile("raw/receipt.png");
		
		datavector = new Vector<Result>();
				
		db = (new Database(this).getReadableDatabase());
		
		back = (Button)findViewById(R.id.goback);
		
		Intent sender = getIntent();
		String searchtype = sender.getStringExtra("DATE");
		
		Log.i("Search", "action received: "+searchtype);
	
		String[] temp = searchtype.split("==");
		
		
		//reading from the database according to search type
		if(temp[0].equals("All")) {
			cursor = db.rawQuery(Search_All, null);
			readCursor(cursor);
		}
		if(temp[0].equals("Single")) {
			date = temp[1];
			Log.i("Search", "single date received");
			cursor=null;
			cursor = db.rawQuery("SELECT * FROM EXPENSES WHERE date LIKE '"+date+"'", null);
			readCursor(cursor);
		}
		if(temp[0].equals("Double")) {
			start = temp[1];finish = temp[2];
			cursor = null;
			cursor = db.rawQuery("SELECT id FROM EXPENSES WHERE date LIKE '"+start+"'", null);
			cursor.moveToFirst();
			int id1 = cursor.getInt(0);
			
			cursor = null;
			cursor = db.rawQuery("SELECT id FROM EXPENSES WHERE date LIKE '"+finish+"'", null);
			cursor.moveToFirst();
			int id2 = cursor.getInt(0);
			
			cursor = null;
			cursor = db.rawQuery("SELECT * FROM EXPENSES WHERE id >= "+id1+" AND date <= "+id2+";", null);
			readCursor(cursor);
		}
			Log.i("Search", "reading cursor "+cursor.getColumnCount());
		
		
		setListAdapter(new ResultAdapter(this, results));
		Log.i("Search", "setting adapter");
	}
	
	public void readCursor(Cursor cursor) {
		
		//reading from cursor
		while(cursor.moveToNext()) {
			String date = cursor.getString(1);
			Result row = new Result(date, ""+cursor.getString(2),  ""+cursor.getString(3),  sample, 
										  ""+cursor.getString(5),  ""+cursor.getString(6),  sample, 
										  ""+cursor.getString(8),  ""+cursor.getString(9),  sample, 
										  ""+cursor.getString(11), ""+cursor.getString(12), sample, 
										  ""+cursor.getString(14), ""+cursor.getString(15), sample, 
										  ""+cursor.getString(17), ""+cursor.getString(18), sample, 
										  ""+cursor.getString(20), ""+cursor.getString(21), sample, 
										  ""+cursor.getString(23), ""+cursor.getString(24), sample, 
										  ""+cursor.getString(26), ""+cursor.getString(27), sample, 
										  ""+cursor.getString(29), ""+cursor.getString(30), sample, 
										  ""+cursor.getString(32), ""+cursor.getString(33), sample);
			datavector.add(row);
			Log.i("Search", "Row added:"+row.elecamount);
			Log.i("Search", "Row added:"+row.grocamount);
			
		}
		results = Vectortoarray(datavector);
	}
	
	public Result[] Vectortoarray(Vector<Result> vector) {
		Result[] r = new Result[vector.size()];
		
		for(int i=0; i<vector.size(); i++){
			r[i] = vector.get(i);
		}
		return r;
	}
	

	//handling click events from the listview
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		Intent in = new Intent(SearchResultsPage.this, ReceiptGalleryPage.class);
		in.putExtra("date",results[position].date);
		startActivity(in);
	}	
	
	public void backaction(View view) {
		if(view == back) {
			finish();
		}
	}
	

}
