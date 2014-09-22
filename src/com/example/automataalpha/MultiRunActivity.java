package com.example.automataalpha;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MultiRunActivity extends Activity {
	ArrayList<TableRow> tb_rows = new ArrayList<TableRow>();
	TableLayout tbl_layout;
	Context actv = this;
	CirclesDrawingView.CircleArea initial;
	public int getStatusBarHeight() {
		  int result = 0;
		  int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		  if (resourceId > 0) {
		      result = getResources().getDimensionPixelSize(resourceId);
		  }
		  return result;
		}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_multi_run);
		Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenHeight = size.y;
        
		tbl_layout = (TableLayout)findViewById(R.id.tbl_layout_mr);
		LayoutParams params =tbl_layout.getLayoutParams();
		params.height = screenHeight - getStatusBarHeight();
		
		
		
		TableRow tb_heads = new TableRow(this);
		TextView steps = new TextView(this);		
		steps.setText("Delete");
		steps.setTypeface(null, Typeface.BOLD);
		steps.setPadding(5, 5, 5, 5);
	    tb_heads.addView(steps);
	    
	    TextView nodes = new TextView(this);		
		nodes.setText("Input Text");
		nodes.setTypeface(null, Typeface.BOLD);
		nodes.setPadding(5, 5, 5, 5);
	    tb_heads.addView(nodes);
	    
	    TextView progress = new TextView(this);	
	    progress.setTypeface(null, Typeface.BOLD);
		progress.setText("Status");
		progress.setPadding(5, 5, 5, 5);
	    tb_heads.addView(progress);
	    
	    tb_rows.add(tb_heads);
	    tbl_layout.addView(tb_heads);
	    
	    
	    
	    initial =(CirclesDrawingView.CircleArea) this.getIntent().getSerializableExtra("cdv_init");
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.multi_run_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		switch (id) {
		case R.id.add_row:
            // search action
			redisplay(true);
            return true;
		case R.id.delete_row:
			for(int i=1;i<tb_rows.size();i++){
				CheckBox temp = (CheckBox)tb_rows.get(i).getChildAt(0);
				if(temp.isChecked()){
				
					tb_rows.set(i, null);
				}
				
			}
			tb_rows.removeAll(Collections.singleton(null));
			redisplay(false);
			return true;
		case R.id.run:
			
			for(int i=1;i<tb_rows.size();i++){
				EditText temp = (EditText)tb_rows.get(i).getChildAt(1);
				TextView verdict = (TextView)tb_rows.get(i).getChildAt(2);
				verdict.setTypeface(null, Typeface.BOLD);
				ArrayList<Node> q0 = initial.traverse(temp.getText().toString());
				if(q0.get(q0.size()-1).isFinal){
					verdict.setTextColor(0xFFBDFFA7);
					verdict.setText("Accepted");
					
				}
				else{
					verdict.setTextColor(0xFFFF6666);
					verdict.setText("Rejected");
				}
				
			}
			redisplay(false);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void redisplay(boolean add){
		tbl_layout.removeAllViews();
		if(add){
		tb_rows.add(new TableRow(actv));
		
		CheckBox cbx = new CheckBox(actv);
		EditText etx = new EditText(actv);
		TextView txv = new TextView(actv);
		
		tb_rows.get(tb_rows.size()-1).addView(cbx);
		tb_rows.get(tb_rows.size()-1).addView(etx);
		tb_rows.get(tb_rows.size()-1).addView(txv);
		}
		for(int i=0;i<tb_rows.size();i++){
			tbl_layout.addView(tb_rows.get(i),new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}
}
