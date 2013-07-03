/* Copyright (c) <2010>, <Alexander Chantavy>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <University of Hawaii at Manoa> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <Alexander Chantavy> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <Alexander Chantavy> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package alexchantavy;
/**
 * Each Vertex represents an (x, y) point
 * in 2-dimensional space. 
 * <p>Each Vertex also contains an ID number used to determine chord
 * connectivity when in a polygon.  The ID number is -1
 * if the Vertex is not associated with a Polygon.  However, if the 
 * Vertex <i>is</i> associated with a Polygon, then the ID number of 
 * the vertex is greater than 0.    
 * <p>This class also provides methods to compute cross products and 
 * determine relative directions of points.  These methods were implemented
 * from algorithms provided by the Cormen text.
 * @author Alex Chantavy
 * @author Jon Lai
 */
public class Vertex {
	double xaxis;
	double yaxis;
	private int id;
	
	/**
	 * Default constructor, create a vertex at (0,0).
	 */
	public Vertex(){
		this.xaxis = 0;
		this.yaxis = 0;
		this.id = -1;
	}
	
	/**
	 * Sets the ID of this Vertex to another id.
	 * If the ID of this vertex was initialized by a polygon,
	 * it is not possible to change the id, and the method returns
	 * false
	 * @param id The new id
	 * @return whether the change ID operation was successful
	 */
	public boolean setID(int id) {
		if (this.id > 0 ) {
			this.id = id;	
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the current Vertex's ID.
	 * @return the current Vertex's ID
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Create a Vertex at the given (x,y) points, 
	 * giving the Vertex a default ID of -1. 
	 * @param x The x coordinate 
	 * @param y The y coordinate
	 */
	public Vertex(double x, double y){
		this.xaxis = x;
		this.yaxis = y;
		this.id = -1;
	}
	
	/**
	 * Create a vertex with the given (x,y) points 
	 * with a specified id number.  id number is used for 
	 * identification with polygon clockwise ordering.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param id The ID number
	 */
	public Vertex(double x, double y, int id) {
		this.xaxis = x;
		this.yaxis = y;
		this.id = id;
	}
 	
	/**
	 * Returns string representation of the Vertex.
	 */
	public String toString() {
		return "(" + this.xaxis + ", " + this.yaxis + ")";
	}
	
	/**
	 * Determines whether this vertex is equivalent to another vertex.
	 * Two vertices are equivalent if their x and y coordinates are equal. 
	 * @param other The other vertex
	 * @return Whether the two vertices are equal
	 */
	public boolean equals (Vertex other) {
		return (this.xaxis == other.xaxis && this.yaxis == other.yaxis);
	}
	
	/**
	 * Determines the "cross product" of p1 and p2.
	 * Cross product is defined in page 1016 in the Cormen textbook 
	 * as the following:
	 * If we treat p1 and p2 as vectors such that p1 = <x1, y1> and
	 * p2 = <x2, y2>, the cross product is det([p1][p2]), where det
	 * denotes the determinant of a matrix. 
	 * @param p1 The first vertex
	 * @param p2 The second vertex
	 * @source <i>Introduction to Algorithms, 3rd edition</i>, Cormen, Rivest, Leiserson and Stein, page 1016
	 * @return The value x1y2 - x2y1
	 */
	public static double crossProduct(Vertex p1, Vertex p2) {
		return p1.xaxis*p2.yaxis -  p2.xaxis*p1.yaxis;
	}
	
	/**
	 * Determines if Vertex p1 is clockwise from Vertex p2 with respect
	 * to the origin.  
	 * @param p1 The first vertex
	 * @param p2 The second vertex
	 * @return 1 if true, -1 if false, 0 if p1 and p2 are collinear from the origin.
	 * @source <i>Introduction to Algorithms, 3rd edition</i>, Cormen, Rivest, Leiserson and Stein, page 1016
	 */
	public static int clockwiseFrom(Vertex p1, Vertex p2) {
		double crossProduct = Vertex.crossProduct(p1, p2);
		if (crossProduct > 0) {
			return 1; //yes, p1 is clockwise from p2 wrt the origin.
		}
		else if (crossProduct < 0) {
			return -1; //no, p1 is not clockwise from p2 wrt the origin.
		}
		else {
			return 0; //p1 and p2 are collinear wrt the origin.
		}
	}
	
	/**
	 * Determines if the line segment p0p1 is closer to p0p2 in clockwise or
	 * counterclockwise direction. Algorithm from pg 1017 of the Cormen text.
	 * @param p0 The "anchor point"
	 * @param p1 Test point
	 * @param p2 Target point
	 * @return The string "cw" if p0p1 is closer to p0p2 in clockwise direction,
	 * "ccw" if in counterclockwise direction, "collinear" if it doesn't matter.
	 * @source <i>Introduction to Algorithms, 3rd edition</i>, Cormen, Rivest, Leiserson and Stein, page 1017 
	 */
	public static String cw_or_ccw_or_collinear(Vertex p0, Vertex p1, Vertex p2) {
		Vertex p1_prime = new Vertex(p1.xaxis-p0.xaxis, p1.yaxis-p0.yaxis);  //adjust to use p0 as the origin
		Vertex p2_prime = new Vertex(p2.xaxis-p0.xaxis, p2.yaxis-p0.yaxis);
		double crossProduct = Vertex.crossProduct(p1_prime, p2_prime);
		if (crossProduct > 0) {
			return "clockwise";
		}
		else if (crossProduct < 0) {
			return "ccw";
		}
		else {
			return "collinear";
		}
	}

	/**
	 * Wrapper method for Vertex.direction.  Returns a string instead of a double.
	 * @see Vertex.direction()
	 * @param p0 The first point
	 * @param p1 The anchor point 
	 * @param p2 The last point
	 * @return "Left" if they turn left, "right" if right, "collinear" if the 
	 * points are on the same line.
	 */
	public static String leftOrRight (Vertex p0, Vertex p1, Vertex p2) {
		double direction = Vertex.direction(p0, p1, p2);
		if (direction < 0) {
			return "left";
		}
		else if (direction > 0) {
			return "right";
		}
		else {
			return "collinear";
		}
	}
	
	/**
	 * Returns true if p2 is right or collinear with line segment
	 * p0p1.  Used in O'Rourke's calculation
	 * @param p0 First point
	 * @param p1 Second point
	 * @param p2 Test point
	 * @return Whether p2 is right or collinear with p0p1
	 */
	public static boolean rightOn(Vertex p0, Vertex p1, Vertex p2) {
		return Vertex.direction(p0, p1, p2) >= 0;
	}
	
	/**
	 * Returns true if p2 is right with line segment p0p1.
	 * @param p0 First point
	 * @param p1 Second point
	 * @param p2 Test point
	 * @return Whether p2 is right of p0p1.
	 */
	public static boolean right (Vertex p0, Vertex p1, Vertex p2) {
		return Vertex.direction(p0, p1, p2) > 0;
	}
	
	/**
	 * Determines if the consecutive line segments p0p1 p1p2 turn left
	 * or right at point p1 using cross products.
	 * @param p0 The first point
	 * @param p1 The anchor point
	 * @param p2 The last point
	 * @return positive if p2 is right, negative if p2 is left, zero if collinear
	 */
	public static double direction (Vertex p0, Vertex p1, Vertex p2) {
		Vertex p1_prime = new Vertex(p1.xaxis-p0.xaxis, p1.yaxis-p0.yaxis);
		Vertex p2_prime = new Vertex(p2.xaxis-p0.xaxis, p2.yaxis-p0.yaxis);
		return Vertex.crossProduct(p2_prime, p1_prime);
	}
	
	/**
	 * Returns whether the three vertices are collinear.
	 * @param p0 The first Vertex
	 * @param p1 The second Vertex
	 * @param p2 The third Vertex
	 * @return Whether the three vertices lie on the same line
	 */
	public static boolean collinear(Vertex p0, Vertex p1, Vertex p2) {
		String result = Vertex.cw_or_ccw_or_collinear(p0, p1, p2);
		return result.equals("collinear");
	}
	
	/**
	 * Determine if p_k is on the segment (p_i to p_j).
	 * @param p_i First endpoint
	 * @param p_j Second endpoint
	 * @param p_k Test point
	 * @return True if p_k is between p_i to p_j, false otherwise.
	 * @source <i>Introduction to Algorithms, 3rd edition</i>, Cormen, Rivest, Leiserson and Stein, page 1018 
	 */
	public static boolean onSegment (Vertex p_i, Vertex p_j, Vertex p_k) {
		return ((Math.min(p_i.xaxis, p_j.xaxis) <= p_k.xaxis) && (p_k.xaxis <= Math.max(p_i.xaxis, p_j.xaxis))) &&
			  (((Math.min(p_i.yaxis, p_j.yaxis) <= p_k.yaxis) && (p_k.yaxis <= Math.max(p_i.yaxis, p_j.yaxis))));
	}
	
	/**
	 * Returns the vertex farthest right out of an array of 
	 * Vertices.
	 * @param vertices The array of vertices
	 * @return The vertex out of the set that is the farthest right
	 */
	public static Vertex getLargestX(Vertex[] vertices) {
		try {
			Vertex v = vertices[0];
			for (int i = 1; i < vertices.length; i++) {
				if (vertices[i].xaxis > v.xaxis) {
					v = vertices[i];
				}
			}
			return v;
		}
		catch (NullPointerException e) {
			return null;
		}
	}
	
	//commented out because not needed, but might be needed in future.
	/*
	/**
	 * Takes a non-polygon created chord and assigns its vertices 
	 * ID numbers, just like how a polygon would do it.
	 * @param chord The chord whose vertices to assign ID numbers
	 * @param vertices The array of vertices of the polygon to get ID numbers from
	 */
	/*
	public static void match_id(Chord chord, Vertex[] vertices) {
		Vertex v1, v2;
		v1 = chord.v1;
		v2 = chord.v2;
		for (int i = 0; i < vertices.length; i++) {
			if (v1.equals(vertices[i]) ) {
				v1.setID(vertices[i].getID());
			}
			else if (v2.equals(vertices[i])) {
				v2.setID(vertices[i].getID());
			}
		}
	}*/
		
	public static void main (String args[]) {
		Vertex origin = new Vertex (0,0);
		Vertex p1 = new Vertex(3, 2);
		Vertex p2 = new Vertex(2, 6);
		System.out.println("Assert: p1 is clockwise from p2\n" + Vertex.clockwiseFrom(p1, p2)+"\n");
		
		Vertex p3 = new Vertex(-2, 6);
		System.out.println("Assert: p2 is clockwise from p3\n" + Vertex.clockwiseFrom(p2, p3)+"\n");
		
		Vertex p4 = new Vertex(1, 3);
		System.out.println("Assert: p2 is collinear with p4\n" + Vertex.clockwiseFrom(p2, p4)+"\n");
		
		System.out.println("Assert: p4 is collinear with p2\n" + Vertex.clockwiseFrom(p4, p2)+"\n");
		
		System.out.println(Vertex.cw_or_ccw_or_collinear(p4, p2, origin));
		
		//does order of parameters matter?
		System.out.println(Vertex.collinear(p2, origin, p4));
		System.out.println(Vertex.collinear(p2, p4, origin));
		System.out.println(Vertex.collinear(p4, p2, origin));
		System.out.println(Vertex.collinear(p4, origin, p2));
		System.out.println(Vertex.collinear(origin, p2, p4));
		System.out.println(Vertex.collinear(origin, p4, p2)); //all return true!
		//so order does not matter.
		
		System.out.println(Vertex.collinear(p1, p4, p2));
		Vertex p5 = new Vertex(3, -1);
		Vertex p6 = new Vertex(4, -1);
		Vertex p7 = new Vertex(5, -1);
		System.out.println(Vertex.collinear(p5, p6, p7));
		System.out.println(Vertex.leftOrRight(p1, p4, p3));
		System.out.println(Vertex.leftOrRight(p4, p1, p2));
		System.out.println(Vertex.leftOrRight(p7, p6, p1));
		
		Vertex b1 = new Vertex(.5, -2);
		Vertex b2 = new Vertex(7.5, -2);
		Vertex b3 = new Vertex(9.5, 0);
		
		System.out.println(Vertex.leftOrRight(b1, b2, b3));
		
	}
}