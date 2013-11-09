package com.sebasta2.expensedairy;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class InsertPage extends Activity implements OnItemSelectedListener {
	
	private Spinner types; 
	private Button picture,save,date;
	private EditText newamount,newcomment;
	private ImageView receipt;
	private SQLiteDatabase db;
	private List<String> list;
	private Toast toast;
	private String expensetype = null;
	private String selecteddate="";
	private Cursor cursor = null;	
	private Bitmap mPhoto = null;
    SharedPreferences sharedprefs = null;
    Editor editor;
    byte[] bytes = null;
    private static String[] expenses = {"Electricity","Groceries","Insurance","Internet",
										"Leisure","Loans","Other","Rent","Telephone",
									"Transport","Utilities"};
   
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_page_layout);
		Log.i("Insert", ""+new Date(System.currentTimeMillis()));
				
		//writing share preferences for common use
		sharedprefs = this.getApplicationContext().getSharedPreferences("DATACOLUMNS",MODE_WORLD_READABLE);
		editor = sharedprefs.edit();
        editor.putString("COLUMNS",Getcolumns());
        editor.commit();
        Log.i("Insert", "writing sharedprefs: "+Getcolumns()+" finished");
		
		list = new ArrayList<String>();
		list.add("Select an expence from below");
		
		addItemsOnSpinner(); //adding the items to the spinner according to the database
		
		newamount    = (EditText)findViewById(R.id.newamount);
		newcomment   = (EditText)findViewById(R.id.newcomment);
		date		 = (Button)findViewById(R.id.insertdate);
		picture      = (Button)findViewById(R.id.picture);
		save         = (Button)findViewById(R.id.save);
		receipt      = (ImageView)findViewById(R.id.receipt);
		
		types.setOnItemSelectedListener(this);
		
		//add a date dialog and display the current date
		date.setText("Select Date");
		selecteddate = ""+new Date(System.currentTimeMillis());
		
		db = (new Database(this)).getWritableDatabase();
		populatelist(); //adds the items in the shared preferences to the list
		
	}
	
	protected void onPause() {
		super.onPause();
	}	
	
	public String Getcolumns() {
		String line = "";
		for(String s: expenses) {
			line +=s+"--";
		}		
		return line;		
	}
	
	//method that handles the click events
	public void clickaction(View view) {
		if(view == date) {
			DatePickerDialog datedialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
				
				public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
					Time chosenDate = new Time();
			        chosenDate.set(dayOfMonth, monthOfYear, year);
			        long dtDob = chosenDate.toMillis(true);
			        CharSequence strDate = DateFormat.format("dd/MM/yyyy", dtDob);
			        
			        selecteddate = ""+strDate;
			        date.setText("Selected Date: "+getdate());
			        Log.i("Search", "Selected Date: "+getdate());
			        
			        
			    }}, 2012,11, 18);
			      
			datedialog.setMessage("Select date for entry!");
			datedialog.show();
		}
		
		if(view == save) {
			String valid = newamount.getText().toString(); //checking for the empty field condition
						
			if(valid.equals("")) {
				toast = Toast.makeText(getApplicationContext(),"The Amount field cannot be empty...", Toast.LENGTH_SHORT); toast.show();
			}
			else if((expensetype.equals("Select an expence from below"))) {
				toast = Toast.makeText(getApplicationContext(),"The type of expense should be selected...", Toast.LENGTH_SHORT); toast.show();
			}
			else if(bytes==null) {
				//prompts the user to take the picture of the receipt
	    		AlertDialog.Builder alertpic = new AlertDialog.Builder(this);
	    		alertpic.setTitle("Picture Receipt");
	    		alertpic.setMessage("It's recommended to take a picture of the receipt");
	    		alertpic.setCancelable(false);
	    		alertpic.setPositiveButton("Save Receipt", new DialogInterface.OnClickListener() {
					
				public void onClick(DialogInterface dialog, int id) {						
					cameraIntent();
				}});
				
	    		alertpic.setNegativeButton("Don't save receipt", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
								
						Bitmap bit = BitmapFactory.decodeResource(getResources(), R.drawable.defreceipt);
						bytes = Bitmaptobytes(bit);
			    		//pops up an alert windows asking the user to confirm
			    		AlertDialog.Builder alertdb = new AlertDialog.Builder(InsertPage.this);
						alertdb.setTitle("Confirm Details");
						alertdb.setMessage("Please confirm the details to proceed");
						alertdb.setCancelable(false);
						alertdb.setPositiveButton("Confirm and save", new DialogInterface.OnClickListener() {
							
						public void onClick(DialogInterface dialog, int id) {	
							//reset the textfields and cameras
							InsertDatabase(); // performs the insert functionality
							types.setSelection(0, true);
							newamount.setText("");
							newcomment.setText("");
							receipt.setImageResource(R.drawable.receipt);
							mPhoto=null;							
						}});
						
						alertdb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();						
							}
						});
						
						//creates the alert dialog
						AlertDialog alertd = alertdb.create();
						
						//shows the dialog
						alertd.show();	  						
					}
				});
				
				//creates the alert dialog
				AlertDialog alertdialog = alertpic.create();
				
				//shows the dialog
				alertdialog.show();	
			}
			else {   		
				InsertDatabase(); // performs the insert functionality
				//reset the textfields and bitmap
				types.setSelection(0, true);
				newamount.setText("");
				newcomment.setText("");
				receipt.setImageResource(R.drawable.receipt);
				mPhoto=null;	    		  			    		
			}
		}
	}
	
	//a method that gets the shared preferences and populate the list with the contents
	@SuppressWarnings("deprecation")
	public void populatelist() {
		sharedprefs = getSharedPreferences("DATACOLUMNS",MODE_WORLD_READABLE);
		String line = sharedprefs.getString("COLUMNS", "");
		Log.i("InsertPage", "Retrieved sharedprefs: "+line.toLowerCase()+" finished");
		
		String[] temp = line.split("--");
		
		for(String s: temp)
		list.add(s.toLowerCase()); 	
	}
		 
	//method that adds items to the spinner
	public void addItemsOnSpinner() {
		types = (Spinner) findViewById(R.id.category);
		
		//adds the appropriate items to the spinner			 			
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		types.setAdapter(dataAdapter);
	}
	
	//method that returns current date as a formatted string	 
	public String getdate() {
		String date = selecteddate.split("/")[2]+"-"+selecteddate.split("/")[1]+"-"+selecteddate.split("/")[0];
		return date;
	}
	
	//method that handles events from record receipt button
	public void Takephoto(View view) {
		if(view == picture)	 cameraIntent();//calls the camera intent		
	}
	
	//method that invokes the camera activity 
	public void cameraIntent() {
		Intent in = new Intent(InsertPage.this, Camera.class); Log.i("INSERT", "Camera intetn starting");
		startActivityForResult(in, 0);
	}	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			bytes = (byte[])extras.getByteArray("receipt");
			Log.i("INSERT", "Bytes decoded: "+bytes.length);
		}
	}

	// A method that performs the insert/update operation to the database
	public void InsertDatabase() throws SQLException {
		try {
			
			Log.i("Insert","Amount: "+newamount);
			Log.i("Insert","Comment: "+newcomment);
			Log.i("Insert","Pictureinbytes: "+bytes.length);
			
			//declaring and initializing the contentvalues object
			ContentValues contentvalues = new ContentValues();
			
			expensetype = expensetype.toLowerCase();
			
			//getting the values from the edit texts
			String amount = ""+newamount.getText();
			String comment = newcomment.getText().toString();
			if(comment.equals("")) {
				comment="no comments";
			}
			
			//checking if the row already exists, if exists then update
			cursor = db.rawQuery("SELECT * FROM EXPENSES WHERE date LIKE '"+getdate()+"'", null);
			Log.i("Insert", "cursor count"+cursor.getCount());
			
			if(cursor.getCount() == 1) {
				cursor.moveToFirst();
					//performing an update to the database with the values
					contentvalues.put(expensetype,""+amount);						//entry amount
					contentvalues.put(expensetype+"C", comment);			//entry comment
					contentvalues.put(expensetype+"R", bytes);	//entry receipt in bytes[]
					
					if((db.update("EXPENSES", contentvalues,"id='"+cursor.getInt(0)+"'", null)>0)) {
						Log.i("Insert", "Amount: "+amount+" Comment: "+comment+" receipt"+bytes.length+" updated");
						toast = Toast.makeText(getApplicationContext(),"Update completed...", Toast.LENGTH_SHORT); toast.show();
					}
				
			}
			else {
				//performing an new insert to the database with the values
				contentvalues.put("date", getdate()); 						//the entry date
				contentvalues.put(expensetype,amount);						//entry amount
				contentvalues.put(expensetype+"C", comment);			//entry comment
				contentvalues.put(expensetype+"R", bytes);	//entry receipt in bytes[]
							
				
				//Initializing other columns
				for(int i=0; i<expenses.length; i++) {
					if(!(expenses[i].toLowerCase().contentEquals(expensetype))) {
						contentvalues.put(expenses[i].toLowerCase(), "0.00");						//entry amount
						contentvalues.put(expenses[i].toLowerCase()+"C", "not edited");			//entry comment
						contentvalues.put(expenses[i].toLowerCase()+"R", bytes);	//entry receipt in bytes[]
					}
				}
				if((db.insert("EXPENSES", null, contentvalues)) > 1) {
					Log.i("Insert", "Amount: "+amount+" Comment: "+comment+" receipt"+mPhoto.toString()+" inserted");
					toast = Toast.makeText(getApplicationContext(),"New Entry added...", Toast.LENGTH_SHORT); toast.show();
				}
			}
		}
		catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	//handling click events from the spinner
	public void onItemSelected(AdapterView<?> arg0, View view, int id,long arg3) {
		expensetype = list.get(id);Log.i("Insert", expensetype);
		
		if(expensetype.equals("Select an expence from below")) {
			toast = Toast.makeText(getApplicationContext(),"Select an expence from below...", Toast.LENGTH_SHORT); toast.show();
		}
		
	}
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	public Bitmap BytestoBitmap(byte[] bytes) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
		Log.i("Search", "Bitmap decoded:"+bitmap.toString());
		return bitmap;
	}
	// A method that converts a bitmap image to an array of bytes
		public byte[] Bitmaptobytes(Bitmap picture) {
			ByteArrayOutputStream bstream = new ByteArrayOutputStream();
			picture.compress(CompressFormat.PNG, 0, bstream);
			byte[] bytes = bstream.toByteArray();
			return bytes;
		}
}
