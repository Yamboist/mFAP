package com.example.automataalpha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7916984637998357153L;
	String label;
	HashMap<String,ArrayList<Relation>> relations = new HashMap<String,ArrayList<Relation>>();
	boolean isFinal = false;
	boolean isInitial = false;
	Node trap;
	
	public Node(String label){
		this.label = label;
		if(this.label != "trap"){
			this.trap = new Node("trap");
		}
	}
	
	public void addRelation(Node dest, String chr){
		if(relations.containsKey(chr)){
			relations.get(chr).add(new Relation(this,dest,chr));
		}
		else{
			ArrayList<Relation> temp = new ArrayList<Relation>();
			temp.add(new Relation(this,dest,chr));
			relations.put(chr, temp);
		}
	}
	
	public void removeRelation(Node end, String chr){
		ArrayList<Relation> rels = relations.get(chr);
		for(int i=0;i<rels.size();i++){
			if(rels.get(i).dest == end){
				relations.get(chr).remove(rels.get(i));
				
				break;
			}
		}
	}
	
	public Node[] next(String chr){
		ArrayList<Relation> directions = relations.get(chr);
		if(directions != null){
			Node[] nodes = new Node[directions.size()];
			for(int i=0;i<directions.size();i++){
				nodes[i] = directions.get(i).dest;
			}
			return nodes;
		}
		return null;
		
	}
	
	public ArrayList<Node> dfa_traverseGetAll(String string){
		ArrayList<Node> frontiers = new ArrayList<Node>();
		ArrayList<Node> newFrontier = new ArrayList<Node>();
		ArrayList<Node> trapped = new ArrayList<Node>();
		ArrayList<Node> path = new ArrayList<Node>();
		frontiers.add(this);
		path.add(frontiers.get(0));
		boolean going = false;
		int size = string.toCharArray().length-1;
		int index = 0;
		for(char current : string.toCharArray()){
			going = false;
			for(int i=0;i<frontiers.size();i++){
				
				newFrontier = new ArrayList<Node>();
				Node temp[] = frontiers.get(i).next(String.valueOf(current));
				
				if(temp != null){
					for(int nodeIndex=0;nodeIndex<temp.length;nodeIndex++){
						newFrontier.add(temp[nodeIndex]);
						going = true;
					}
					
				}
				else{
					trapped.add(frontiers.get(i));
					
				}
				
				
			}
			try{
				path.add(newFrontier.get(0));
			}catch(Exception e){}
			
			frontiers.clear();
			frontiers = newFrontier;
			if(!going && index < size){
				break;
			}
			index++;
		}
		if(!going){
			path.add(this.trap);
		}
		return path;
	}
	
	
	public ArrayList<Node> traverse(String string){
		ArrayList<Node> frontiers = new ArrayList<Node>();
		ArrayList<Node> newFrontier = new ArrayList<Node>();
		ArrayList<Node> trapped = new ArrayList<Node>();
		frontiers.add(this);
		boolean going = true;
		int size = string.toCharArray().length-1;
		int index = 0;
		for(char current : string.toCharArray()){
			going = false;
			for(int i=0;i<frontiers.size();i++){
				System.out.println(frontiers.get(0).label);
				newFrontier = new ArrayList<Node>();
				Node temp[] = frontiers.get(i).next(String.valueOf(current));
				
				if(temp != null){
					for(int nodeIndex=0;nodeIndex<temp.length;nodeIndex++){
						newFrontier.add(temp[nodeIndex]);
						going = true;
					}
					
				}
				else{
					trapped.add(frontiers.get(i));
				}
				
				
			}
			
			frontiers.clear();
			frontiers = newFrontier;
			if(!going && index < size){
				break;
			}
			index++;
		}
		
		trapped.addAll(frontiers);
		if(!going){
			trapped.add(this.trap);
			return trapped;
		}
		return trapped;
	}
	

	
}
