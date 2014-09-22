package com.example.automataalpha;

import java.util.ArrayList;

import com.example.automataalpha.CirclesDrawingView.CircleArea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputActivity extends Fragment {
	public CirclesDrawingView cdv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        final View rootView = inflater.inflate(R.layout.simluate_layout, container, false);
        Button btn_fast_run = (Button) rootView.findViewById(R.id.btn_fast_run);
        btn_fast_run.setOnClickListener(new OnClickListener(){
    	@Override
    	public void onClick(View view){
    		AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
			alert.setTitle("Input string");
			alert.setMessage("Please give the string to fast run");
		
			final EditText input = new EditText(view.getContext());
			alert.setView(input);
			
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@SuppressWarnings("unchecked")
				public void onClick(DialogInterface dialog, int whichButton) {
					
					if(cdv.initial == null){
						AlertDialog.Builder alert = new AlertDialog.Builder(rootView.getContext());
						alert.setTitle("Error");
						alert.setMessage("Please set the initial node; go back to the drawing tab, and use the node selector and pick a node to be a initial");
						alert.show();
					}
					else{
						Intent myIntent = new Intent(rootView.getContext(), FastRunActivity.class);
						ArrayList<Node> results = cdv.initial.dfa_traverseGetAll(input.getText().toString());
						ArrayList<String> results_txt = new ArrayList<String>();
						
						for(int i =0;i<results.size();i++){
							Log.w("--------------------------",results.get(i).label);
							results_txt.add(results.get(i).label);
							
						}
						
						myIntent.putStringArrayListExtra("nodes", results_txt);
						myIntent.putExtra("input", input.getText().toString());
						myIntent.putExtra("cdv", cdv.mCircles);
						myIntent.putExtra("cdv_init", cdv.initial);
						myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						if(results.get(results.size()-1).isFinal == true){
							myIntent.putExtra("final", true);
						}
						else{
							myIntent.putExtra("final", false);
						}
						
						rootView.getContext().startActivity(myIntent);
					}
					
				  }
				});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					
				}
			});

			alert.show();
    	}
        });
        
        Button btn_multi_run = (Button) rootView.findViewById(R.id.btn_multi_run);
        btn_multi_run.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(cdv.initial == null){
					Toast.makeText(arg0.getContext().getApplicationContext(), "Please specify initial node first", Toast.LENGTH_LONG).show();
				}
				else{
					Intent myIntent = new Intent(rootView.getContext(), MultiRunActivity.class);
					myIntent.putExtra("cdv", cdv.mCircles);
					myIntent.putExtra("cdv_init", cdv.initial);
					myIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					rootView.getContext().startActivity(myIntent);
				}
			}
        		
        });
        return rootView;
    }
}


