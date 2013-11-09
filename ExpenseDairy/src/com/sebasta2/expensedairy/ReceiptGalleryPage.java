package com.sebasta2.expensedairy;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ReceiptGalleryPage extends Activity{
	
	DisplayMetrics dm;
	int width;
	int height;
	SQLiteDatabase db;
	Cursor cursor;
	TextView title;
	ImageView receipt;
	Button previous, next, close;
	int[] num = {4,7,10,13,16,19,22,25,28,31,34};
	int count = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receipts_page_layout);
		
		db = (new Database(this)).getReadableDatabase();
		
		title = (TextView)findViewById(R.id.receipttitle);
		receipt = (ImageView)findViewById(R.id.receiptpicture);
		previous = (Button)findViewById(R.id.previous);
		next = (Button)findViewById(R.id.next);
		close = (Button)findViewById(R.id.close);		
		
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");
		
		title.setText("Receipts for the date:"+date+"."); Log.i("ReceiptGallery", "Date received: "+date);
		
		cursor = db.rawQuery("SELECT * FROM EXPENSES WHERE date='"+date+"';", null);
		Log.i("ReceiptGallery", "cursor columns:"+cursor.getColumnCount());
		if(cursor != null) {
			cursor.moveToFirst();
			
			Log.i("ReceiptGallery", "cursor:"+cursor.getString(2));
			
			receipt.setImageBitmap(BytestoBitmap(cursor.getBlob(4)));
			
		}		
	}
	
	public Bitmap[] bitmaps() {
		int[] num = {4,7,10,13,16,19,22,25,28,31,34};
		Bitmap[] maps = new Bitmap[11];
		for(int i=0; i<num.length; i++) {
			maps[i] = decodeBitmapFrombytes(cursor.getBlob(num[i]),640,480);
		}
		return maps;
	}
	
	public Bitmap BytestoBitmap(byte[] bytes) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
		//Log.i("Search", "Bitmap decoded:"+bitmap.toString());		
		return bitmap;
	}
	
	//loading bitmaps efficiently
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);
        } 	else {
            	inSampleSize = Math.round((float)width / (float)reqWidth);
        	}
		}
		return inSampleSize;
	}
		
	public static Bitmap decodeBitmapFrombytes(byte[] bytes,int reqWidth, int reqHeight) {
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeByteArray(bytes,0,bytes.length, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}
		
	public void buttonclicked(View view) {
		if(view == close) {	finish(); }
		if(view == previous) {
			count--;
			if(count== -1) {	count = 10; }
			changePicture(num[count]);
			Log.i("rECEIPT", "Position:" +count);
			
	}
		if(view == next) {
			count++;
			if(count == 11) {	count = 0 ; }
			changePicture(num[count]);
			Log.i("rECEIPT", "Position:" +count);
		}
	}
		
	public void changePicture(int i) {
		receipt.setImageBitmap(BytestoBitmap(cursor.getBlob(i)));
	}

}