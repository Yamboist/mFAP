package com.example.automataalpha;





import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
		for(char current : string.toCharArray()){
			
			for(int i=0;i<frontiers.size();i++){
				going = false;
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
				
				going = true;
			}
			try{
				path.add(newFrontier.get(0));
			}catch(Exception e){}
			
			frontiers.clear();
			frontiers = newFrontier;
			
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
				
				System.out.println(going);
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
			System.out.println("IM TRAPPED");
			trapped.add(this.trap);
			return trapped;
		}
		return trapped;
	}
	
	
	public static ArrayList<Node> toDFA(ArrayList<Node> nodes){
		ArrayList<String> alphabet = new ArrayList<String>();
		
		for(Node node: nodes){
			for(String key: node.relations.keySet()){
				if(alphabet.indexOf(key) >=0);
				else{
					alphabet.add(key);
				}
			}
		}
		
		System.out.println("Alphabet: " + Arrays.toString(alphabet.toArray(new String[0])));
		
		int counter = 0;
		while(counter < nodes.size()){
		//for(Node node: nodes){
			Node node = nodes.get(counter);
			for(String key: node.relations.keySet()){
				if(node.relations.get(key).size() > 1){
					ArrayList<Relation> temp_relations = node.relations.get(key);
					String builder = "";
					for(Relation relation:  temp_relations.toArray(new Relation[0])){
						if(builder.indexOf(relation.dest.label)<0){
							builder += relation.dest.label+",";
						}
					}
					
					builder = builder.substring(0,builder.length() - 1);
					boolean to_add_or_not = true;
					for(Node node_temp: nodes){
						if(node_temp.label.equals(builder)){
							node.relations.get(key).clear();
							node.addRelation(node_temp, key);
							to_add_or_not = !to_add_or_not;
							
						}
					}
					
					
					if(to_add_or_not){
						Node newNode = new Node(builder);
						nodes.add(newNode);
						
						String splitted[] = builder.split(",");
						for(String q: splitted){
							for(Node node_in: nodes){
								if(node_in.label.equals(q)){
									if(node_in.isFinal){
										newNode.isFinal = true;
									}
									for(String key_in: node_in.relations.keySet()){
										ArrayList<Relation> rels = node_in.relations.get(key_in);
										for(Relation rel: rels.toArray(new Relation[0])){
											newNode.addRelation(rel.dest, rel.chr);
										}
									}
									break;
								}
							}
						}
						node.relations.get(key).clear();
						node.addRelation(newNode, key);
						
					}
					
					
				}
			}
			counter++;
			
		}
		
		return nodes;
	}
	
	
	public static ArrayList<Node> removeUnnecessary(ArrayList<Node> nodes){
		ArrayList<Node> withUse = new ArrayList<Node>();
		ArrayList<String> strings = new ArrayList<String>(); 
		for(Node node: nodes.toArray(new Node[0])){
			for(String key: node.relations.keySet()){
				if(strings.indexOf(node.relations.get(key).get(0).dest.label)<0){
					strings.add(node.relations.get(key).get(0).dest.label);
				}
				
			}
		}
		for(Node node: nodes.toArray(new Node[0])){
			if(strings.indexOf(node.label)>=0 || node.isInitial){
				withUse.add(node);
			}
		}
		return withUse;
	}
	
	public static void main(String args[]){
		Node q0 = new Node("q0");
		Node q1 = new Node("q1");
		Node q2 = new Node("q2");
		Node q3 = new Node("q3");
		
		q0.addRelation(q1, "a");
		q0.addRelation(q2,"a");
		
		q1.addRelation(q3, "b");
		q2.addRelation(q3, "b");
		
		q3.addRelation(q0, "a");
		
		ArrayList<Node> nodes  = new ArrayList<Node>();
		nodes.add(q0);
		nodes.add(q1);
		nodes.add(q2);
		nodes.add(q3);
		for(String dests: nodes.get(0).relations.keySet()){
			ArrayList<Relation> temp22 = nodes.get(0).relations.get(dests);
			System.out.print("["+dests+"]");
			for(Relation tempo: temp22.toArray(new Relation[0])){
				System.out.print("--- "+ tempo.dest.label);
			}
			System.out.println();
		}
		System.out.println("-=-=-=-=--=-=-=-=--=-=");
		nodes = Node.toDFA(nodes);
		
		for(Node nodex: nodes.toArray(new Node[0])){
			System.out.println(nodex.label);
		}
		/*for(String dests: nodes.get(0).relations.keySet()){
			ArrayList<Relation> temp22 = nodes.get(0).relations.get(dests);
			System.out.print("["+dests+"]");
			for(Relation tempo: temp22.toArray(new Relation[0])){
				System.out.print("--- "+ tempo.dest.label);
			}
			System.out.println();
		}*/
		
	}
	
	
	

	
}
