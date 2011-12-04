package edu.nlp.ageattr.helper.wordFrequency;

//File   : data-collections/wordfreq2/WordCounter.java
//Purpose: Provides methods to read ingore file, input file, get results.
//       Computes the frequency for each word.
//Author : Fred Swartz - April 2007 - Placed in public domain.

import java.io.*;
import java.util.*;
import java.util.regex.*;

/** Computes word frequency in source file; ignores words in ignore file.
* Uses generic Sets, Maps, ArrayLists, regular expressions, Scanner.
* @author Fred Swartz
* @version 2007-05-06
*/
public class WordCounter {
 //================================================================ constants
 private static final Comparator<Map.Entry<String, Int>> SORT_BY_FREQUENCY = 
         new ComparatorFrequency();
 private static final Comparator<Map.Entry<String, Int>> SORT_ALPHABETICALLY = 
         new ComparatorAlphabetic();
 public enum SortOrder {ALPHABETICALLY, BY_FREQUENCY}
 
 //=================================================================== fields
 Set<String>      _ignoreWords;   // Words to ignore.
 Map<String, Int> _wordFrequency; // Words -> frequency
 int              _totalWords;    // Total source words.
 
 //============================================================== constructor
 /** Constructor */
 public WordCounter() {
     _ignoreWords   = new HashSet<String>();
     _wordFrequency = new HashMap<String, Int>();
     _totalWords    = 0;
 }
 
 //=================================================================== ignore
 /**
  * Reads file of words to ignore. Ignore words are added to a Set.
  *  The IOException is passed to caller because we certinaly don't
  *  know what the user interface issue is.
  * 
  * @param ignoreFile File of words to ignore.
  */
 public void ignore(File ignoreFile) throws IOException {
     Scanner ignoreScanner = new Scanner(ignoreFile);
     ignoreScanner.useDelimiter("[^A-Za-z]+");
     
     while (ignoreScanner.hasNext()) {
         _ignoreWords.add(ignoreScanner.next());
     }
     ignoreScanner.close();  // Close underlying file.
 }
 
 //=================================================================== ignore
 /**
  * Takes String of words to ignore. Ignore words are added to a Set.
  * 
  * @param ignore String of words to ignore.
  */
 public void ignore(String ignoreStr) {
     Scanner ignoreScanner = new Scanner(ignoreStr);
     ignoreScanner.useDelimiter("[^A-Za-z]+");
     
     while (ignoreScanner.hasNext()) {
         _ignoreWords.add(ignoreScanner.next());
     }
 }
 
 
 //=============================================================== countWords
 /** Record the frequency of words in the source file.
  *  May be called more than once.
  *  IOException is passed to caller, who might know what to do with it.
  *@param File of words to process.
  */
 public void countWords(File sourceFile) throws IOException {
     Scanner wordScanner = new Scanner(sourceFile);
     wordScanner.useDelimiter("[^A-Za-z]+");
     
     while (wordScanner.hasNext()) {
         String word = wordScanner.next().toLowerCase();
         _totalWords++;
      //   System.out.println("wordcount is "+_totalWords);
         //... Add word if not in map, otherewise increment count.
         if (!_ignoreWords.contains(word)) {
             Int count = _wordFrequency.get(word);
             if (count == null) {    // Create new entry with count of 1.
                 _wordFrequency.put(word, new Int(1));
             } else {                // Increment existing count by 1.
                 count.value++;
             }
         }
     }
     wordScanner.close();  // Close underlying file.
 }
 
 
 //=============================================================== countWords
 /** Record the frequency of words in a String.
  *  May be called more than once.
  *@param String of words to process.
  */
 public void countWords(String source) {
     Scanner wordScanner = new Scanner(source);
     wordScanner.useDelimiter("[^A-Za-z]+");
     
     while (wordScanner.hasNext()) {
         String word = wordScanner.next();
         _totalWords++;
         
         //... Add word if not in map, otherewise increment count.
         if (!_ignoreWords.contains(word)) {
             Int count = _wordFrequency.get(word);
             if (count == null) {    // Create new entry with count of 1.
                 _wordFrequency.put(word, new Int(1));
             } else {                // Increment existing count by 1.
                 count.value++;
             }
         }
     }
 }
 
 //============================================================= getWordCount
 /** Returns number of words in all source file(s).
  *@return Total number of words proccessed in all source files.
  */
 public int getWordCount() {
     return _totalWords;
 }
 
 //============================================================ getEntryCount
 /** Returns the number of unique, non-ignored words, in the source file(s).
  *  This number should be used to for the size of the arrays that are
  *  passed to getWordFrequency.
  *@return Number of unique non-ignored source words.
  */
 public int getEntryCount() {
     return _wordFrequency.size();
 }
 
 //========================================================= getWordFrequency
 /** Stores words and their corresponding frequencies in parallel array lists
  *  parameters.  The frequencies are sorted from low to high.
  * @param words Unique words that were found in the source file(s).
  * @param counts Frequency of words at corresponding index in words array.
  */
 public void getWordFrequency(ArrayList<String> out_words,
         ArrayList<Integer> out_counts) {
     //... Put in ArrayList so sort entries by frequency
     ArrayList<Map.Entry<String, Int>> entries =
             new ArrayList<Map.Entry<String, Int>>(_wordFrequency.entrySet());
     Collections.sort(entries, new ComparatorFrequency());
     
     //... Add word and frequency to parallel output ArrayLists.
     for (Map.Entry<String, Int> ent : entries) {
         out_words.add(ent.getKey());
         out_counts.add(ent.getValue().value);
     }
 }
 
 //================================================================= getWords
 /** Return array of unique words, in the order specified.
  * @return An array of the words in the currently selected order.
  */
 public String[] getWords(SortOrder sortBy) {
     String[] result = new String[_wordFrequency.size()];
     ArrayList<Map.Entry<String, Int>> entries =
             new ArrayList<Map.Entry<String, Int>>(_wordFrequency.entrySet());
     if (sortBy == SortOrder.ALPHABETICALLY) {
         Collections.sort(entries, SORT_ALPHABETICALLY);
     } else {
         Collections.sort(entries, SORT_BY_FREQUENCY);
     }
     
     //... Add words to the String array.
     int i = 0;
     for (Map.Entry<String, Int> ent : entries) {
         result[i++] = ent.getKey();
     }
     return result;
 }
 
 //=========================================================== getFrequencies
 /** Return array of frequencies, in the order specified.
  * @return An array of the frequencies in the specified order.
  */
 public int[] getFrequencies(SortOrder sortBy) {
     int[] result = new int[_wordFrequency.size()];
     ArrayList<Map.Entry<String, Int>> entries =
             new ArrayList<Map.Entry<String, Int>>(_wordFrequency.entrySet());
     if (sortBy == SortOrder.ALPHABETICALLY) {
         Collections.sort(entries, SORT_ALPHABETICALLY);
     } else {
         Collections.sort(entries, SORT_BY_FREQUENCY);
     }
     
     //... Add words to the String array.
     int i = 0;
     for (Map.Entry<String, Int> ent : entries) {
         result[i++] = ent.getValue().value;
     }
     return result;
 }
}
