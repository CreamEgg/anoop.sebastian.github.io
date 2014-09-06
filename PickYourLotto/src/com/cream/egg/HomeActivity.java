package com.cream.egg;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.ads.*;
/*
 * Irish Lotto: Row = 6 numbers ranging from 1 - 45, and two rows is the minimum
 * 
 * */
public class HomeActivity extends Activity 
{
	private AdView adView;
	private static final String TAG = "HomeActivity";	
	//GridView lottoRow_1,lottoRow_2;
	Button pick,clear;
	private Set<Integer> randomNumbers = null;
	Integer[] numbers;
	private static final int numbers_per_row = 6;
	private static final String adID = "private key";
	private static final Integer[] icons = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
						  R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight,
						  R.drawable.nine, R.drawable.ten, R.drawable.eleven, R.drawable.twelve,
						  R.drawable.thirteen, R.drawable.fourteen, R.drawable.fifteen,
						  R.drawable.sixteen, R.drawable.seventeen, R.drawable.eighteen,
						  R.drawable.nineteen,R.drawable.twenty, R.drawable.twentyone, R.drawable.twentytwo,
						  R.drawable.twentythree, R.drawable.twentyfour, R.drawable.twentyfive,
						  R.drawable.twentysix, R.drawable.twentyseven, R.drawable.twentyeight,
						  R.drawable.twentynine, R.drawable.thirty, R.drawable.thirtyone,
						  R.drawable.thirtytwo, R.drawable.thirtythree, R.drawable.thirtyfour, 
						  R.drawable.thirtyfive, R.drawable.thirtysix, R.drawable.thirtyseven,
						  R.drawable.thirtyeight, R.drawable.thirtynine, R.drawable.fourty,
						  R.drawable.fourtyone, R.drawable.fourtytwo, R.drawable.fourtythree,
						  R.drawable.fourtyfour, R.drawable.fourtyfive};
	private static final String MESSAGE = "Flip the screen to get new numbers ...";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_layout);
		playSound();
		randomNumbers = new HashSet<Integer>();
		initialiseUI();		
		setLottoBalls();
		
		//display the way to change number
		Toast.makeText(this, MESSAGE, Toast.LENGTH_LONG).show();
	}

	private void playSound() 
	{
		MediaPlayer player = MediaPlayer.create(this, R.raw.jackpot);
		player.start();		
	}

	private void setLottoBalls() 
	{
		generateNumbers();
		lottoRow_1.setAdapter(new CustomGridAdapter(this,numbers));
		generateNumbers();
		lottoRow_2.setAdapter(new CustomGridAdapter(this,numbers));		
	}

	private void initialiseUI() 
	{
		//lottoRow_1 = (GridView)findViewById(R.id.gridview1);
		//lottoRow_2 = (GridView)findViewById(R.id.gridview2);
		
		adView = new AdView(this, AdSize.BANNER,adID);
		LinearLayout layout = (LinearLayout)findViewById(R.id.homelayout);
		
		layout.addView(adView);		
		adView.loadAd(new AdRequest());
		
		Log.i(TAG, "UI Initialised");
	}
	
	private void generateNumbers()
	{
		Integer[] randomnumbers = makeRandomNumbers();
		numbers = new Integer[numbers_per_row];
		
		for (int i=0; i<numbers.length;i++) 
		{
			numbers[i] = icons[randomnumbers[i]];
		}		
	}
	
	private Integer[] makeRandomNumbers()
	{
		randomNumbers.clear();
		
		Random randomNumberGenerator = new Random();
		
		while (randomNumbers.size() < numbers_per_row) {
			randomNumbers.add(Integer.valueOf(randomNumberGenerator.nextInt(44)));
		}
		
		Log.i(TAG, "Numbers generated: "+randomNumbers);
		
		Object[] objects = randomNumbers.toArray();
		Integer[] arrays = new Integer[numbers_per_row];
		for (int i=0; i<objects.length; i++)
		{
			arrays[i] = (Integer)objects[i];
		}
		return arrays;		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onDestroy() 
	{
		if (adView !=null) {
			adView.destroy();
		}			
		super.onDestroy();
		Log.i(TAG, "onDestroy called");
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		Log.i(TAG, "onPause called");
	}

	@Override
	protected void onRestart() 
	{
		super.onRestart();
		Log.i(TAG, "onRestart called");
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		Log.i(TAG, "onResume called");
	}
}