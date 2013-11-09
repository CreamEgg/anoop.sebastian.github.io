package com.sebasta2.expensedairy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

public class GraphView extends View {

	Context context;
	int width,height;
	Path path;
	int test = 18;
	int sizel = 0;
	int sizer = 50;
	int y=0;
	SharedPreferences sharedprefs = null;
	Editor editor;
	SQLiteDatabase db;
	String date;
	Cursor cursor;
	Paint paint = new Paint();
	
	//height 0-18 width 2-22
	float[] points = new float[11];//{162.36f,13.45f,100,38.62f,100,300,10,900,50,39,100};
	//float[] points = {125.96f,23.85f,0f,38.96f,369f,680f,500f,750f,10f,258.89f,150.96f};
	String[] expenses = {"Electricity","Groceries","Insurance","Internet",
						"Leisure","Loans","Other","Rent","Telephone",
						"Transport","Utilities"};
	
	public GraphView(Context context) {
		super(context);
		path = new Path();	
		
		sharedprefs = context.getSharedPreferences("DATACOLUMNS",context.MODE_WORLD_READABLE);
		String line = sharedprefs.getString("VALUES", "");
		Log.i("GraphViewPage", "Retrieved sharedprefs: "+line+" finished");
		
		String[] temp = line.split("==");
		
		for(int i=0;i<temp.length;i++) {
			if(temp[i].equals("") || temp[i].equals("0.00")) {
				points[i] = 0;
			}
			else {
				points[i] = Float.valueOf(Float.parseFloat(temp[i]));
			}
		}		
	}
	
	public void onDraw(Canvas canvas) {
		width = this.getWidth();
		height = this.getHeight();
	
		int w = width/23;
		int h = height/20;
	
		Log.i("Screen Dimensions","Width: "+width+" Height: "+height);
	
		paint.setColor(Color.BLACK);
		paint.setTextSize(10);
		paint.setStrokeWidth(3);

		//drawing the axis and the texts
		canvas.drawLine(w*1.9f, h*18,w*23, h*18, paint); // x axis
		canvas.drawLine(w*1.9f, h/20,w*1.9f, h*18, paint); //y axis
	
		for(int i=0; i<=20; i++) {
			paint.setTextSize(9);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(1);
			canvas.drawText("€"+sizel+"-"+sizer, w/20, h*test, paint);
			sizel = sizel+50; sizer = sizer+50;
			test--;
			
			paint.setStrokeWidth(1);
			paint.setColor(Color.GRAY);
			
			canvas.drawLine(w*1.9f, h*test,w*23, h*test, paint);
		}
		
		paint.setStrokeWidth(4);
		
	
		for(int i=0; i<points.length; i++) {
			int x = ((i+1)*2)+1;
			//1-18 height
			if(points[i] <= 50 &&  points[i] > 0) y = 18;
			if(points[i] <= 100 && points[i] > 50) y = 17;
			if(points[i] <= 150 && points[i] > 100) y = 16;
			if(points[i] <= 200 && points[i] > 150) y = 15;
			if(points[i] <= 250 && points[i] > 200) y = 14;
			if(points[i] <= 300 && points[i] > 250) y = 13;
			if(points[i] <= 350 && points[i] > 300) y = 12;
			if(points[i] <= 400 && points[i] > 350) y = 11;
			if(points[i] <= 450 && points[i] > 400) y = 10;
			if(points[i] <= 500 && points[i] > 450) y = 9;
			if(points[i] <= 550 && points[i] > 500) y = 8;
			if(points[i] <= 600 && points[i] > 550) y = 7;
			if(points[i] <= 650 && points[i] > 600) y = 6;
			if(points[i] <= 700 && points[i] > 650) y = 5;
			if(points[i] <= 750 && points[i] > 700) y = 4;
			if(points[i] <= 800 && points[i] > 750) y = 3;
			if(points[i] <= 850 && points[i] > 800) y = 2;
			if(points[i] <= 900 && points[i] > 850) y = 1;
			if(points[i] <= 1000 && points[i] > 900) y = 1;
			if(points[i] > 1000) y = 1;
		
			float posx = w*x;
			float posy = h*y;
		
					
			//drawing path through the points		
			if(i == 0)	path.moveTo(posx, posy);//Initializing the path object
		
			else path.lineTo(posx, posy);
		
			paint.setColor(0xff99CC00);
			canvas.drawPath(path, paint);
			
			//drawing circles on points posx, posy
			paint.setAntiAlias(true); paint.setStyle(Paint.Style.STROKE); paint.setColor(0xff2E3D00);
			canvas.drawCircle(posx, posy,4, paint);
	}
	
		//setting the values in the x axis with their expense types
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(10);
	
		for(int i=0; i<=10; i++) {
			int x = (i+1)*2;	
		
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(3);
			paint.setTextSize(10);
		
			canvas.drawText(expenses[i],w*x,h*19,paint);
			canvas.drawText("€ "+points[i], w*x, h*20f, paint);
			paint.setStrokeWidth(1);
			paint.setColor(Color.GRAY);
		}
	}
}
