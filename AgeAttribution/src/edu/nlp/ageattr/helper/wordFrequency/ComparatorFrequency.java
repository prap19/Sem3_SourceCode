package edu.nlp.ageattr.helper.wordFrequency;

import java.util.Comparator;
import java.util.Map.Entry;

//File   : data-collections/wordfreq2/ComparatorFrequency.java
//Purpose: A comparator to sort Map.Entries by frequency.
//Author : Fred Swartz - April 2007 - Placed in public domain.

import java.util.*;

//////////////////////////////////////////////////////class ComparatorFrequency
/** Order words from least to most frequent, put ties in alphabetical order. */
class ComparatorFrequency implements Comparator<Map.Entry<String, Int>> {
 public int compare(Map.Entry<String, Int> obj1
                  , Map.Entry<String, Int> obj2) {
     int result;
     int count1 = obj1.getValue().value;
     int count2 = obj2.getValue().value;
     if (count1 < count2) {
         result = -1;
         
     } else if (count1 > count2) {
         result = 1;
         
     } else { 
         //... If counts are equal, compare keys alphabetically.
         result = obj1.getKey().compareTo(obj2.getKey());
     }
     return result;
 }
}
