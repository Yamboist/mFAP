package com.example.automataalpha;

public class Relation {
	Node src;
	Node dest;
	String chr;
	
	public Relation(Node src, Node dest, String chr){
		this.src = src;
		this.dest = dest;
		this.chr = chr;
	}
}
