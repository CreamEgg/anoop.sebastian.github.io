/**
 * 
 */
package com.sebasta2.expensedairy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @author anoop
 *
 */
public class Information extends Activity implements KeyListener{
		
	TextView title,description;
	private static String[] topics = {"General Information",
		  "Adding New Entries",
		  "Searching For Entries",
		  "Graphing Selected Data",
		  "Recording And Viewing Receipts"};
	
	String general = "Home Expense Diary is an Application developed by Anoop Sebastian " +
					 "for his EE310 Smartphone application development project. This is a " +
					 "financial app, where users can save, search, insert and graph their day to day expenses.";
	
	String newentry = " Adding a new entry is simple. You first choose the type of expence you want to add" +
					  ", then enter the amount, the comment part is optional but encouraged to enter to keep track of your spending." +
					  "The last step is take a picture of the receipt, this is purely optional, but still recommended";
	String search = " You can search the database for a single date, by selecting the singledate option or you can select a period " +
					" of dates where you can view the money spend on that period.";
	String graph = " You can graph your expenses for a single date, by selecting the singledate option or you can select a period " +
					" of dates where you can view the money spend on that period in a line graph, with labels";
	String receipts = " You can view the receips that you saved on a day, by using the search functionality and clicking on " +
					  "any list item, which takes you to the receipts that you saved on that day.";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_layout);
		
		title = (TextView)findViewById(R.id.infotitle);
		description = (TextView)findViewById(R.id.infodescription);
		
		Intent intent = getIntent();
		String type = intent.getStringExtra("title");
		
		if(type.equals(topics[0])){ title.setText(topics[0]); description.setText(general);}
		if(type.equals(topics[1])) {title.setText(topics[1]); description.setText(newentry);}
		if(type.equals(topics[2])) {title.setText(topics[2]); description.setText(search);}
		if(type.equals(topics[3])) {title.setText(topics[3]); description.setText(graph);}
		if(type.equals(topics[4])) {title.setText(topics[4]); description.setText(receipts);}
	}

	/* (non-Javadoc)
	 * @see android.text.method.KeyListener#clearMetaKeyState(android.view.View, android.text.Editable, int)
	 */
	@Override
	public void clearMetaKeyState(View view, Editable content, int states) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see android.text.method.KeyListener#getInputType()
	 */
	@Override
	public int getInputType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean onKeyDown(View view, Editable text, int keyCode,KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	public boolean onKeyOther(View view, Editable text, KeyEvent event) {
		return false;
	}

	
	public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
		return false;
	}

}
