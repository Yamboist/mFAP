package com.example.automataalpha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrawingActivity extends Fragment {
	public CirclesDrawingView mycdv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.drawing_layout, container, false);
        mycdv = (CirclesDrawingView) rootView.findViewById(R.id.cdv);
       
        return rootView;
    }
    
    
    
}
