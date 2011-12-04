package edu.nlp.ageattr.helper.wordFrequency;

//File   : data-collections/wordfreq2/WordFrequencyCmd.java
//Purpose: Main program to test WordCounter
//       Prints word frequency in source file. Ignores words in ignore file.
//       Uses Sets, Maps, ArrayList, regular expressions, BufferedReader.
//Author : Fred Swartz - April 2007 - Placed in public domain.

import java.io.*;
import java.util.*;

/////////////////////////////////////////////////////////////// WordFrequencyCmd
public class WordFrequencyCmd {
 //===================================================================== main
 /*public static void main(String[] unused) {
     Scanner in = new Scanner(System.in);
     
     try {
         //... Read two file names from the input.
         System.out.println("Name of file containing text to analyze:");
         File inputFile = new File(in.nextLine());
         
         System.out.println("Name of file containing words to ignore:");
         File ignoreFile = new File(in.nextLine());
         
         //... Supply two files to WordCounter.
         WordCounter counter = new WordCounter();
         counter.ignore(ignoreFile);
         counter.countWords(inputFile);
         
         //... Get the results.
         String[] wrds   = counter.getWords(WordCounter.SortOrder.BY_FREQUENCY);
         int[] frequency = counter.getFrequencies(WordCounter.SortOrder.BY_FREQUENCY);
         
         //... Display the results.
         int n = counter.getEntryCount();
         for (int i=0; i<n; i++) {
             System.out.println(frequency[i] + " " + wrds[i]);
         }
         
         System.out.println("\nNumber of input words: " + counter.getWordCount());
         System.out.println("Number of unique words: " + n);
         
     } catch (IOException iox) {
         System.out.println(iox);
     }
 }
*/
 public String[] get50MostFrequentlyOccuringWords(File InputFile, File StopWordFile, File OutputFile){
	 System.out.println("hi");
	 String[] wrds = null;
     //... Supply two files to WordCounter.
     WordCounter counter = new WordCounter();
     try {
		counter.ignore(StopWordFile);
		counter.countWords(InputFile);
		//... Get the results.
	     wrds   = counter.getWords(WordCounter.SortOrder.BY_FREQUENCY);
	     int[] frequency = counter.getFrequencies(WordCounter.SortOrder.BY_FREQUENCY);
	     
	     //... Display the results.
	     int n = counter.getEntryCount();
	    /* for (int i=0; i<n; i++) {
	         System.out.println(frequency[i] + " " + wrds[i]);
	     }*/
	     writeOutputFile(n,wrds,frequency,OutputFile);
	     
	     System.out.println("\nNumber of input words: " + counter.getWordCount());
	     System.out.println("Number of unique words: " + n);


		
     } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
     return wrds;
          

}

private void writeOutputFile(int n,String[] wrds, int[] frequency, File outputFile) {
	BufferedWriter bufferedWriter = null;
	try {
		bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		for (int i=n-1; i>=0; i--) {
	        bufferedWriter.write(wrds[i]+" "+frequency[i]);
	        bufferedWriter.newLine();
	        
	     }
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

}
