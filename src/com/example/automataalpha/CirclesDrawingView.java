package com.example.automataalpha;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.ToggleButton;

public class CirclesDrawingView extends View {
	
    private static final String TAG = "CirclesDrawingView";

    /** Main bitmap */
    private Bitmap mBitmap = null;
    public int mode = 0;
    private Rect mMeasuredRect;
    private int number_of_nodes = 0;
    public CircleArea initial;
    
    /** Stores data about single circle */
    public static class CircleArea extends Node {
        int radius;
        int centerX;
        int centerY;
        String id;
        ArrayList<Lines> myLines = new ArrayList<Lines>();
        
        CircleArea(int centerX, int centerY, int radius,String id) {
        	super("q"+id);
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
            this.id = "q"+id;
            
        }

        @Override
        public String toString() {
            return "Circle[" + centerX + ", " + centerY + ", " + radius + "]";
        }
    }
    
    public static class Lines{
    	int startX,startY,endX,endY;
    	CircleArea circleStart = null;
    	CircleArea circleEnd = null;
    	String name;
    	int textCoordsX,textCoordsY;
    	Lines(int startX,int startY,int endX,int endY){
    		
    		this.startX = startX;
    		this.startY = startY;
    		this.endX = endX;
    		this.endY = endY;
    	}
    }

    
    /** Paint to draw circles */
    private Paint mCirclePaint;

    private final Random mRadiusGenerator = new Random();
    // Radius limit in pixels
    private final static int RADIUS = 40;

    private static final int CIRCLES_LIMIT = 20;

    /** All available circles */
    private ArrayList<Lines> mLines = new ArrayList<Lines>();
    private HashMap<String,Lines> corr = new HashMap<String,Lines>();
    private HashSet<CircleArea> mCircles = new HashSet<CircleArea>(CIRCLES_LIMIT);
    private SparseArray<CircleArea> mCirclePointer = new SparseArray<CircleArea>(CIRCLES_LIMIT);

    /**
     * Default constructor
     *
     * @param ct {@link android.content.Context}
     */
    public CirclesDrawingView(final Context ct) {
        super(ct); 
        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        init(ct);
    }

    public CirclesDrawingView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);
        init(ct);
    }

    private void init(final Context ct) {
        // Generate bitmap used for background
        mBitmap = BitmapFactory.decodeResource(ct.getResources(), R.drawable.bg);

        mCirclePaint = new Paint();

        mCirclePaint.setColor(Color.YELLOW);
        mCirclePaint.setStrokeWidth(40);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        
    }

    @Override
    public void onDraw(final Canvas canv) {
        // background bitmap to cover all area
        //canv.drawBitmap(mBitmap, null, mMeasuredRect, null);
    	
        for (CircleArea circle : mCircles) {
        	mCirclePaint.setColor(Color.GRAY);
        	canv.drawCircle(circle.centerX, circle.centerY, circle.radius+2, mCirclePaint);
        	mCirclePaint.setColor(0xFFBFFFEF);
            canv.drawCircle(circle.centerX, circle.centerY, circle.radius, mCirclePaint);
            if(circle.isFinal){
            	mCirclePaint.setColor(Color.GRAY);
            	mCirclePaint.setStyle(Paint.Style.STROKE);
            	mCirclePaint.setStrokeWidth(2);
            	canv.drawCircle(circle.centerX, circle.centerY, circle.radius-3, mCirclePaint);
            	mCirclePaint.setStrokeWidth(40);
            	mCirclePaint.setStyle(Paint.Style.FILL);
            }
            if(circle == this.initial){
            	mCirclePaint.setColor(Color.BLACK);
            	Path path = new Path();
            	path.moveTo(circle.centerX-circle.radius,circle.centerY);
            	path.lineTo(circle.centerX-circle.radius*2,(float) (circle.centerY - circle.radius/1.5));
            	path.lineTo(circle.centerX-circle.radius*2, (float) (circle.centerY+ circle.radius/1.5));
      
            	canv.drawPath(path, mCirclePaint);
            }
            mCirclePaint.setTextSize(23);
            mCirclePaint.setColor(Color.BLACK);
            canv.drawText(circle.id,circle.centerX-12,circle.centerY+8,mCirclePaint);
        }
        if(mLines.size() >= 1){
        	mCirclePaint.setStrokeWidth(1);
        	if(mode == 2 || mode == 3){
	        	for(int i=0;i<mLines.size();i++){
	    
	        		
	        			if(mLines.get(i).circleEnd != null){
		        			mCirclePaint.setColor(Color.BLACK);
			            	mCirclePaint.setStrokeWidth(1);
			            	int points[] = this.getPoints(
			            			mLines.get(i).circleStart.centerX,
			            			mLines.get(i).circleStart.centerY,
			            			mLines.get(i).circleEnd.centerX, 
			            			mLines.get(i).circleEnd.centerY);
			            	
			            	canv.drawLine(
			            			points[0],points[1],points[2],points[3], 
			            			mCirclePaint);	
			            	canv.drawCircle(points[2], points[3], 4, mCirclePaint);
			            	
			            	if(mLines.get(i).name != null){
				            	mCirclePaint.setTextSize(20);
				            	
				            	mLines.get(i).textCoordsX = (points[0]+points[2])/2;
				            	mLines.get(i).textCoordsY = (points[1]+points[3])/2;
				            	mCirclePaint.setColor(Color.BLUE);
				                canv.drawText(mLines.get(i).name,mLines.get(i).textCoordsX-5,mLines.get(i).textCoordsY+3,mCirclePaint);
				                mCirclePaint.setColor(Color.BLACK);
				                
				                mCirclePaint.setTextSize(12);
			            	}
		        		}
		        		else{
		        			canv.drawLine(
		    	        			mLines.get(mLines.size()-1).startX, 
		    	        			mLines.get(mLines.size()-1).startY,
		    	        			mLines.get(mLines.size()-1).endX, 
		    	        			mLines.get(mLines.size()-1).endY, 
		    	        			mCirclePaint);
		        		}
	        		}
	        		
	        		
	            	
	        	
	        
	        	
        	}
        	else if(mode == 1 || mode ==0){
        		for(int i=0;i<mLines.size();i++){
	            	mCirclePaint.setColor(Color.BLACK);
	            	mCirclePaint.setStrokeWidth(1);
	            	int points[] = this.getPoints(
	            			mLines.get(i).circleStart.centerX,
	            			mLines.get(i).circleStart.centerY,
	            			mLines.get(i).circleEnd.centerX, 
	            			mLines.get(i).circleEnd.centerY);
	            	
	            	canv.drawLine(
	            			points[0],points[1],points[2],points[3], 
	            			mCirclePaint);
	            	
	            	mCirclePaint.setTextSize(20);
	            	
	            	canv.drawCircle(points[2], points[3], 4, mCirclePaint);
	            	mLines.get(i).textCoordsX = (points[0]+points[2])/2;
	            	mLines.get(i).textCoordsY = (points[1]+points[3])/2;
	            	mCirclePaint.setColor(Color.BLUE);
	                canv.drawText(mLines.get(i).name,mLines.get(i).textCoordsX-5,mLines.get(i).textCoordsY+3,mCirclePaint);
	                mCirclePaint.setColor(Color.BLACK);
	                
	                mCirclePaint.setTextSize(12);
	            }	
        	}
        }
        
    }

    /*
     * 
     * 
     * */
    public int[] getPoints(int p1X,int p1Y,int p2X,int p2Y){
    	int dx = p2X - p1X;
    	int dy = p2Y - p1Y;
    	int dx_inv = p1X - p2X;
    	int dy_inv = p1Y - p2Y;
    	int xValue1,yValue1,xValue2,yValue2,xArrow1=0,yArrow1=0,xArrow2=0,yArrow2=0;
    	int d = (int) Math.sqrt(Math.abs(dx*dx) + Math.abs(dy*dy));
    	int x = Math.abs((int) ((double)(dx*RADIUS)/d));
    	int y = Math.abs((int) ((double)(dy*RADIUS)/d));
    	if(dx > 0){
    		xValue1 = p1X + x;	
    	}
    	else{
    		xValue1 = p1X - x;
    	}
    	if(dy > 0){
    		yValue1 = p1Y + y;	
    	}
    	else{
    		yValue1 = p1Y - y;
    	}
    	
    	if(dx_inv < 0){
    		xValue2 = p2X - x;
    		
    	}
    	else{
    		xValue2 = p2X + x;
    	}
    	if(dy_inv < 0){
    		 yValue2 = p2Y - y;
    	}
    	else{
    		yValue2 = p2Y +y;
    	}
    	
    	
    	if(dx > 0 && dy>0){
    		xArrow1 = p2X - 2*x;
    		yArrow1 = p2Y - y;
    		xArrow2 = p2X - x;
    		yArrow2 = p2Y - 2*y;

    	}
    	else if(dx>0 && dy<0){
    		xArrow1 = xValue2 - 6;xArrow2 = xValue2 - 6;
    		yArrow1 = yValue2 + 6;yArrow2 = yValue2 - 6;
    	}
    	else if(dx<0 && dy<0){
    		xArrow1 = xValue2 + 6;xArrow2 = xValue2 + 6;
    		yArrow1 = yValue2 + 6;yArrow2 = yValue2 - 6;
    	}
    	else if(dx<0 && dy>0){
    		xArrow1 = xValue2 + 6;xArrow2 = xValue2 + 6;
    		yArrow1 = yValue2 + 6;yArrow2 = yValue2 - 6;
    	}
    	
    	
    	int[] returnable = {xValue1,yValue1,xValue2,yValue2,xArrow1,yArrow1,xArrow2,yArrow2};
    	return returnable;
    }
    
    @SuppressLint("NewApi")
	@Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;

        CircleArea touchedCircle;
        int xTouch;
        int yTouch;
        int pointerId;
        int actionIndex = event.getActionIndex();

        // get touch event coordinates and make transparent circle from it
        switch (event.getActionMasked()) {
        
            case MotionEvent.ACTION_DOWN:
            	if(this.mode == 0){
            		xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
            		
            		// check if we've touched inside some circle
                    touchedCircle = obtainTouchedCircle(xTouch, yTouch);
                    
                    if(touchedCircle != null){
                    	final PopupWindow pwindo;
                    	LayoutInflater inflater = (LayoutInflater) this.getContext()
                    			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    	View layout = inflater.inflate(R.layout.popup,
                    	(ViewGroup) findViewById(R.id.OG));
                    	layout.setBackgroundColor(0xFFF9FB67);
                    	pwindo = new PopupWindow(layout, 400, 500, true);
                    	pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

                    	Button btn_closepopup=(Button)layout.findViewById(R.id.dismissbtn);
                    	
                    	
                    	btn_closepopup.setOnClickListener(new OnClickListener() {

                    	    @Override
                    	    public void onClick(View arg0) {
                    	    // TODO Auto-generated method stub
                    	    	pwindo.dismiss();
                    	    }
                    	    });
                    
                    	
                    	
                    	Button btn_init = (Button) layout.findViewById(R.id.btn_init);
                    	final CircleArea touched = touchedCircle;
                    	final CirclesDrawingView class_view = this;
                    	btn_init.setOnClickListener(new OnClickListener(){
                    		@Override
                    		public void onClick(View view){
                    			 class_view.initial = touched;
                    			 pwindo.dismiss();
                    			 invalidate();
                    		}     	
                    	});
                    	
                    	
                    	
                    	final Switch btn_final = (Switch) layout.findViewById(R.id.switch_final);
                    	if(touched.isFinal){
            				btn_final.setChecked(true);
            			}
                    	btn_final.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    	        // do something, the isChecked will be
                    	        // true if the switch is in the On position
                    	    	
                    	    	touched.isFinal = !touched.isFinal;
                    	    	invalidate();
                    	    	pwindo.dismiss();
                    	    }
                    	});
                    	
                    	
                    	final Button btn_set_label = (Button) layout.findViewById(R.id.btn_set_label);
                    	btn_set_label.setOnClickListener(new OnClickListener(){
                    		@Override
                    		public void onClick(View view){
                    			
                    		}     	
                    	});
                    	
                    }
            	}
            	//draw circle mode
            	if(this.mode == 1 || this.mode == 0){
            		 // it's the first pointer, so clear all existing pointers data
                    clearCirclePointer();

                    xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
                    
                    // check if we've touched inside some circle
                    touchedCircle = obtainTouchedCircle(xTouch, yTouch);
                    if(touchedCircle!=null){
	                    touchedCircle.centerX = xTouch;
	                    touchedCircle.centerY = yTouch;
	                    mCirclePointer.put(event.getPointerId(0), touchedCircle);
                    }
                    invalidate();
                    handled = true;
            	}
            	//draw lines
            	else if(this.mode == 2){
            		xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
            		touchedCircle = obtainTouchedCircle(xTouch,yTouch);
            		
            		if(touchedCircle != null){
            			Lines newLine = new Lines(touchedCircle.centerX,touchedCircle.centerY,xTouch,yTouch);
            			newLine.circleStart = touchedCircle;
            			touchedCircle.myLines.add(newLine);
            			mLines.add(newLine);
            			
            			invalidate();
            			handled = true;
            		}
            	}
            	//delete
            	else if(this.mode == 3){
            		xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
            		touchedCircle = obtainTouchedCircle(xTouch,yTouch);
            		if(touchedCircle != null){
	            		for(int i=0;i<touchedCircle.myLines.size();i++){
	            			touchedCircle.myLines.get(i).circleEnd.myLines.remove(touchedCircle.myLines.get(i));
	            			mLines.remove(touchedCircle.myLines.get(i));
	            			
	            		}
	            		mCircles.remove(touchedCircle);
	            		touchedCircle = null;
            		}
	          
            		invalidate();
            		
            		handled = true;
            	}
            	
               
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if(this.mode == 1){
                	Log.w(TAG, "Pointer down");
                    // It secondary pointers, so obtain their ids and check circles
                    pointerId = event.getPointerId(actionIndex);

                    xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);

                    // check if we've touched inside some circle
                    touchedCircle = obtainTouchedCircle(xTouch, yTouch);

                    mCirclePointer.put(pointerId, touchedCircle);
                    touchedCircle.centerX = xTouch;
                    touchedCircle.centerY = yTouch;
                    invalidate();
                    handled = true;
                }
                //draw lines
            	else if(this.mode == 2){
            		xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
            		touchedCircle = obtainTouchedCircle(xTouch,yTouch);
            		
            		if(touchedCircle != null){
            			Lines newLine = new Lines(touchedCircle.centerX,touchedCircle.centerY,xTouch,yTouch);
            			mLines.add(newLine);
            			handled = true;
            		}
            	}
            	
                break;

            case MotionEvent.ACTION_MOVE:
            	
            	if(this.mode == 1 || this.mode == 0){
            		final int pointerCount = event.getPointerCount();

                    for (actionIndex = 0; actionIndex < pointerCount; actionIndex++) {
                        // Some pointer has moved, search it by pointer id
                        pointerId = event.getPointerId(actionIndex);

                        xTouch = (int) event.getX(actionIndex);
                        yTouch = (int) event.getY(actionIndex);
                        
                        touchedCircle = mCirclePointer.get(pointerId);

                        if (null != touchedCircle) {
                            touchedCircle.centerX = xTouch;
                            touchedCircle.centerY = yTouch;
                        }
                    }
                    invalidate();
                    handled = true;
            	}
            	
            	else if(this.mode == 2){
         
            		xTouch = (int) event.getX(actionIndex);
                    yTouch = (int) event.getY(actionIndex);
                    
                    Lines currentLine = mLines.get(mLines.size()-1);
            		currentLine.endX = xTouch;
            		currentLine.endY = yTouch;
            		
            		
            		invalidate();
            		handled = true;
            	}
                
                break;

            case MotionEvent.ACTION_UP:
            	
            	if (this.mode == 1){
            		clearCirclePointer();
                    invalidate();
                    handled = true;
            	}
            	else if(this.mode == 2){
            		xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);
            		touchedCircle = obtainTouchedCircle(xTouch,yTouch);
            		
            		if(touchedCircle != null){
            			final Lines currentLine = mLines.get(mLines.size()-1);
            			touchedCircle.myLines.add(mLines.get(mLines.size()-1));
            			currentLine.endX = touchedCircle.centerX;
            			currentLine.endY = touchedCircle.centerY;
            			currentLine.circleEnd = touchedCircle;
            			
            			
            			
            			
            			
            			
            			AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
            			alert.setTitle("Input symbol");
            			alert.setMessage("Please give the input symbol for the two terminals");
            			
            			final EditText input = new EditText(this.getContext());
            			alert.setView(input);
            			
            			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            				public void onClick(DialogInterface dialog, int whichButton) {
            					Editable value = input.getText();
            					currentLine.name = value.toString();
            					currentLine.textCoordsX = (currentLine.startX+currentLine.endX)/2;
            					currentLine.textCoordsY = (currentLine.startY+currentLine.endY)/2;
            					
            					CircleArea mytouchedCircle = currentLine.circleEnd;
            					currentLine.circleStart.addRelation(mytouchedCircle, currentLine.name);
            				  }
            				});

            			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            				public void onClick(DialogInterface dialog, int whichButton) {
            					mLines.remove(mLines.size()-1);
            					invalidate();
            				}
            			});

            			alert.show();
            			
            			
            		}
            		else{
            			mLines.get(mLines.size()-1).circleStart.myLines.remove(mLines.get(mLines.size()-1));
            			mLines.remove(mLines.size()-1);
            		}
            		invalidate();
            		handled = true;
            		
            		
            	}
            	else if(mode == 3){
            		invalidate();
            		handled = true;
            	}
                break;

            case MotionEvent.ACTION_POINTER_UP:
            	if(this.mode == 1){
            		 pointerId = event.getPointerId(actionIndex);

                     mCirclePointer.remove(pointerId);
                     invalidate();
                     handled = true;
            	}
            	
                // not general pointer was up
               
                break;

            case MotionEvent.ACTION_CANCEL:
            	if(this.mode == 1){
            		handled = true;
            	}
                
                break;

            default:
                // do nothing
                break;
        }

        return super.onTouchEvent(event) || handled;
    }

    /**
     * Clears all CircleArea - pointer id relations
     */
    private void clearCirclePointer() {
        Log.w(TAG, "clearCirclePointer");

        mCirclePointer.clear();
    }

    /**
     * Search and creates new (if needed) circle based on touch area
     *
     * @param xTouch int x of touch
     * @param yTouch int y of touch
     *
     * @return obtained {@link CircleArea}
     */
    private CircleArea obtainTouchedCircle(final int xTouch, final int yTouch) {
        CircleArea touchedCircle = getTouchedCircle(xTouch, yTouch);

        if (null == touchedCircle && mode == 1) {
            touchedCircle = new CircleArea(xTouch, yTouch,RADIUS,String.valueOf(number_of_nodes));
            number_of_nodes++;
            
            if (mCircles.size() == CIRCLES_LIMIT) {
                Log.w(TAG, "Clear all circles, size is " + mCircles.size());
                // remove first circle
                mCircles.clear();
            }

            Log.w(TAG, "Added circle " + touchedCircle);
            mCircles.add(touchedCircle);
        }
 
        return touchedCircle;
    }

    /**
     * Determines touched circle
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     *
     * @return {@link CircleArea} touched circle or null if no circle has been touched
     */
    private CircleArea getTouchedCircle(final int xTouch, final int yTouch) {
        CircleArea touched = null;

        for (CircleArea circle : mCircles) {
            if ((circle.centerX - xTouch) * (circle.centerX - xTouch) + (circle.centerY - yTouch) * (circle.centerY - yTouch) <= circle.radius * circle.radius) {
                touched = circle;
                break;
            }
        }

        return touched;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredRect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }
}
