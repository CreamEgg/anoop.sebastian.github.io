package com.sebasta2.expensedairy;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class ParentActivity extends TabActivity {

	private TabHost tabhost;
	private Intent intent;
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        
        tabhost = getTabHost();
        TabHost.TabSpec spec;
        
        //tab1
        intent = new Intent(this, HomePage.class);
        spec = tabhost.newTabSpec("");
        spec.setIndicator("",getResources().getDrawable(R.drawable.homepage));
        spec.setContent(intent);
        
        //adding the tab to the tabhost
        tabhost.addTab(spec);
        
        //tab2
        intent = new Intent(this, InsertPage.class);
        spec = tabhost.newTabSpec("");
        spec.setIndicator("",getResources().getDrawable(R.drawable.inserticon));
        spec.setContent(intent);
        
        //adding the tab to the tabhost
        tabhost.addTab(spec);
        
        //tab3
        intent = new Intent(this, SearchPage.class);
        spec = tabhost.newTabSpec("");
        spec.setIndicator("",getResources().getDrawable(R.drawable.search) );
        spec.setContent(intent);
        
        //adding the tab to the tabhost
        tabhost.addTab(spec);
        
       //tab4
        intent = new Intent(this, GraphPage.class);
        spec = tabhost.newTabSpec("");
        spec.setIndicator("",getResources().getDrawable(R.drawable.graph));
        spec.setContent(intent);
        
        //adding the tab to the tabhost
        tabhost.addTab(spec);
        
        //setting the current tab
        tabhost.setCurrentTab(0);
    }
    
    protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		//restoring the current tab
		tabhost.setCurrentTab(state.getInt("Current_Tab",0));
	}
    
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//getting current tab
		outState.putInt("Current_Tab", tabhost.getCurrentTab());
	}




	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_parent, menu);
        return true;
    }
}