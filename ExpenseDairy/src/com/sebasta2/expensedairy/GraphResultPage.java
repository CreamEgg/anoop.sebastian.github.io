package com.sebasta2.expensedairy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GraphResultPage extends Activity{
	
	TextView line;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GraphView(this));
		
		//use the bundle to get the data from the database
		
		//line = (TextView)findViewById(R.id.line);
		//line.setText("this is line - 1 \nthis is line - 2");
	}

}
