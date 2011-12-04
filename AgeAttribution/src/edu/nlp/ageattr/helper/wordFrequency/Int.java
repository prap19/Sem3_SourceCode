package edu.nlp.ageattr.helper.wordFrequency;
//File   : data-collections/wordfreq2/Int.java
//Purpose: Simple value class to hold a mutable int.
//Author : Fred Swartz - March 2005 - Placed in public domain.

////////////////////////////////////////////////////////////////value class Int
/** Utility class to keep int as Object but allow changes (unlike Integer).
* Java collections hold only Objects, not primitives, but need to update value.
* The intention is that the public field should be used directly.
* For a simple value class this is appropriate.
*/
class Int {
 //=================================================================== fields
 public int value;  // Just a simple PUBLIC int.  
 
 //============================================================== constructor
 /** Constructor 
     @param value Initial value. */
 public Int(int value) { 
     this.value = value;  
 }
}