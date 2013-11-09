package com.sebasta2.expensedairy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class GraphPage extends Activity implements OnItemSelectedListener{

	Button graphbutton;
	Spinner selectdate;
	TextView displaydate;
	private String singledate = "";
	private String startdate = "";
	private String finishdate = "";
	private List<String> datelist;
	String values="";
	private static String DATE = null;
	private String[] dates = {"          Dates          ","Single Date","Date from","Date to"};
	float[] plotvalues;
	int[] columns = {2,5,8,11,14,17,20,23,29,32};
	ArrayAdapter<String> dataAdapter;
	DatePickerDialog datedialog;
	Toast toast;
	SQLiteDatabase db;
	Cursor cursor;
	SharedPreferences sharedprefs = null;
    Editor editor;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_page_layout);
		
		graphbutton = (Button)findViewById(R.id.graphbutton);
		selectdate = (Spinner)findViewById(R.id.dates);
		displaydate = (TextView)findViewById(R.id.selecteddate);
		
		datelist = new ArrayList<String>();
		
		populatelist(datelist, dates);
		
		selectdate.setOnItemSelectedListener(this);
		
		db = (new Database(this)).getReadableDatabase();
			
		addItemsOnSpinner(); //adds the categories to the spinner
	
	}	
	
	protected void onPause() {
		super.onPause();
	}
	
	//method that adds items to the date spinner
	public void addItemsOnSpinner() {		 
		dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, datelist);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectdate.setAdapter(dataAdapter);
	}
		
	//a method that objects add the string array to the list
	public void populatelist(List<String> list, String[] items) {
		for(String s: items) {
			list.add(s);
		}
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int id,long arg3) {
		if(parent.getAdapter().equals(dataAdapter)) {
			DATE = datelist.get(id); //lets date equals to the item selected 
			if(id==0) {
				toast = Toast.makeText(getApplicationContext(),"Select a date from below...", Toast.LENGTH_SHORT); toast.show();
			}
			else {	showdatedialog();	}
		}
	}
	
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	@SuppressWarnings("deprecation")
	public void graphaction(View view) {
		if(view == graphbutton) {
			Intent in = new Intent(GraphPage.this, GraphResultPage.class);
			if(singledate.equals("") && startdate.equals("") && finishdate.equals("")) {
				toast = Toast.makeText(getApplicationContext(),"Select atleast a single date to graph...", Toast.LENGTH_SHORT); toast.show();
			}
			else if(!singledate.equals("")) {
				String date = singledate.split("/")[2]+"-"+singledate.split("/")[1]+"-"+singledate.split("/")[0];
				Log.i("Graph", ""+date);
				cursor = db.rawQuery("SELECT * FROM EXPENSES WHERE date LIKE '"+date+"'", null);
				cursor.moveToFirst();
				
				//cursor.moveToFirst();
				for(int i=0; i<columns.length; i++) {
					values+=cursor.getString(columns[i])+"==";
				}
				sharedprefs = this.getApplicationContext().getSharedPreferences("DATACOLUMNS",MODE_WORLD_READABLE);
				editor = sharedprefs.edit();
		        editor.putString("VALUES",values);
		        editor.commit();
				startActivity(in);
				cursor = null;
			}
			/*if(singledate.equals("") && !startdate.equals("") && !finishdate.equals("")) {
				String date1 = startdate.split("/")[2]+"-"+startdate.split("/")[1]+"-"+startdate.split("/")[0];
				String date2 = finishdate.split("/")[2]+"-"+finishdate.split("/")[1]+"-"+finishdate.split("/")[0];
				in.putExtra("DATE", "Double=="+date1+"=="+date2);
				startActivity(in);
			}
			
		
			
			
			if(singledate.length()>0) {
				cursor = db.rawQuery("SELECT * FROM EXPENSES WHERE date='"+finishdate+"'", null);
				Log.i("Insert", "Cursor Row count: "+cursor.getCount());
				cursor.moveToFirst();
				
				for(int i=0; i<columns.length;i++) {
					 values+=cursor.getString(columns[i]);
				}
				
				Log.i("Graph", "plot values: "+values);
				//writing share preferences for graphing
				sharedprefs = this.getApplicationContext().getSharedPreferences("PLOTVALUES",MODE_WORLD_READABLE);
				editor = sharedprefs.edit();
		        editor.putString("VALUES","162.36f--13.45f--100--38.62f--100--300--10--900--50--39--100");
		        editor.commit();
		        Log.i("Graph", "saved in share preferences: "+values);
			}
			
		}*/
	}
		}
	
	public void showdatedialog() {
		datedialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
				Time chosenDate = new Time();
		        chosenDate.set(dayOfMonth, monthOfYear, year);
		        long dtDob = chosenDate.toMillis(true);
		        CharSequence strDate = DateFormat.format("dd/MM/yyyy", dtDob);
		       
		        if(DATE.equals("Single Date")) { //Single date
		        	singledate = ""+strDate;
		        	displaydate.setText("Selected Date: "+singledate);
		        	Log.i("Search", "Selected Date: "+singledate);
		        }
		        if(DATE.equals("Date from")) { //period start date
		        	startdate = ""+strDate;
		        	displaydate.setText("Selected Date: "+startdate);
		        	Log.i("Search","Selected Date: "+startdate);
		        }
		        if(DATE.equals("Date to")) { //period finish date
		        	finishdate = ""+strDate;
		        	displaydate.setText("Selected Date: "+startdate+" - "+finishdate+".");
		        	Log.i("Search", "Selected Date: "+startdate+" - "+finishdate+".");
		        }
		    }}, 2012,0, 1);
		      
		datedialog.setMessage("Pick a date!");
		datedialog.show();
	}
	
	
}
