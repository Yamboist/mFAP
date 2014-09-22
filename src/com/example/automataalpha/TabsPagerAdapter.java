package com.example.automataalpha;


import com.example.automataalpha.DrawingActivity;
import com.example.automataalpha.InputActivity;
import com.example.automataalpha.ConvertActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
	DrawingActivity always;
	InputActivity inputs;
	ConvertActivity convert;
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return this.always;
        case 1:
            // Games fragment activity
        	this.inputs.cdv = this.always.mycdv;
            return this.inputs;
        case 2:
            // Movies fragment activity
            return this.convert;
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}