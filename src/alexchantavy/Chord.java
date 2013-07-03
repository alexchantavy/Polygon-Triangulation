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
import java.util.ArrayList;

/**
 * Data structure that mimics the structure of a Polygon chord.
 * Uses algorithms from Cormen and O'Rourke to compute line 
 * intersection and diagonal validity.  Algorithm sources 
 * cited near appropriate subroutines.
 * <p>A Chord is a line segment containing two vertices in the 
 * x,y plane with a length.
 * @author Alex Chantavy
 */
public class Chord implements Comparable<Chord> {
	protected Vertex v1, v2;
	protected double length = 0;
	
	/**
	 * Creates a Chord from the given set of Vertices.  
	 * @param v1_id The first id of the vertex to form the chord
	 * @param v2_id The second id of the vertex to form the chord
	 * @param vertices The array of vertices that define the polygon
	 */
	public Chord (int v1_id, int v2_id, Vertex[] vertices) {
		this(vertices[v1_id], vertices[v2_id]) ;
	}
	
	/**
	 * Creates a chord from the two Vertices v1 and v2
	 * @param v1 The first vertex
	 * @param v2 The second vertex
	 */
	public Chord (Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
		length = Math.sqrt(Math.pow(((double)v2.xaxis - (double)v1.xaxis), 2)  +  Math.pow(((double)v2.yaxis - (double)v1.yaxis), 2));
	}
	
	/**
	 * Determines if this chord is equivalent to another chord.
	 * This is true if they refer to the same points
	 * @param other The other chord
	 * @return Whether this chord and the other chord refer to the same points
	 */
	public boolean equals(Chord other) {
		return this.length == other.length &&   
		((this.v1.equals(other.v1) && this.v2.equals(other.v2)) || (this.v2.equals(other.v1) && this.v1.equals(other.v2)));
	}

	/**
	 * Compare Chords based on length.  This is used with Collections.sort
	 * @param other The other chord
	 * @return 0 if equal in length, 1 if equivalent in length, -1 if shorter than other
	 */
	public int compareTo(Chord other) {
		if (this.length == other.length) {
			return 0;
		}
		else if (other.length > this.length) {
			return -1;
		}
		else {
			return 1;
		}
	}
	
	/**
	 * Return a string representation of the chord detailing what vertices 
	 * the chord contains along with coordinates.
	 */
	public String toString() {
		return "(pt." + v1.getID() + " to pt." + v2.getID() + ") \t ("+v1.xaxis+", "+v1.yaxis+") to ("+v2.xaxis+", "+v2.yaxis+")\n";
	}
	
	/**
	 * Determines if a given chord exists in a given list of chords.  
	 * This is a static method for convenience.
	 * @param list An ArrayList of chords
	 * @param chord any chord
	 * @return Whether the given chord is present in the list
	 */
	public static boolean contains (ArrayList<Chord> list, Chord chord){
		for (Chord c : list) {
			if (chord.equals(c)) {
				return true;
			}
		}
		return false;
	}
	
	 /**
	  * Determines whether chord1 and chord 2 intersect.  First checks the base cases
	  * if chord1 is the same as chord2.  In this case, they do intersect because they are the 
	  * same chord.
	  * 
	  * <p>The second base case is if chord1 and chord2 share one endpoint.  In this case,
	  * the two chords do NOT intersect.  
	  * 
	  * <p>Then, executes Cormen's algorithm for line intersection (pg 1018).
	  * @source <i>Introduction to Algorithms, 3rd edition</i>, Cormen, Rivest, Leiserson and Stein, page 1018 
	  * @param chord1 The first chord
	  * @param chord2 The second chord
	  * @return Whether the two chords intersect
	  */
	public static boolean intersects (Chord chord1, Chord chord2) {	
		if (chord1.equals(chord2)) {
			return true; //they are the same chord, so they intersect at all points
		}
		else if (chord1.v1.equals(chord2.v1) || chord1.v1.equals(chord2.v2) || //they share an endpoint
				chord1.v2.equals(chord2.v1) || chord1.v2.equals(chord2.v2)) {
			return false;
		}
		else {
			Vertex p1, p2, p3, p4; 
			double d1, d2, d3, d4;
			p1 = chord1.v1;
			p2 = chord1.v2;
			p3 = chord2.v1;
			p4 = chord2.v2;
			d1 = Vertex.direction (p3, p4, p1);
			d2 = Vertex.direction (p3, p4, p2);
			d3 = Vertex.direction (p1, p2, p3);
			d4 = Vertex.direction (p1, p2, p4);
			if ( ((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && 
					((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
				return true;
			}
			else if (d1 == 0 && Vertex.onSegment(p3, p4, p1)) {
				return true;
			}
			else if (d2 == 0 && Vertex.onSegment(p3, p4, p2)){
				return true;
			}
			else if (d3 == 0 && Vertex.onSegment(p1, p2, p3)) {
				return true;
			}
			else if (d4 == 0 && Vertex.onSegment(p1, p2, p4)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * Returns whether the chord is a diagonal in the polygon defined by the array of vertices and 
	 * the boundary defined by the ArrayList of chords.
	 * @param testChord The chord to test
	 * @param vertices The vertices of the polygon
	 * @param boundary List of line segments constituting the boundary of the polygon
	 * @return True if testChord is a valid diagonal.  False otherwise.
	 * @source <i>Computational Geometry in C</i> by Joseph O'Rourke.  Cambridge
	 * University Press. 1994
	 */
	public static boolean diagonal (Chord testChord, Vertex[] vertices, ArrayList<Chord> boundary) {
		return Chord.inCone(testChord, vertices) &&
			   Chord.diagonalie(testChord, vertices, boundary);
	}
	
	/**
	 * Returns true if testChord is entirely inside or entirely outside of a polygon.
	 * @param testChord The chord to test
	 * @param vertices Polygon's Vertex array
	 * @param boundary Polygon's boundary
	 * @return true if testChord is entirely inside or outside of a polygon
	 * @source <i>Computational Geometry in C</i> by Joseph O'Rourke.  Cambridge
	 * University Press. 1994, page 36
	 */
	public static boolean diagonalie (Chord testChord, Vertex[] vertices, ArrayList<Chord> boundary) {
		for (Chord c : boundary) {
			//skip edges incident to end points of testChord
			if ( ! (
			       ( c.v1.equals(testChord.v1)) || (c.v2.equals(testChord.v1)) 
			   ||  ( c.v1.equals(testChord.v2)) || (c.v2.equals(testChord.v2)))) {
				if (Chord.intersects(testChord, c)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Determines if the given chord C = (v1, v2) is strictly internal
	 * to the polygon in the neighborhood of v1.
	 * @param testChord The given chord
	 * @param vertices The vertices of the polygon
	 * @return True if testChord's v1 is strictly internal to the polygon,
	 * false otherwise
	 * @source <i>Computational Geometry in C</i> by Joseph O'Rourke.  Cambridge
	 * University Press. 1994, page 38
	 */
	public static boolean inCone (Chord testChord, Vertex[] vertices) {
		//Vertex.match_id(testChord, vertices);
		Vertex v1, v2;
		v1 = testChord.v1;
		v2 = testChord.v2;
		int n = vertices.length;
		Vertex i1 = vertices[(v1.getID() + 1) % n];
		Vertex in1 = vertices[(v1.getID() + n - 1) % n];
		//convex case
		if ( Vertex.rightOn(in1, v1, i1) ) {
			return Vertex.right(v1, v2, in1) &&
				   Vertex.right(v2, v1, i1);
		}
		// assume (i-1, i, i + 1) not collinear
		// else i is reflex
		else {
			return !(	Vertex.rightOn(v1, v2, i1) 
					&& Vertex.rightOn(v2, v1, in1));
		}
	}
	
	/**
	 * Returns a string representation of the given ArrayList of Chords
	 * @param chordList An ArrayList of Chords
	 * @return A string representation of the set of chrods
	 */
	public static String chordListAsString (ArrayList<Chord> chordList) {
		String s = "";
		for (Chord c : chordList) {
			s += c.toString();
		}
		return s;
	}
	
	/**
	 * Used for debug and test purposes.  
	 * @param args
	 */
	public static void main (String[] args) {
		Vertex v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15;
		v0 = new Vertex (-1, 2, 0);
		v1 = new Vertex (-1, 1, 1);
		v2 = new Vertex (0, 1.5, 2);
		v3 = new Vertex (1, .5, 3);
		v4 = new Vertex (1.5, -1, 4);
		v5 = new Vertex (2.5, -1, 5);
		v6 = new Vertex (2, -2.5, 6);
		v7 = new Vertex (.5, -3, 7);
		v8 = new Vertex (-.5, -2, 8);
		v9 = new Vertex (-2, -2.5, 9);
		v10 = new Vertex (-3, -2 , 10);
		v11 = new Vertex (-2.5, -1, 11);
		v12 = new Vertex (-3, 1, 12);
		v13 = new Vertex (-4, 1, 13);
		v14 = new Vertex (-4, 2, 14);
		v15 = new Vertex (-2, 2.5, 15);
		Vertex[] vertices = {v0, v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15};
		
		ArrayList<Chord> bound = new ArrayList<Chord>();
		for (int i = 0; i < vertices.length; i++) {
			bound.add(new Chord(vertices[i], vertices[(i+1) % vertices.length]));
		}
		System.out.println(bound.toString());
		ArrayList<Chord> allDiags = Polygon.generateAllDiagonals(vertices, bound);
		System.out.println(allDiags.toString());
		
		System.out.println("triangulation");
		ArrayList<Chord> tri = Polygon.generateTriangulation(55, vertices.length, allDiags);
		System.out.println(tri.toString());
		//v4 = new Vertex ()
		/*
		//create test02 polygon
		Vertex p1, p2, p3, p4, p5, p6, p7;
		p1 = new Vertex(.5, -2, 0);
		p2 = new Vertex (-1.5, 0, 1);
		p3 = new Vertex (2, 3, 2);
		p4 = new Vertex (4, 3.25, 3);
		p5 = new Vertex (6, 3, 4);
		p6 = new Vertex (9.5, 0, 5);
		p7 = new Vertex (7.5, -2, 6);
		
		//form the boundary
		Chord b1, b2, b3, b4, b5, b6, b7;
		b1 = new Chord(p1, p2);
		b2 = new Chord(p2, p3);
		b3 = new Chord(p3, p4);
		b4 = new Chord(p4, p5);
		b5 = new Chord(p5, p6);
		b6 = new Chord(p6, p7);
		b7 = new Chord(p7, p1);
		
		Vertex[] vertices = {p1, p2, p3, p4, p5, p6, p7};
		
		ArrayList<Chord> bound = new ArrayList<Chord> ();
		bound.add(b1);
		bound.add(b2);
		bound.add(b3);
		bound.add(b4);
		bound.add(b5);
		bound.add(b6);
		bound.add(b7);
		
		Chord testDiag = new Chord(1, 5, vertices);
		//Chord outsideLine = new Chord(new Vertex(1, 0), new Vertex(1, -7));
		//System.out.println(Chord.intersects(b1, b6));
		System.out.println(Chord.diagonalie(testDiag, vertices, bound));
		System.out.println(Chord.inCone(testDiag, vertices));
		Chord diag1 = new Chord(0, 2, vertices);
		System.out.println(Chord.diagonal(diag1, vertices, bound));
		ArrayList<Chord> allDiags = Chord.generateAllDiagonals(vertices, bound);
		
		Collections.sort(allDiags);
		System.out.println(allDiags.toString());
		//ArrayList<Chord> triangulation = Chord.generateTriangulation(4, 7, allDiags);
		//System.out.println(triangulation.toString());
		
		ArrayList<Chord> opt_triangulation = Chord.optimalTriangulation(vertices, bound);
		System.out.println(opt_triangulation.toString());
		
		//ArrayList<Chord> triangulation2 = Chord.generateTriangulation(9, 7, allDiags);
		//System.out.println(triangulation2.toString());
		//ArrayList<Chord> triangulation3 = Chord.generateTriangulation(8, 7, allDiags);
		//System.out.println(triangulation3.toString());
		
		//ArrayList<Chord> triangulation4 = Chord.generateTriangulation(7, 7, allDiags);
		//System.out.println(triangulation4.toString());
		
		//ArrayList<Chord> triangulation5 = Chord.generateTriangulation(6, 7, allDiags);
		//System.out.println(triangulation5.toString());
		//System.out.println(Chord.insidePolygon(outsideLine, vertices, bound));
		//System.out.println(bound.get(6));
		//System.out.println(Chord.intersects(outsideLine, bound.get(6)));
		*/
	}
}
