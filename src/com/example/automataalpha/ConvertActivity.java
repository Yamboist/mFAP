package com.example.automataalpha;

import java.util.ArrayList;

import com.example.automataalpha.CirclesDrawingView.CircleArea;

import android.app.LauncherActivity.ListItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConvertActivity extends Fragment {
	CirclesDrawingView cdv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.convert_layout, container, false);
        Button toDFA = (Button) rootView.findViewById(R.id.btn_to_DFA);
       
        toDFA.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(rootView.getContext(), ToDFA.class);
				ArrayList<Node> nodes = new ArrayList<Node>();
				for(CircleArea c: cdv.mCircles){
					Node nd = (Node) c;
					nodes.add(nd);
				}
				
				
				myIntent.putExtra("results",nodes);
				myIntent.putExtra("initial",cdv.initial);
				rootView.getContext().startActivity(myIntent);
			}
        	
        });
        
        
        return rootView;
    }
}
