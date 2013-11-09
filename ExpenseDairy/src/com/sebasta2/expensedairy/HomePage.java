package com.sebasta2.expensedairy;

import java.util.Date;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;


public class HomePage extends Activity implements OnItemClickListener{
	private TextView date;
	private ListView listview;
	private Button income;
	private Toast toast;
	SharedPreferences sharedprefs = null;
	Editor editor;
	SQLiteDatabase db;
	Cursor cursor;
	
	private static String[] topics = {"General Information",
									  "Adding New Entries",
									  "Searching For Entries",
									  "Graphing Selected Data",
									  "Recording And Viewing Receipts"};
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page_layout);
			
		sharedprefs = getSharedPreferences("DATACOLUMNS",MODE_WORLD_READABLE);
		String line = sharedprefs.getString("INCOME", "");
		
		
		date = (TextView)findViewById(R.id.homepagedate);
		date.setText("Today's Date: "+new Date(System.currentTimeMillis()));
		income = (Button)findViewById(R.id.income);
		income.setText("Current Income:"+line +"Euros(Click to Update)");
		
		listview = (ListView)findViewById(R.id.homelistview);
		listview.setOnItemClickListener(this);
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, topics);
		listview.setAdapter(adapter);
	}


	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		Intent in = new Intent(HomePage.this,Information.class);
		in.putExtra("title", topics[position]);
		startActivity(in);		
	}
	
	public void updateincome(View view) {
		if(view == income){
					
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Update Income");
		alert.setMessage("Enter the new Income.");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = ""+input.getText();
		  if(isInteger(value)) {
			  income.setText("Current Income: "+value+" Euros(Click to Update)");
			//writing share preferences for common use
				sharedprefs = getApplicationContext().getSharedPreferences("DATACOLUMNS",MODE_WORLD_READABLE);
				editor = sharedprefs.edit();
		        editor.putString("INCOME",value);
		        editor.commit();
		  }
		  else {
			  toast = Toast.makeText(getApplicationContext(),"Non valid income...", Toast.LENGTH_SHORT); toast.show();
		  }
		  		  
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
			  income.setText(income.getText());
		  }
		});

		alert.show();
		
		}
	}
	
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		income.setText(savedInstanceState.getString("Button_Text"));
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("Button_Text", ""+income.getText());
	}
	
	public boolean isInteger( String input )  
	{  
	   try  
	   {  
	      if(Integer.parseInt( input )>0 ||Float.parseFloat(input)>0 || Double.parseDouble(input)>0) 
	    	  return true;  
	      return false;
	      
	        
	   }  
	   catch( Exception e)  
	   {  
	      return false;  
	   }  
	} 
	
	
	
	

}
