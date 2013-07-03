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

import java.math.BigInteger;

/**
 * Systematically generate combinations.
 * Open source product taken from http://www.merriampark.com/comb.htm, by Michael Gilleland
 * @author Michael Gilleland
 *
 */
public class CombinationGenerator {

private int[] a;
private int n;
private int r;
private BigInteger numLeft;
private BigInteger total;

//------------
// Constructor
//------------
/**
 * Creates a combination generator to generate sets of r elements from n elements.
 * @param n The total number of elements
 * @param r The length of each combination
 */
public CombinationGenerator (int n, int r) {
 if (r > n) {
   throw new IllegalArgumentException ();
 }
 if (n < 1) {
   throw new IllegalArgumentException ();
 }
 this.n = n;
 this.r = r;
 a = new int[r];
 BigInteger nFact = getFactorial (n);
 BigInteger rFact = getFactorial (r);
 BigInteger nminusrFact = getFactorial (n - r);
 total = nFact.divide (rFact.multiply (nminusrFact));
 reset ();
}

/**
 * Resets the generator.
 */
public void reset () {
 for (int i = 0; i < a.length; i++) {
   a[i] = i;
 }
 numLeft = new BigInteger (total.toString ());
}

/**
 * Returns number of combinations not yet generated.
 * @return The number of combinations not yet generated
 */
public BigInteger getNumLeft () {
 return numLeft;
}

/**
 * Determines if there are more combinations.
 * @return whether there are more combinations
 */
public boolean hasMore () {
 return numLeft.compareTo (BigInteger.ZERO) == 1;
}

/**
 * Get the total number of combinations
 * @return The total number of combinations
 */
public BigInteger getTotal () {
 return total;
}

/**
 * Factorial method
 */
private static BigInteger getFactorial (int n) {
 BigInteger fact = BigInteger.ONE;
 for (int i = n; i > 1; i--) {
   fact = fact.multiply (new BigInteger (Integer.toString (i)));
 }
 return fact;
}

/**
 * Generate next combination (algorithm from Rosen 2nd edition p 286)
 */
public int[] getNext () {

 if (numLeft.equals (total)) {
   numLeft = numLeft.subtract (BigInteger.ONE);
   return a;
 }

 int i = r - 1;
 while (a[i] == n - r + i) {
   i--;
 }
 a[i] = a[i] + 1;
 for (int j = i + 1; j < r; j++) {
   a[j] = a[i] + j - i;
 }

 numLeft = numLeft.subtract (BigInteger.ONE);
 return a;

}

public static void main (String[] args){
	String[] elements = {"a", "b", "c", "d", "e", "f", "g"};
	int[] indices;
	CombinationGenerator x = new CombinationGenerator (elements.length, 3);
	StringBuffer combination;
	while (x.hasMore ()) {
	  combination = new StringBuffer ();
	  indices = x.getNext ();	 
	  for (int i = 0; i < indices.length; i++) {
	    combination.append (elements[indices[i]]);
	  }
	  System.out.println (combination.toString ());
	}
}
}