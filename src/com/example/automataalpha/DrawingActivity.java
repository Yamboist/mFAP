package com.example.automataalpha;

import java.io.Serializable;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DrawingActivity extends Fragment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 905541670191240731L;
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
