package com.sebasta2.expensedairy;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Camera extends Activity {

	Intent in;
	Bitmap pic;
	ImageView image;
	Button takepicture, back;	
	
	final static int cameraData = 0;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_layout);
		
		image = (ImageView)findViewById(R.id.picture);
		takepicture = (Button)findViewById(R.id.takepicture);
		back = (Button)findViewById(R.id.savepicture);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			pic = (Bitmap) extras.get("data");
			image.setImageBitmap(pic);
		}
		else {
			pic = BitmapFactory.decodeResource(getResources(), R.drawable.defreceipt);			
		}
	}
	
	public void cameraclick(View view) {
		if(view == takepicture) {
			in = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(in, cameraData);	
			
			Log.i("CAMERA", "picture clicked");
		}
		
		if(view == back) {
			finish();
			Log.i("CAMERA", "back clicked");
		}		
	}
	
	public void finish() {
		Intent data = new Intent();
		data.putExtra("receipt",Bitmaptobytes(pic));
		 // Activity finished ok, return the data
		 setResult(RESULT_OK, data);
		super.finish();
	}
	
	// A method that converts a bitmap image to an array of bytes
	public byte[] Bitmaptobytes(Bitmap picture) {
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
		picture.compress(CompressFormat.PNG, 0, bstream);
		byte[] bytes = bstream.toByteArray();
		return bytes;
	}
}
