package edu.nlp.ageattr.helper.wordFrequency;
//File   : data-collections/wordfreq2/ComparatorAlphabetic.java
//Purpose: A comparator to sort Map.Entries alphabetically.
//Author : Fred Swartz - March 2005 - Placed in public domain.

import java.util.*;

/////////////////////////////////////////////////////// class ComparatorAlphabetic
/** Order words alphabetically. */
class ComparatorAlphabetic implements Comparator<Map.Entry<String, Int>> {
 public int compare(Map.Entry<String, Int> entry1
                  , Map.Entry<String, Int> entry2) {
     return entry1.getKey().compareTo(entry2.getKey());
 }
}