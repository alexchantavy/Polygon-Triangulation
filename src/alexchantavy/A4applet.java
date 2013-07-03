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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JProgressBar;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ICS 311 Spring 2010 Assignment 4 : Polygon Triangulation
 * <p>Java applet to allow user input of a polygon text file, polygon drawing, and computation of
 * an optimal triangulation.
 * <p>Special Features:
 * <ul>
 * <li><b>Scale factor drawing</b> - User may adjust how large they want the polygon to be displayed</li>
 * <li><b>Progress bar</b> - Tells the user how far the algorithm has progressed</li>
 * <li><b>Cancel</b> - Algorithm runs in background thread, allowing for cancellation!</li> 
 * <li><b>Preset tests</b> - Comes with 3 pre-specified test URLs.</li>
 * <li><b>Labeled vertices</b> - All vertices are numbered in the same order they are specified in the text file</li>
 * <li><b>Clear coloring</b> - All diagonals represented as dotted lines, optimal triangulation drawn in bold lines
 * </ul>
 * @author Alex Chantavy
 * @author Jon Lai
 */
@SuppressWarnings("serial")
public class A4applet extends JApplet 
			          implements ActionListener, ChangeListener, PropertyChangeListener{
	// Scale Factor slider constants
	static final int SF_MIN = 20;
	static final int SF_MAX = 100;
	static final int SF_INIT = 28;
	
	// status constants
	static final int CLEAR_SCREEN = 0;
	static final int DRAW_POLYGON = 1;
	static final int DRAW_DIAGS   = 2;
	
   	// UI components
	private TextField url_field; 
	private TextArea console;        //console area for status updates
	private Label url_label;	     //label to tell the user to enter URL
	private JScrollPane drawingArea  = new JScrollPane();
	private JButton btn_inputPolygon = new JButton(); //button for input graph
	private JButton btn_runAlgorithm = new JButton(); //button to run the algorithm
	private JButton btn_reset        = new JButton(); //reset button
	private JButton btn_ex1          = new JButton(); //test01 button
	private JButton btn_ex2          = new JButton(); //test02 button
	private JButton btn_ex3          = new JButton(); //26 point test
	private JProgressBar progressBar = new JProgressBar(0,100);
	private JLabel label_sf;
	private static JTextField combination_display;
	private JSlider sf_slider; 
	private static int scaleFactor   = 28;
	private Task task;
	
   	//necessary global variables
   	Vertex[] vertices;
   	Vertex[] verticesLocation        =  null; // used to adjust vertex coords for drawing
   	int taskStatus                   = CLEAR_SCREEN; // start the applet with a clear screen
	ArrayList<Chord> boundary        = new ArrayList<Chord>();
	ArrayList<Chord> allDiags        = new ArrayList<Chord>(); 
	ArrayList<Chord> optimumSolution = new ArrayList<Chord>();
	Chord maxChord;
	int errorcode;
	
   	/**
   	 * Initializes the applet to display input and output boxes.
   	 */
   	public void init() {
		setLayout(new FlowLayout());	//set layout of applet
		url_label              = new Label("Input a polygon text file address:"); 
		url_field                     = new TextField("http://", 80);
		btn_inputPolygon              = new JButton("Input Polygon");
		btn_runAlgorithm              = new JButton("Run algorithm");
		btn_reset                     = new JButton("Reset");
		btn_runAlgorithm.setEnabled(false);
		btn_reset.setEnabled(false);
		console = new TextArea("Hello, I am PolygonIllustrator.\nEnter the URL for a polygon text file, and I will draw it!\n\n" +
				"Otherwise, hit any of the \"Load Test\" buttons to the\nright of this box to try some sample polygons.\n\n" +
				"Then, I will triangulate the polygon so that the maximum\nlength diagonal is minimized! :)\n");
		console.setEditable(false);
		label_sf                      = new JLabel("Scale factor:");
		combination_display           = new JTextField("----------------------------------");
		combination_display.setEditable(false);
		sf_slider                     = new JSlider(JSlider.HORIZONTAL, SF_MIN, SF_MAX, SF_INIT);
		btn_ex1                       = new JButton("Load Test1");
		btn_ex2                       = new JButton("Load Test2");
		btn_ex3                       = new JButton("Load Test3");
		progressBar.setValue(0);
		progressBar.setStringPainted(false);
		btn_inputPolygon.addActionListener(this);
		btn_runAlgorithm.addActionListener(this);
		btn_reset.addActionListener(this);
		btn_ex1.addActionListener(this);
		btn_ex2.addActionListener(this);
		btn_ex3.addActionListener(this);
		btn_ex1.setEnabled(true);
		btn_ex2.setEnabled(true);
		btn_ex3.setEnabled(true);
		
		sf_slider.setMajorTickSpacing(10);
		sf_slider.setMinorTickSpacing(1);
		sf_slider.setPaintTicks(true);
		sf_slider.setPaintLabels(true);
		sf_slider.addChangeListener(this);
		sf_slider.setEnabled(false);
		
		//add components to be displayed in FlowLayout order
		add(url_label);
		add(url_field);
		add(console);
		
		add(btn_ex1);
		add(btn_ex2);
		add(btn_ex3);
		
		add(btn_inputPolygon);
		add(btn_runAlgorithm);
		add(btn_reset);
		
		add(label_sf);
		add(sf_slider);
		add(drawingArea);
		
		add(progressBar);
		add(combination_display);
		setSize(1000,800);  //size of application
  }
   	
   	/**
   	 * Displays the given array of integers in a JTextField.
   	 * Used to show the user what combinations the algorithm is
   	 * currently trying out
   	 * @param array Array of integers of diagonal indices
   	 */
   	public static void printArrayCombination(int[] array) {
   		String s ="";
   		for (int i = 0; i < array.length; i++) {
   			s += array[i] + " ";
   		}
   		combination_display.setText(s+"\n");
   	}
   	
   	/**
   	 * Anonymous subclass of SwingWorker.  Allows polyogn triangulation algorithm to be 
   	 * run in a separate thread, which gives the user the ability to cancel the process.
   	 * @author Alex Chantavy
   	 */
   	class Task extends SwingWorker<Void, Void> {
   		boolean successful = false;
   		
   		/**
   		 * Generates the optimal triangulation of the polygon in a separate thread.
   		 * @see Polygon#optimalTriangulation(Vertex[], ArrayList)
   		 */
   		@Override
   		public Void doInBackground() {
   			setProgress(0);
   			
   			ArrayList<Chord> allDiagonals = Polygon.generateAllDiagonals(vertices, boundary);
   			ArrayList<Chord> optimum = new ArrayList<Chord>();
   			Collections.sort(allDiagonals); //Collections.sort uses an n*log(n) mergesort.
   			
   			int n = vertices.length;
   			int start = n-3-1;
   			int end = allDiagonals.size();
   			int num_chords_to_try = end - start; // used for JProgressBar reporting
   			
   			for (int i = start; i<end; i++) {
   				try {
	   				double percent = (((double)i-(double)start) / (double)num_chords_to_try) * 100;
	   				setProgress((int)percent);
	   				if (Thread.interrupted()) {
	   					throw new InterruptedException();
	   				}
	   				optimum = Polygon.generateTriangulation(i, n, allDiagonals);
		   			if (optimum != null) {
		   				setProgress(100);
		   				successful = true;
		   				i = allDiagonals.size() + 2; // 2 is arbitrary. We just want to break out of the loop early
		   			}
   				}
   				catch (InterruptedException e) {
   					setProgress(0);
   					return null;
   				}
   			}
   			optimumSolution = optimum;
            return null;
   		}
   		
   		/**
   		 * Manages what happens when algorithm execution is cancelled or completed.   
   		 */
   		@Override
   		public void done() {
   			setCursor(null); //turn off the wait cursor
   			sf_slider.setEnabled(true); //turn on the slider again after algorithm task is done
			if (successful) {
				taskStatus = DRAW_DIAGS;
				maxChord = optimumSolution.get(optimumSolution.size()-1);
				repaint();
				btn_inputPolygon.setEnabled(false);
				btn_reset.setText("Reset");
				btn_runAlgorithm.setEnabled(false);
				btn_reset.setEnabled(true);
				btn_ex1.setEnabled(false);
				btn_ex2.setEnabled(false);
				btn_ex3.setEnabled(false);
				sf_slider.setEnabled(true);
				console.append("Success!\n" +
								"Stats:\n" +
					         	allDiags.size() + " total diagonals.\n" + 
					         	optimumSolution.size() + " chords in optimum set.\n" +
					         	"------------------------------\n" +
					         	"Optimum Set: \n" + Chord.chordListAsString(optimumSolution) + 
					         	"-------------------------------\n" +
					         	"Max chord: " + maxChord.toString() +
					         	"Length: " + maxChord.length + " units\n");
			}
   		}
   	}
   
  
   /**
    * Uses changeEvents to update the scaleFactor of the polygon drawn
    * when the user moves the JSlider.
    */
   	public void stateChanged(ChangeEvent e) {
   		JSlider source = (JSlider)e.getSource();
   		if (source.getValueIsAdjusting()) {
   			int sf = (int)source.getValue();
   			scaleFactor = sf;
   			repaint();
   		}
   	}
   	
  /**
   * Controls what happens when the user clicks on buttons.  
   * @param evt The action event
   */
   	//detects action by user
   	public void actionPerformed(ActionEvent evt) { 
   		try {			   	
   			if (evt.getActionCommand() == "Input Polygon") { 
   				String url_text = url_field.getText();
   				inputPolygon(url_text); // attempt to form a polygon from the given URL
   				if (taskStatus == DRAW_POLYGON) {
   	   				console.append("Attempting to draw polygon\n");
   	   				vertices =  Polygon.getVertices();  //get vertices input from file
   	   				//!! if all error checks are passed then the polygon is (re)painted.
   	   				repaint();
   	   				console.append("Polygon successfully drawn.  It has " + Polygon.getVertices().length +" points.\n" +
   	   						"You can zoom the polygon in and out with the \"Scale Factor\" slider.\n" +
   	   						"Click \"Run algorithm\" to triangulate this polygon.\n");
   	   				btn_inputPolygon.setEnabled(false);
   	   				btn_runAlgorithm.setEnabled(true);
   	   				btn_ex1.setEnabled(false);
   	   				btn_ex2.setEnabled(false);
   	   				btn_ex3.setEnabled(false);
   	   				btn_reset.setEnabled(true);
   	   				sf_slider.setEnabled(true);
   	   			}
   			}
   			else if(evt.getActionCommand() == "Run algorithm") {
   					progressBar.setStringPainted(true);
   					combination_display.setVisible(true);
   					btn_reset.setText("Cancel");
   					sf_slider.setEnabled(false); //turn off the scale factor slider when algorithm is being executed
   					
   					btn_runAlgorithm.setEnabled(false);
   					console.append("Attempting algorithm.  Please wait.\n");	
   					
   					boundary = Polygon.getBoundary();
   				  	allDiags = Polygon.generateAllDiagonals(vertices, boundary);
   				  	
   				  	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
   				  	task = new Task();  //start the algorithm
   				  	task.addPropertyChangeListener(this);
   				  	task.execute();
   			}
   			else if(evt.getActionCommand() == "Reset") {
   				progressBar.setValue(0);
   				combination_display.setVisible(false);
   				btn_inputPolygon.setEnabled(true);
   				btn_runAlgorithm.setEnabled(false);
   				btn_reset.setEnabled(false);
   				sf_slider.setEnabled(false);
   				btn_ex1.setEnabled(true);
	   			btn_ex2.setEnabled(true);
	   			btn_ex3.setEnabled(true);
   				remove(drawingArea);
   				add(new JScrollPane());
   				console.append("Drawing area cleared\n");
   				taskStatus = CLEAR_SCREEN; //reset the graph area.
   				repaint();
   			}
   			else if (evt.getActionCommand() == "Load Test1") {
   				url_field.setText("http://www2.hawaii.edu/~sugihara/course/ics311s10/assign4_test_data/test01.txt");
   				console.append("Test01 URL loaded.  Click \"Input Polygon\" to continue.\n");
   			}
   			else if (evt.getActionCommand() == "Load Test2") {
   				url_field.setText("http://www2.hawaii.edu/~sugihara/course/ics311s10/assign4_test_data/test02.txt");
   				console.append("Test02 URL loaded.  Click \"Input Polygon\" to continue.\n");
   			}
   			else if (evt.getActionCommand() == "Load Test3") {
   				url_field.setText("http://www2.hawaii.edu/~chantavy/ics311/assignments/a4/tests/26points.txt");
   				console.append("Test03 URL loaded.  Click \"Input Polygon\" to continue.\n");
   			}
   			else if (evt.getActionCommand() == "Cancel") {
   				btn_reset.setText("Reset");
   				btn_runAlgorithm.setEnabled(true);
   				btn_ex1.setEnabled(false);
   				btn_ex2.setEnabled(false);
   				btn_ex3.setEnabled(false);
   				btn_reset.setEnabled(true);
   				sf_slider.setEnabled(true);
   				task.cancel(true);
   				console.append("Algorithm cancelled.\n");
   				btn_inputPolygon.setEnabled(false);
   				
   			}
   			remove(drawingArea);
   			add(new JScrollPane());
   		} 	
   		catch (IOException e) {
   			e.printStackTrace();
   			console.append(e.toString());
   		}		
   		validate(); //resets positioning of all applet components
   		return;
   	}
   	
   	public void propertyChange (PropertyChangeEvent evt) {
   		if ("progress" == evt.getPropertyName()) {
   			int progress = (Integer) evt.getNewValue();
   			progressBar.setValue(progress);
   			
   		}
   	}

	/**
	 * Connects to the given URL string and determines if polygon creation was successful.
	 * <p>Error List
	 * <ol>
	 * <li>Blank input</li>
	 * <li>Invalid URL syntax</li>
	 * <li>File not found at given address</li>
	 * <li>File is being denied retrieval</li>
	 * <li>URL is over 1000 characters</li>
	 * <li>File contains a non integer character</li>
	 * <li>Not all vertices were defined.</li>
	 * <li>More than 100 vertices</li>
	 * <li>Incorrect input n. n specified was less than three.</li>
	 * <li>File extension must end in .txt.</li>
	 * <li>Data contains duplicate points</li>
	 * <li>Polygon is non-simple</li>
	 * <li>Polygon contains point out of bounds of (+/-24 , +/-13)</li> 
	 * </ol>
	 * @param url_text String of the desired URL
	 * @throws IOException
	 */
	private void inputPolygon(String url_text) throws IOException {
		console.append("Attempting to fetch URL.  Please wait.\n");
		errorcode = Polygon.input(url_text); //takes url as input
		switch(errorcode) {
		case -1:
			console.append("(err 1) Please enter a URL.\n"); //blank input
		  	return;
		case -2:
			console.append("(err 2) Invalid URL syntax.\n");
			return;  
		case -3:
			console.append("(err 3) The file you were looking for cannot be found.  " +
							"Please verify that the input URL is correct.\n");
			return;
		case -4:
			console.append("(err 4) File cannot be accessed.  " +
		  				"Please try another file.\n");
		  	return;
		case -5:
			console.append("(err 5) URL must contain fewer than 1000 characters.\n");
		  	return;
		case -6:
			console.append("(err 6) I could not read the file you gave me as numbers. " +
					" Maybe it: \n" +
		  			"1.) Contains nonnumeric characters.\n" +
		  			"2.) Or is blank.\n In any case, please verify that the input URL " +
		  			"refers to a text file of valid format.\n");
			return;  
		case -7:
			console.append("(err 7) The file you gave me specified " 
					+ Polygon.getVertices().length + " vertices, " +
					"but it only defined " + Polygon.getNumDefPts() + " of them. " +
					"\nPlease edit the file or choose a different one.\n");
			return;
		case -8:
			console.append("(err 8) The file wants me to draw " 
					+ Polygon.getVertices().length + " points, but I can only draw " 
					+ Polygon.MAX_N + "!\n" +
					":( Please pick a file with fewer points\n");
			return;  
		case -9:
			console.append("(err 9) The text file you specified does not have the vertices " +
					"ordered in clockwise order.\nPlease modify or change the text file.\n");
			return;
		case -10:
			console.append("(err 10) Line 1 indicates that the polygon you are trying to" +
					" draw has fewer than 3 vertices.  This is not possible.\n");
			return;
		case -11:
			console.append("(err 11) Invalid file extension.  File must end in *.txt\n");
			return;
		case -12:
			console.append("(err 12) The text file you specified contains duplicate points.\n" +
					"Point: " + Polygon.lastDefined.toString() +
					"Please modify or choose another text file.\n");
			return;
		case -13:
			console.append("(err 13) The text file you specified is of a non-simple polygon, \n" +
					"i.e., its boundary lines intersect each other.  Only simple \n polygons are supported\n");
			return;
		case -14:
			console.append("(err 14) The text file you specified contains a vertex out of our drawable range.\n" +
					"The vertex you tried to add was " + Polygon.lastDefined.toString() + ".\n" +
		  				"The maximum x value is abs(" + Polygon.MAX_X + ") and the maximum y \n" +
		  						"value is abs(" + Polygon.MAX_Y + ").\n");
			return;
		default:
			taskStatus = DRAW_POLYGON; //side effect: if no error occurs, set taskStatus to DRAW_POLYGON
		}
	}
  
    /** 
     * Paints the graphics pane when action is performed.  
     * Either draws a polygon, draws an optimal triangulation, or clears the screen when buttons are pressed.
     */
    public void paint(Graphics g) {
    	
    	if(taskStatus == CLEAR_SCREEN){  
    		super.paint(g);	
    	}
    	else if(taskStatus == DRAW_POLYGON) {  
    		drawPolygon(g);
		}
    	else if(taskStatus == DRAW_DIAGS) { 
    		drawPolygon(g);
    		drawTriangulation(g);  
    	}
  	}

    /**
     * Draws all the diagonals of the polygon in blue.  Draws the diagonals of the 
     * optimum solution in green, and draws the maximum length chord in red.
     * @param g The graphics object
     */
	private void drawTriangulation(Graphics g) {
		// Draw all the diagonals in blue
		   Graphics2D g2 = (Graphics2D) g;
		   g2.setColor(Color.blue);
		   float dash1[] = {1.0f};
		   BasicStroke dashed = new BasicStroke(1.0f, 
                   BasicStroke.CAP_BUTT, 
                   BasicStroke.JOIN_MITER, 
                   10.0f, dash1, 0.0f);
		   g2.setStroke(dashed);
		   for (Chord c : allDiags) {
			   g2.drawLine((int)((c.v1.xaxis*scaleFactor)+500+5), (int)((-1 * c.v1.yaxis*scaleFactor) + 475+5), 
					                          (int)((c.v2.xaxis*scaleFactor)+500+5), (int)((-1 * c.v2.yaxis*scaleFactor)+ 475+5));
		   }
		  // Draw the optimum solution in green,
		  // with the maximum length chord in red
		  g2.setStroke(new BasicStroke(2));
		  for (int index = 0; index < optimumSolution.size(); index++) {
			  Chord c = optimumSolution.get(index);
			  g2.setColor(Color.green);
			  if (index != (optimumSolution.size()-1)) {
				  g2.drawLine((int)((c.v1.xaxis*scaleFactor)+500+5), (int)((-1 * c.v1.yaxis*scaleFactor) + 475+5), 
                          (int)((c.v2.xaxis*scaleFactor)+500+5), (int)((-1 * c.v2.yaxis*scaleFactor)+ 475+5));
			  }
			  else{
				  g2.setColor(Color.red);
				  g2.drawLine((int)((c.v1.xaxis*scaleFactor)+500+5), (int)((-1 * c.v1.yaxis*scaleFactor) + 475+5), 
                          (int)((c.v2.xaxis*scaleFactor)+500+5), (int)((-1 * c.v2.yaxis*scaleFactor)+ 475+5));
			  }
		  }
	}
	
	/**
	 * Draws the boundary of the polygon with lines, ellipses and text labels.
	 * @param g The graphics object
	 */
	private void drawPolygon(Graphics g) {
		super.paint(g);	
		verticesLocation =  new Vertex[vertices.length]; 
		//draw vertices in polygon as ovals
		for(int i = 0; i < vertices.length; i++){
			int x = (int) (vertices[i].xaxis * (double)scaleFactor) + 500;
			int y = -1* (int) (vertices[i].yaxis* (double)scaleFactor) +475;
			g.setColor(Color.black);
			g.fillOval(x, y, 10, 10);
			g.drawString(""+i, x+7, y-3); //label the vertices by ID
		}
  
		/*
		 *convert the points on file to points in applet
		 *creates an array of points that can be accessed later to create edges and chords
		 *verticesLocatin[i] is in clockwise order given by file where 0 is the first input and n-1 is the last
		 *
		 * formula example: location of x or y axis * 20 + 500 + 5
		 * location of x or y is taken from file
		 * 20 enlarges the polygon this can be changed to whatever size is prefered
		 * 500/scaleFactor0 is the offset based on half the size so origin is in the center of the plane of the applet, this can be changed
		 * 5 is the offset for the line to start in the center of the vertex/oval, based on half the size of vertex, set at 10
		 */
		for(int i = 0; i < vertices.length; i++) {
			verticesLocation[i] = new Vertex((double)(vertices[i].xaxis * (double)scaleFactor) + 500+5, 
		                                  (double) -1* (int) (vertices[i].yaxis* (double)scaleFactor) +475+5);
		}
		// draw edges of polygon	
		g.setColor(Color.black);
		int n = vertices.length;
		for(int i = 0; i < n; i++) {
			g.drawLine((int) verticesLocation[i].xaxis, (int)verticesLocation[i].yaxis, 
					  (int) verticesLocation[(i+1) % n].xaxis, (int)verticesLocation[(i+1) % n].yaxis);
		}
	}
	/**
   	 * Destroys the applet.
   	 */
   	public void destroy(){};
}