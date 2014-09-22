package com.example.automataalpha;

import java.io.Serializable;

public class Relation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5494874515405336519L;
	Node src;
	Node dest;
	String chr;
	
	public Relation(Node src, Node dest, String chr){
		this.src = src;
		this.dest = dest;
		this.chr = chr;
	}
}
