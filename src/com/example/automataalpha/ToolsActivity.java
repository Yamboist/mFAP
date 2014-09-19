package com.example.automataalpha;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ToolsActivity extends Activity{
	private Menu menu;
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
		 	this.menu = menu;
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.tools_menu, menu);
	 
	        return super.onCreateOptionsMenu(menu);
	    }
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
}
