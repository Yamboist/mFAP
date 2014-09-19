package com.example.automataalpha;

import com.example.automataalpha.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener  {
	private CustomViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    
    
    
    
    // Tab titles
    private String[] tabs = { "Drawing", "Simulate", "Convert" };
    DrawingActivity always = new DrawingActivity();
    InputActivity inputs = new InputActivity();
    ConvertActivity converts = new ConvertActivity();
    
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        // Initilization
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        viewPager.setEnabled(false);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
 
        
        
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        /**
         * on swiping the viewpager make respective tab selected
         * */
        
        mAdapter.always = this.always;
        mAdapter.inputs = this.inputs;
        mAdapter.convert = this.converts;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
                
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
 
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    	 mAdapter.always = this.always;
         mAdapter.inputs = this.inputs;
         mAdapter.convert = this.converts;
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
       
    	viewPager.setCurrentItem(tab.getPosition());
        
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    	 mAdapter.always = this.always;
         mAdapter.inputs = this.inputs;
         mAdapter.convert = this.converts;
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
    	
        switch (item.getItemId()) {
        case R.id.hand_btn:
            // search action
        	always.mycdv.mode = 0;
        	pos = 0;
        	invalidateOptionsMenu();
        	
            return true;
        case R.id.node_btn:
        	always.mycdv.mode = 1;
        	pos = 1;
        	invalidateOptionsMenu();
        	return true;
        case R.id.line_btn:
        	always.mycdv.mode = 2;
        	pos = 2;
        	invalidateOptionsMenu();
        	return true;
        case R.id.eraser_btn:
        	always.mycdv.mode = 3;
        	pos =3;
        	invalidateOptionsMenu();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem hand = menu.findItem(R.id.hand_btn);     
        MenuItem node = menu.findItem(R.id.node_btn); 
        MenuItem line = menu.findItem(R.id.line_btn); 
        MenuItem eraser = menu.findItem(R.id.eraser_btn); 
        
    	hand.setIcon(R.drawable.hand);
    	node.setIcon(R.drawable.node);
    	line.setIcon(R.drawable.line);
    	eraser.setIcon(R.drawable.eraser);
    	switch(pos){
    	case 0:
    		hand.setIcon(R.drawable.hand_s);
    		break;
    	case 1:
    		node.setIcon(R.drawable.node_s);
    		break;
    	case 2:
    		line.setIcon(R.drawable.line_s);
    		break;
    	case 3:
    		eraser.setIcon(R.drawable.eraser_s);
    		break;
    	default:
    		break;
    	}
        return super.onPrepareOptionsMenu(menu);

    }
    
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);
		
		/*ImageButton nodes = (ImageButton) findViewById(R.id.file_btn);
		ImageButton lines = (ImageButton) findViewById(R.id.input_btn);
		ImageButton delete = (ImageButton) findViewById(R.id.convert_btn);
		final CirclesDrawingView cdw = (CirclesDrawingView) findViewById(R.id.cdv);
		
		nodes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cdw.mode = 1;
			}
		});
		
		lines.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cdw.mode = 2;
			}
		});
		
		delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cdw.mode = 3;
			}
		});
	}*/

	
}
