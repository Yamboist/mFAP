package com.example.automataalpha;
import java.util.ArrayList;
import java.util.HashMap;


public class Node {
	String label;
	HashMap<String,ArrayList<Relation>> relations = new HashMap<String,ArrayList<Relation>>();
	boolean isFinal = false;
	boolean isInitial = false;
	
	
	public Node(String label){
		this.label = label;
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
	
	public ArrayList<Node> traverse(String string){
		ArrayList<Node> frontiers = new ArrayList<Node>();
		ArrayList<Node> newFrontier = new ArrayList<Node>();
		ArrayList<Node> trapped = new ArrayList<Node>();
		frontiers.add(this);
		for(char current : string.toCharArray()){
			
			for(int i=0;i<frontiers.size();i++){
				
				newFrontier = new ArrayList<Node>();
				Node temp[] = frontiers.get(i).next(String.valueOf(current));
				
				if(temp != null){
					for(int nodeIndex=0;nodeIndex<temp.length;nodeIndex++){
						newFrontier.add(temp[nodeIndex]);
					}
					
				}
				else{
					trapped.add(frontiers.get(i));
				}
				
				
			}
			
			frontiers.clear();
			frontiers = newFrontier;
			
		}
		trapped.addAll(frontiers);
		return trapped;
	}
	
	public static void main(String args[]){
		Node q1 = new Node("q1");
		Node q2 = new Node("q2");
		Node q3 = new Node("q3");
		Node q4 = new Node("q4");
		
		q1.isInitial = true;
		q4.isFinal = true;
		
		q1.addRelation(q2, "0");
		q2.addRelation(q4, "0");
		q1.addRelation(q3, "1");
		q3.addRelation(q2, "1");
		q3.addRelation(q1,"0");
		
		ArrayList<Node> result = q1.traverse("100");
		
		for(int i=0;i<result.size();i++){
			System.out.print(result.get(i).label);
		}
	}
	
}
