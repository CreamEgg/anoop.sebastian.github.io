package com.sebasta2.expensedairy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultAdapter extends ArrayAdapter<Result>{
	
	Context context;
	int layout;
	Result[] data = null;
	
	public ResultAdapter(Context context,Result[] data) {
		super(context,R.layout.rowtable,data);
		this.context = context;
		this.data = data;
	}
	
	public View getView(int position, View convertview, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View row = inflater.inflate(R.layout.rowtable, parent,false);
		
		/*"Electricity","Groceries","Insurance","Internet","Leisure","Loans",
		"Other","Rent","Telephone","Transport","Utilities"};*/
		
		TextView date = (TextView)row.findViewById(R.id.date);
		
		TextView elect = (TextView)row.findViewById(R.id.electext);
		ImageView elecr = (ImageView)row.findViewById(R.id.elecreceipt);
		
		TextView groct = (TextView)row.findViewById(R.id.groctext);
		ImageView grocr = (ImageView)row.findViewById(R.id.grocreceipt);
		
		TextView insut = (TextView)row.findViewById(R.id.insutext);
		ImageView insur = (ImageView)row.findViewById(R.id.insureceipt);
		
		TextView intert = (TextView)row.findViewById(R.id.intertext);
		ImageView interr = (ImageView)row.findViewById(R.id.interreceipt);
		
		TextView leist = (TextView)row.findViewById(R.id.leistext);
		ImageView leisr = (ImageView)row.findViewById(R.id.leisreceipt);
		
		TextView loant = (TextView)row.findViewById(R.id.loantext);
		ImageView loanr = (ImageView)row.findViewById(R.id.loanreceipt);
		
		TextView othet = (TextView)row.findViewById(R.id.othertext);
		ImageView other = (ImageView)row.findViewById(R.id.otherreceipt);
		
		TextView rentt = (TextView)row.findViewById(R.id.renttext);
		ImageView rentr = (ImageView)row.findViewById(R.id.rentreceipt);
		
		TextView telet = (TextView)row.findViewById(R.id.teletext);
		ImageView teler = (ImageView)row.findViewById(R.id.telereceipt);
		
		TextView trant = (TextView)row.findViewById(R.id.transtext);
		ImageView tranr = (ImageView)row.findViewById(R.id.transreceipt);
		
		TextView utilt = (TextView)row.findViewById(R.id.utiltext);
		ImageView utilr = (ImageView)row.findViewById(R.id.utilreceipt);
		
		
		date.setText("Date: "+data[position].date);
		elect.setText("  ELECTRICITY\n    Amount: €" + data[position].elecamount + "\n    Comment: " + data[position].eleccomment);
		elecr.setImageBitmap(data[position].elecreceipt);
		
		groct.setText("  GROCERIES\n    Amount: €" + data[position].grocamount + "\n    Comment: " + data[position].groccomment);
		grocr.setImageBitmap(data[position].grocreceipt);
		
		insut.setText("  INSURANCE\n    Amount: €" + data[position].insuamount + "\n    Comment: " + data[position].insucomment);
		insur.setImageBitmap(data[position].insureceipt);
		
		intert.setText("  INTERNET\n    Amount: €" + data[position].interamount + "\n    Comment: " + data[position].intercomment);
		interr.setImageBitmap(data[position].interreceipt);
		
		leist.setText("  LEISURE\n    Amount: €" + data[position].leisamount + "\n    Comment: " + data[position].leiscomment);
		leisr.setImageBitmap(data[position].leisreceipt);
		
		loant.setText("  LOAN\n    Amount: €" + data[position].loanamount + "\n    Comment: " + data[position].loancomment);
		loanr.setImageBitmap(data[position].loanreceipt);
		
		othet.setText("  OTHER\n    Amount: €" + data[position].otheamount + "\n    Comment: " + data[position].othecomment);
		other.setImageBitmap(data[position].othereceipt);
		
		rentt.setText("  RENT\n    Amount: €" + data[position].rentamount + "\n    Comment: " + data[position].rentcomment);
		rentr.setImageBitmap(data[position].rentreceipt);
		
		telet.setText("  TELEPHONE\n    Amount: €" + data[position].teleamount + "\n    Comment: " + data[position].telecomment);
		teler.setImageBitmap(data[position].telereceipt);
		
		trant.setText("  TRANSPORT\n    Amount: €" + data[position].tranamount + "\n    Comment: " + data[position].trancomment);
		tranr.setImageBitmap(data[position].tranreceipt);
		
		utilt.setText("  UTILITIES\n    Amount: €" + data[position].utilamount + "\n    Comment: " + data[position].utilcomment);
		utilr.setImageBitmap(data[position].utilreceipt);
		
		
		return row;
		
	}
	
	//static class ResultHolder {
		//TextView rheading,ramount;//,rcomment;
		//ImageView rimage;
	//}
 
}
