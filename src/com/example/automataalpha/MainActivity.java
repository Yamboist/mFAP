package com.example.automataalpha;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.example.automataalpha.CirclesDrawingView.CircleArea;
import com.example.automataalpha.CirclesDrawingView.Lines;
import com.example.automataalpha.TabsPagerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener  {
	private CustomViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    
    // Tab titles
    private String[] tabs = { "Drawing", "Simulate", "Convert" };
    DrawingActivity always;
    InputActivity inputs;
    ConvertActivity converts;
    
    
    int pos = 0;
    
    @Override
    protected void onSaveInstanceState(Bundle state){
    	state.putString("ere", "kva");
    	super.onSaveInstanceState(state);
    	//state.putSerializable("actv", this.always);
 
    	
    }
    @Override
    protected void onRestoreInstanceState(Bundle state){ 
    	super.onRestoreInstanceState(state);
    	Log.w("-=-=-=-=-=-=-=---=-=-=-=-=-=-=-=-=-=-=-",state.getString("ere"));
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("[--------------------------]","hellohellohello");
        if(savedInstanceState == null){
        	Log.w("[-----------------------------]","WHAAAAAAAAATTTTTT");
        }
        if(savedInstanceState != null){
        	Log.w("[--------------------------]","pewewpewpepwewpewpew");
        }
        else{
        if(this.always == null && this.converts == null && this.inputs == null){
        	 this.always = new DrawingActivity();
             this.converts = new ConvertActivity();
             this.inputs = new InputActivity();
        }
       
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
       inputs.cdv = always.mycdv;
       converts.cdv = always.mycdv;
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
        case R.id.new_btn:
        	
        	this.always.mycdv.mCirclePointer.clear();
        	this.always.mycdv.mCircles.clear();
        	this.always.mycdv.initial = null;
        	this.always.mycdv.mLines.clear();
        	this.always.mycdv.invalidate();
        	return true;
        case R.id.save_as_image_btn:
        	save();
        	return true;
        case R.id.save_btn:
        	saveFile();
        	return true;
        case R.id.from_file_btn:
        	loadFile();
        	return true;
        case R.id.exit_btn:
        	finish();
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
    
    public void loadFile(){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Input file");
		alert.setMessage("Please give the name of the file");
	
		final EditText input = new EditText(this);
		alert.setView(input);
		final String fileName;
		final boolean cont = false;
		final Context actv = this;
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(DialogInterface dialog, int whichButton) {
		        String path = Environment.getExternalStorageDirectory().getAbsolutePath(); 
		              
		        File file = getAlbumStorageDir(input.getText().toString()+".txt");
		        Toast.makeText(getApplicationContext(), file.getAbsolutePath(),Toast.LENGTH_LONG).show();
		        try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					boolean readLines = false;
					String row;
					always.mycdv.mCircles.clear();
					always.mycdv.mLines.clear();
					while( (row = br.readLine()) != null){
						if(row.indexOf("===lines===")>=0){
							readLines = true;
						}
						if(!readLines && row.indexOf("===lines===")<0){
							String data[] = row.split(",");
							CircleArea c = new CircleArea(Integer.parseInt(data[1]),Integer.parseInt(data[2]),40,data[0]);
							c.id = data[0];
							c.label = data[0];
							
							c.isInitial = Boolean.parseBoolean(data[3]);
							if(c.isInitial){
								always.mycdv.initial = c;
							}
							c.isFinal = Boolean.parseBoolean(data[4]);
							
							always.mycdv.mCircles.add(c);
						}
						else if(row.indexOf("===lines===")<0){
							String lineData[] = row.split(",");
							CircleArea start = null;
							CircleArea end = null;
							for(CircleArea c: always.mycdv.mCircles){
								if(c.id.equals(lineData[0])){
									start = c;
									
								}
								if(c.id.equals(lineData[1])){
									end = c;
								}
							}
							if(start != null && end != null){
								Lines l = new Lines(start.centerX,start.centerY,end.centerX,end.centerY);
								l.circleStart = start;
								l.circleEnd = end;
								l.name =lineData[2];
								start.addRelation(end, l.name);
								always.mycdv.mLines.add(l);
							}
							
						}
						else{
							String val = row.substring(0,row.indexOf("===lines==="));
							int n = Integer.parseInt(val);
							always.mycdv.number_of_nodes = n;
						}
					}
					always.mycdv.invalidate();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			  }
			});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		});

		alert.show();
    }
    
	public void saveFile(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Input file");
		alert.setMessage("Please give the name of the file");
	
		final EditText input = new EditText(this);
		alert.setView(input);
		final String fileName;
		final boolean cont = false;
		final Context actv = this;
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(DialogInterface dialog, int whichButton) {
		        String path = Environment.getExternalStorageDirectory().getAbsolutePath(); 
		              
		        File file = getAlbumStorageDir(input.getText().toString()+".txt");
		        Toast.makeText(getApplicationContext(), file.getAbsolutePath(),Toast.LENGTH_LONG).show();
		        try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					for(CircleArea c: always.mycdv.mCircles){
						bw.write(c.id+","+c.centerX+","+c.centerY+","+String.valueOf(c.isInitial)+","+String.valueOf(c.isFinal));
						bw.newLine();
					}
					bw.write(String.valueOf(always.mycdv.number_of_nodes)+"===lines===");
					bw.newLine();
					for(Lines l: always.mycdv.mLines.toArray(new Lines[0])){
						bw.write(l.circleStart.id+","+l.circleEnd.id+","+l.name);
						bw.newLine();
					}
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			  }
			});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		});

		alert.show();
	}
    public void save()
    {
    	this.always.mycdv.setDrawingCacheEnabled(true);
    	
    	
    	
    	
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Input file");
		alert.setMessage("Please give the name of the image");
	
		final EditText input = new EditText(this);
		alert.setView(input);
		final String fileName;
		final boolean cont = false;
		final Context actv = this;
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@SuppressWarnings("unchecked")
			public void onClick(DialogInterface dialog, int whichButton) {
				Bitmap bitmap = ( (MainActivity) actv).always.mycdv.getDrawingCache();    
		        String path = Environment.getExternalStorageDirectory().getAbsolutePath(); 
		              
		               File file = getAlbumStorageDir(input.getText().toString()+".png");
		               Toast.makeText(getApplicationContext(), file.getAbsolutePath(),Toast.LENGTH_LONG).show();
		                 try 
		                 {
		                     if(!file.exists())

		                 {
		                     file.createNewFile();
		                 }
		                     FileOutputStream ostream = new FileOutputStream(file);
		                     bitmap.compress(CompressFormat.PNG, 10, ostream);

		                     ostream.close();                            
		                 } 
		                 catch (Exception e) 
		                 {
		                     e.printStackTrace();
		                 }
			  }
			});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
			}
		});

		alert.show();
    	
    	
    	
    	
    
     

    }
    
    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory. 
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        
        return file;
    }
}
