package com.cream.egg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomGridAdapter extends BaseAdapter 
{
	private Context context = null;
	private Integer[] ids = null;
	
	public CustomGridAdapter(Context context,Integer[] icons)
	{
		this.context = context;
		this.ids = icons;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parentView) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View gridView = null;
		
		if (view == null) {
			gridView = new View(context);
			
			//inflate layout file
			gridView = inflater.inflate(R.layout.grid_layout, null);
			
			//set the value in textfield
			//TextView grid_label = (TextView) gridView.findViewById(R.id.grid_item_label);
			//grid_label.setText(ids[position]);
			
			// set image based on selected text
			ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			 
			imageView.setImageResource(ids[position]);
		}
		else {
			gridView = (View)view;
		}
		return gridView;
	}

	@Override
	public int getCount() {
		return ids.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}	
}