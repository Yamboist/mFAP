package com.example.automataalpha;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class FastRunActivity extends Activity {
	TableLayout table_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fast_run);
		
		table_layout = (TableLayout) findViewById(R.id.tbl_layout_fr);
		

		ArrayList<String> results = this.getIntent().getStringArrayListExtra("nodes");
		String input = this.getIntent().getStringExtra("input");
		@SuppressWarnings("unchecked")
		HashSet<CirclesDrawingView.CircleArea> circles = (HashSet<CirclesDrawingView.CircleArea>) this.getIntent().getSerializableExtra("cdv");
		CirclesDrawingView.CircleArea cdv_init = (CirclesDrawingView.CircleArea) this.getIntent().getSerializableExtra("cdv_init");
		String x0 = String.valueOf(cdv_init.traverse("111").get(0).isFinal);
		boolean judgement = this.getIntent().getBooleanExtra("final", false);
	
		
		TableRow tb_heads = new TableRow(this);
		TextView steps = new TextView(this);		
		steps.setText("Steps");
		steps.setTypeface(null, Typeface.BOLD);
		steps.setPadding(5, 5, 5, 5);
	    tb_heads.addView(steps);
	    
	    TextView nodes = new TextView(this);		
		nodes.setText("Nodes");
		nodes.setTypeface(null, Typeface.BOLD);
		nodes.setPadding(5, 5, 5, 5);
	    tb_heads.addView(nodes);
	    
	    TextView progress = new TextView(this);	
	    progress.setTypeface(null, Typeface.BOLD);
		progress.setText("Progress");
		progress.setPadding(5, 5, 5, 5);
	    tb_heads.addView(progress);
	    
	    
	    table_layout.addView(tb_heads);
		for(int row = 0 ; row < results.size(); row ++) {
			TableRow tb_row = new TableRow(this);
			if(judgement && row==results.size()-1){
				tb_row.setBackgroundColor(0xFFBDFFA7);
			}
			else if(row==results.size()-1){
				tb_row.setBackgroundColor(0xFFFF6666);
			}
			
			tb_row.setPadding(0, 10, 0, 10);
			
		
			TextView tv3 = new TextView(this);
		    tv3.setPadding(5, 5, 5, 5);
		    tv3.setText("step " + (row));
		    tb_row.addView(tv3); 
			
			
			TextView tv = new TextView(this);
			
		    tv.setText(results.get(row));
		    tv.setPadding(5, 5, 5, 5);
		    tb_row.addView(tv);
		    
		    Log.w("----------------------------------------",results.get(row));
		    TextView tv2 = new TextView(this);
		    tv2.setPadding(5, 5, 5, 5);
		    if(row==0){
		    	tv2.setText("start");
		    }
		    else{
		    	 tv2.setText(input.substring(0, row));
		    }
		   
		    tb_row.addView(tv2);
		    
		    
		    
			
			table_layout.addView(tb_row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		
		TableRow judge_row = new TableRow(this);
		TextView results_txt = new TextView(this);	
	    results_txt.setTypeface(null, Typeface.BOLD);
	    if(judgement){
	    	results_txt.setText("Accepted");
	    }
	    else{
	    	results_txt.setText("Rejected");
	    }
		
		results_txt.setPadding(5, 5, 5, 5);
	    judge_row.addView(results_txt);
		table_layout.addView(judge_row);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fast_run, menu);
		return true;
	}

	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
