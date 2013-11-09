package com.sebasta2.expensedairy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchPage extends Activity implements OnItemSelectedListener{

	private String[] dates = {"          Dates          ","Single Date","Date from","Date to"};
	private List<String> datelist;
	Spinner datepick;
	TextView selecteddate;
	Button searchbutton;
	String DATE = "";
	String singledate = "";
	String startdate = "";
	String finishdate = "";
	Toast toast;
	DatePickerDialog datedialog;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_page_layout);
		
		datelist = new ArrayList<String>();
		
		datepick = (Spinner)findViewById(R.id.date);
		selecteddate = (TextView)findViewById(R.id.selecteddate);
		searchbutton = (Button)findViewById(R.id.searchbutton);
		
		datepick.setOnItemSelectedListener(this);
		
		populatelist(datelist, dates);
		addItemsOnSpinner();
	}
	
	protected void onPause() {
		super.onPause();
	}
	
	//a method that objects add the string array to the list
	public void populatelist(List<String> list, String[] items) {
		for(String s: items) {
			list.add(s);
		}
	}
	
	//method that adds items to the expense spinner
	public void addItemsOnSpinner() {		 
		ArrayAdapter< String>dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, datelist);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		datepick.setAdapter(dataAdapter1);
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int id,long arg3) {
		DATE = datelist.get(id);
			if(id==0) {
				toast = Toast.makeText(getApplicationContext(),"Select an item from below...", Toast.LENGTH_SHORT); toast.show();
			}
			else{ showdatedialog(); }				
	}
		
	public void onNothingSelected(AdapterView<?> arg0) {}
		
	public void showdatedialog() {
		datedialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
				
			public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
				Time chosenDate = new Time();
		        chosenDate.set(dayOfMonth, monthOfYear, year);
		        long dtDob = chosenDate.toMillis(true);
		        CharSequence strDate = DateFormat.format("dd/MM/yyyy", dtDob);
			       
		        if(DATE.equals("Single Date")) { //Single date
		        	singledate = ""+strDate;
		        	selecteddate.setText("Selected Date: "+singledate);
		        	Log.i("Search", "Selected Date: "+singledate);
		        }
		        if(DATE.equals("Date from")) { //period start date
		        	startdate = ""+strDate;
		        	selecteddate.setText("Selected Date: "+startdate);
		        	Log.i("Search","Selected Date: "+startdate);
		        }
		        if(DATE.equals("Date to")) { //period finish date
		        	finishdate = ""+strDate;
		        	selecteddate.setText("Selected Date: "+startdate+" - "+finishdate+".");
		        	Log.i("Search", "Selected Date: "+startdate+" - "+finishdate+".");
		        }
		    }}, 2012,0, 1);
		      
			datedialog.setMessage("Pick a date!");
			datedialog.show();
	}
		
	public void Searchaction(View view) {
		if(view == searchbutton) {
			Intent in = new Intent(SearchPage.this, SearchResultsPage.class);
			if(singledate.equals("") && startdate.equals("") && finishdate.equals("")) {
				in.putExtra("DATE", "All=="+"");
				startActivity(in);
			}
			if(!(singledate.equals(""))) {
				String date = singledate.split("/")[2]+"-"+singledate.split("/")[1]+"-"+singledate.split("/")[0];
				in.putExtra("DATE", "Single=="+date);
				singledate="";
				startActivity(in);
			}
			if(singledate.equals("") && !startdate.equals("") && !finishdate.equals("")) {
				String date1 = startdate.split("/")[2]+"-"+startdate.split("/")[1]+"-"+startdate.split("/")[0];
				String date2 = finishdate.split("/")[2]+"-"+finishdate.split("/")[1]+"-"+finishdate.split("/")[0];
				in.putExtra("DATE", "Double=="+date1+"=="+date2);
				startdate=""; finishdate="";
				startActivity(in);
			}
		}
	}
}
	