package com.example.automataalpha;

import java.util.*;

import com.example.automataalpha.CirclesDrawingView.CircleArea;
import com.example.automataalpha.CirclesDrawingView.Lines;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ToDFA extends Activity {
	CirclesDrawingView cdv;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_df);
		/*HashSet<CircleArea> mCircles_o = (HashSet<CircleArea>) 
		ArrayList<Node> circs = new ArrayList<Node>();
		for(CircleArea c: mCircles_o){
			circs.add((Node) c);
		}*/
		ArrayList<Node> res = (ArrayList<Node>) this.getIntent().getSerializableExtra("results");
		CircleArea init = null;
		try{
			init = (CircleArea)this.getIntent().getSerializableExtra("initial");
		}
		catch(Exception e){}
		ArrayList<Node> results = Node.toDFA(res);
		results = Node.removeUnnecessary(results);
		ArrayList<CircleArea> mCir= new ArrayList<CircleArea>();
		this.cdv = (CirclesDrawingView) findViewById(R.id.mycdv);
		Log.w("--------------",String.valueOf(results.size()));
		int row = 0;
		int col = 0;
		for(Node node: results.toArray(new Node[0])){
			Log.w("[NAME]: ",node.label);
			CircleArea newCircle = new CircleArea(row*80+50,col*100+80,40,node.label);
			newCircle.label = node.label;
			newCircle.id = node.label;
			if(node.isFinal){
				newCircle.isFinal = true;
			}
			if(node.isInitial){
				this.cdv.initial = newCircle;
			}
			if(row%4 == 0 && row!=0) {
				row = 0;
				col+= 1;
			}
			row+= 1;
			cdv.mCircles.add(newCircle);
			mCir.add(newCircle);
		}
		
		int i = 0;
		for(CircleArea cc: mCir.toArray(new CircleArea[0])){
			Node similar = results.get(i);
			for(String key: similar.relations.keySet()){
				for(Relation rel: similar.relations.get(key).toArray(new Relation[0])){
					for(CircleArea cc_in: cdv.mCircles){
						if(rel.dest.label.equals(cc_in.id)){
							cc.addRelation(cc_in, key);
						}
					}
				}
			}
			i+=1;
		}
		
		for(CircleArea circle: cdv.mCircles){
			if(init != null){
				if(init.id == circle.id)
				{
					cdv.initial = circle;
				}
			}
			for(String key: circle.relations.keySet()){
				for(Relation rel: circle.relations.get(key).toArray(new Relation[0])){
					CircleArea opp = null;
					for(CircleArea circle2: cdv.mCircles){
						if(circle2.id == rel.dest.label){
							opp = circle2;
							break;
						}
					}
					Log.w("[centers]: ",  circle.centerX+" "+circle.centerY+"=||="+opp.centerX+" "+opp.centerY);
					Lines l = new Lines(circle.centerX,circle.centerY,opp.centerX,opp.centerY);
					l.name = key;
					l.circleStart = circle;
					l.circleEnd = opp;
					this.cdv.mLines.add(l);
				}
			}
			
		}
		cdv.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_d, menu);
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
