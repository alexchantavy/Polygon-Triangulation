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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

/**
 * <p>Represents a Polygon.  Contains an array of vertices that constitute its points,
 * and an ArrayList of line segments that constitute its boundary.</p>
 * 
 * <p>Contains methods to generate triangulations, as well as the optimal triangulation
 * required by the assignment.</p>
 * 
 * <p>Method to read in text file for polygon input taken from open source product 
 * written by Jon Lai.  Triangulation algorithm written and implemented by Alex Chantavy.
 * 
 * @author Alex Chantavy
 * @author Jon Lai
 */
public class Polygon {
	public static final int MAX_URLCHARS = 1000;
	public static final int MAX_N = 100;  //cannot have more than 100 vertices.
	public static final int MAX_X = 24;
	public static final int MAX_Y = 13;

	protected double progress;
	
	private static Vertex[] vertices =  null;
	private static ArrayList<Chord> boundary = null;
	private static int num_defined_pts = 0; // used to report error 7: not all points defined.  
	public static Vertex lastDefined; // used to report error 14: vertex out of bounds
	/**
	 * Facilitates opening a URL, reading a text file, instantiating the vertex array
	 * and boundary, and dealing with exceptions. A listing and demonstration of error
	 * codes is found in the testing documentation
	 * of this project.
	 * @see A4applet#actionPerformed(java.awt.event.ActionEvent)
	 * @param address The URL
	 * @return An outcome code.  Negative codes for errors, positive ones for success.
	 * @throws IOException
	 */
	public static int input(String address) throws IOException{
		Integer n = null;
		int eof = 0; // end of file
		if(address.length() == 0){ // no address entered
			return -1; //error code for blank
		}
		else if(address.length() > MAX_URLCHARS){  //more then 1000 characters in url 
			return -5; //error code for more than 1000 chars
		}
		else if(!address.endsWith(".txt")) {
			return -11;  //error code for invalid file extension
		}
		address = VerifyString.verifyStrings(address);	//verify string sanitize
		try{
			int count = 0;
			URL targetURL = new URL(address);  		// input String to URL
			URLConnection connection = targetURL.openConnection();  		//connects to URL and input file
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String currentLine = "";  //Used to Store input Lines
			while (true) {
				try {
					currentLine = inputStream.readLine();  //2: Case 5: reads the next line of input into num breaks at \n or \r allows for both cases 
					int num_vertices = 0;
					if(count == 0){//first integer tells us the number of vertices
						num_vertices = Integer.parseInt(currentLine);
						if (num_vertices < 3) {
							return -10; //error code for n specified less than 3
						}
						else if (num_vertices > MAX_N){
							return -8; // error code for n > 100
						}
						vertices = new Vertex[num_vertices];
						eof = Integer.parseInt(currentLine) +1;
					}
					if(count == eof) {
						//no need to add vertices anymore, so form the boundary of the polygon
						boundary = new ArrayList<Chord>();
						for (int index = 0; index < vertices.length; index++) {
							Vertex v1 = vertices[index];
							Vertex v2 = vertices[(index + 1) % vertices.length]; //cycle
							Chord boundary_line = new Chord(v1, v2);
							if (Polygon.intersectsWithList(boundary_line, boundary)){
								return -13; //error 13: polygon defined is not a simple polygon.  That is,
								            // the line segments of its boundary intersect each other.
							}
							else{
								boundary.add(boundary_line);
							}
						}
						System.out.println(boundary.toString());
						break;  //reach end of input
					}
					if (currentLine.contains("\t")){  //input contains a tab 
						String []  parts= currentLine.split("\t");  //split string at tab
						String c1 = parts[0];    			// start edge
						String c2 = parts[1];				//end edge
						// Create a new Vertex with the coordinates specified by the text file
						// and with an ID number telling its clockwise order.  
						double x_coord = Double.parseDouble(c1);
						double y_coord = Double.parseDouble(c2);
						Vertex toBeAdded= new Vertex(x_coord, y_coord, count-1);
						if (Math.abs(x_coord) > MAX_X || Math.abs(y_coord) > MAX_Y) {
							lastDefined = toBeAdded;
							return -14; // error 14: vertex out of drawable region
						}
						 
						if (verticesContain(toBeAdded)) {
							lastDefined = toBeAdded;
							return -12;  // error 12: duplicate points in the text file
						}
						else {
							vertices[count-1] = toBeAdded;
						}
					}	
					else n = Integer.parseInt(currentLine); //parse initial value n
				}
				catch (NullPointerException e) {
					num_defined_pts = count-1;
					return -7; //error that not all points are specified
				}
				catch(NumberFormatException g){
					return -6; // file contains noninteger character
				}
				// TODO: Deal with max x coords and max y coords so that we dont draw out of bounds
				count++;
			}
		}
		catch(MalformedURLException e){
			return -2;
		}
		catch(FileNotFoundException g){
			return -3;
		}	
  		catch(IOException f){
			return -4; // file is denied retrieval
		}
		return n;  //if execution made it this far, n is positive and won't give an error code
	}
	
	  /**
	   * Generates the optimal triangulation of the polygon. Algorithm
	   * designed and implemented by Alex Chantavy.
	   * 
	   * <p>An <b><i>optimal triangulation</i></b> is defined as follows: given <i>n</i> 
	   * vertices in a polygon, find a set of <i>n</i>-3 nonintersecting diagonals
	   * such that the maximum length diagonal is as small as possible.</p>
	   * 
	   * <p>We accomplish this task in the following manner.
	   * <ol>
	   * <li>Generate all possible diagonals of the polygon</li>
	   * <li>Sort the diagonals from least length to greatest length</li>
	   * <li>Take n-3 diagonals {array[0], array[1], ..., array[(n-3)-1]} and determine whether 
	   * they constitute a nonintersecting set.  If they do, we are done.  If they do not,
	   * determine if a combination of n-3 nonintersecting diagonals may be formed with array[n-3]
	   * as the maximum.  IF this is possible, we are done.  If it is not, repeat for the rest
	   * of the array.
	   * </ol>
	   * @param vertices Array of vertices of the polygon
	   * @param boundary The set of line segments that constitute the boundary of the polygon
	   * @return The optimal triangulation of the polygon
	   */
	  public static ArrayList<Chord> optimalTriangulation (Vertex [] vertices, ArrayList<Chord> boundary) {
		  ArrayList<Chord> allDiagonals = Polygon.generateAllDiagonals(vertices, boundary);
		  ArrayList<Chord> optimum = new ArrayList<Chord>();
		  Collections.sort(allDiagonals); //Collections.sort uses an n*log(n) mergesort.
		  int n = vertices.length;
		  for (int i = n-3-1; i<allDiagonals.size(); i++) {
			  optimum = Polygon.generateTriangulation(i, n, allDiagonals);
			  if (optimum != null) {
				  i = allDiagonals.size() + 20; // 20 is just arbitrary. We just want to break out of the loop early
			  }
		  }
		  return optimum;
	  }	
	
	/**
	 * Generates all the diagonals of the polygon.  Uses {@link Chord#diagonal(Chord, Vertex[], ArrayList)} to 
	 * determine what is a diagonal.
	 * @see Chord#diagonal(Chord, Vertex[], ArrayList)
	 * @param vertices The array of vertices of the polygon
	 * @param boundary The line segments that constitute the boundary of the polygon
	 * @return An ArrayList containing all the diagonals of the polygon
	 */
	public static ArrayList<Chord> generateAllDiagonals(Vertex[] vertices, ArrayList<Chord> boundary) {
		  ArrayList<Chord> allDiagonals = new ArrayList<Chord>();
		  int n = vertices.length;
		  for (int i = 0; i < n; i++) {
			  for (int j = 0; j < n; j++) { //chords may not be formed from nonadjacent vertex to nonadajcent vertex
				  if ( ! (j == ( (i+1) % n ) ||
						 (j == ( (i-1) % n ))) 
				     ) 
				  {
					  Chord toBeAdded = new Chord(i, j, vertices);
					  if (Chord.diagonal(toBeAdded, vertices, boundary)) {
						  if (! (Chord.contains(allDiagonals, toBeAdded))){ //prevent duplicates from being added
							  allDiagonals.add(toBeAdded);
						  }
					  }
				  }
			  }
		  }
		  return allDiagonals;
	}
	
	/**
	 * Attempts to generate a triangulation from the set of allDiagonals where max_chord 
	 * is the index of the maximum length chord of the triangulation.  Returns an ArrayList 
	 * of chords if possible, otherwise null.  Uses Michael Killeland's open source product
	 * <i>CombinationGenerator</i> to systematically generate combinations.
	 * 
	 * <p>Recall that a triangulation is a set of n-3 nonintersecting chords.
	 * 
	 * @precondition allDiagonals is SORTED, and is an ArrayList of all the diagonals of a given polygon 
	 * @param max_chord Index of the chord in <code>allDiagonals</code> to test for as the max length chord
	 * @param n The number of vertices of the polygon
	 * @param allDiagonals Sorted ArrayList containing all diagonals of the polygon.
	 * @return null if not possible, the ArrayList of diagonals if possible.
	 */
	public static ArrayList<Chord> generateTriangulation(int max_chord, int n, ArrayList<Chord> allDiagonals){
		//Collections.sort(allDiagonals);
		if (n-3 > max_chord+1) {
			return null;
		}
		CombinationGenerator x = new CombinationGenerator(max_chord+1, n-3);
		int[] indices = new int[n-3];
		while (x.hasMore()) {
			indices = x.getNext();
			A4applet.printArrayCombination(indices);
			ArrayList<Chord> triangulation = new ArrayList<Chord>();
			if (arrayContainsNum(indices, max_chord)) {  //find a combination with max_chord as the maximum chord
				for (int i = 0; i < indices.length; i++) {
					Chord toBeAdded = allDiagonals.get(indices[i]);
					if (!intersectsWithList(toBeAdded, triangulation)){
						triangulation.add(toBeAdded);
					}
				}
				if (triangulation.size() == n - 3) {
					return triangulation;
				}
			}
		}
		return null;
	}
	
	/**
	 * Determines if the given array of integers contains a given integer
	 * @param array The array
	 * @param num The integer
	 * @return true if array has the number, false if not
	 */
	private static boolean arrayContainsNum (int[] array, int num) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == num) {
				return true;
			}
		}
		return false;
	}
	
	/** Wrapper method to test for intersection with the existing chords in chordList
	  * @param chordList An ArrayList of Chords
	  * @param toBeAdded The Chord to be tested
	  * @return whether toBeAdded intersects with chordList
	  */
	private static boolean intersectsWithList (Chord toBeAdded, ArrayList<Chord> chordList ){
		try {
			for (Chord c: chordList) {
				if (Chord.intersects(toBeAdded, c)) { 
					return true;
				}
			}
			return false;
		}
		catch (NullPointerException e) {
			return false;
		}
	}
	  
	/**
	 * Returns the vertices of the polygon
	 * @return The polygon's vertices
	 */
	public static Vertex[] getVertices(){
		return vertices;
	}
		
	/**
	 * Returns the ArrayList consisting of line segments that constitute
	 * the boundary of the polygon.
	 * @return the boundary of the polygon
	 */
	public static ArrayList<Chord> getBoundary() {
		return boundary;
	}
	
	/**
	 * Determines if the vertex array contains a given vertex.
	 * This is used to prevent duplicate points from being added to
	 * the polygon.
	 * @param coord The Vertex to test
	 * @return Whether coord is already defined in the Vertex array
	 */
	private static boolean verticesContain(Vertex coord) {
		try {
			for (int i = 0; i < vertices.length; i++) {
				if (coord.equals(vertices[i])) {
					return true;
				}
			}
			return false;
		}
		catch (NullPointerException e) {
			return false;
		}
	}
		
	/**
	 * Used for error reporting when not all lines have been defined.  I.e,
	 * when the text file says n = 10 but only 8 lines are defined.
	 * In that case, this method would return 8.
	 * @return The number of points defined by the text file
	 */
	public static int getNumDefPts() {
		return num_defined_pts;
	}	
}