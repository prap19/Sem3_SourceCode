package edu.nlp.ageattr.helper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * Used for Part of speech tagging of the sentence 
 * @author piyush
 *
 */
public class PosTaggging {
	private String textString;
    public static MaxentTagger tagger;
    
    /**
     * Initializes the Stanford Pos Tagger
     * @throws ClassNotFoundException 
     * @throws IOException 
     */
    public PosTaggging() throws IOException, ClassNotFoundException
    {
    	tagger = new MaxentTagger("rsrc/left3words-distsim-wsj-0-18.tagger");
    }
    /**
     * Performs Part of Speech Tagging of a file  
     * @param InputFile
     * @param OutputFile
     * @throws IOException 
     */
    public void TagFile(String InputFile, String OutputFile) throws IOException
    {
    	BufferedWriter bufferedWriter =null;
		BufferedReader bufferedReader = null;
		String line="";
		int count=0;
		String taggedSentence = new String();
    	try
		{
    		bufferedReader = new BufferedReader(new FileReader(InputFile));
    		bufferedWriter = new BufferedWriter(new FileWriter(OutputFile));
    		while((line=bufferedReader.readLine())!=null)
    		{
    			//System.out.println("line before tagging "+line);
    			List<ArrayList<? extends HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader(line));
    			
    			for (ArrayList<? extends HasWord> sentence : sentences) {
    			    taggedSentence ="";    
    				ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
    				taggedSentence += tSentence.size()+" ";   
    				for (TaggedWord tWord : tSentence) {
    		               if (tWord.tag().length() > 0) {
    		            	   taggedSentence += tWord.word()+" "+tWord.tag()+" ";
    		               }
    			       }
    				if(taggedSentence!=null){
        				count ++;
        				//System.out.println("the tagged sentence is "+count);
        				bufferedWriter.write(taggedSentence);
        				bufferedWriter.newLine();
        			}
    			   }
    		//taggedSentence = "";
    		//System.out.println("tagged sentence count is "+count);
    		}
    		//System.out.println("the count is "+count);
		}
    	catch(FileNotFoundException e)
    	{
    		e.getMessage();
    	}
    	finally
    	{
    		bufferedReader.close();
    		if(bufferedWriter!=null){
    			bufferedWriter.flush();
    			bufferedWriter.close();
    		}
    	}
    }
}
