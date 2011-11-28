package edu.nlp.ageattr.Baseline;
/**
 * @author piyush
 */
import java.io.File;
import java.util.HashMap;


import edu.nlp.ageattr.helper.StopWord;
import edu.nlp.ageattr.processDataset.CreateDataset;
import edu.nlp.ageattr.processDataset.PersonBlogs;

public class NaiveBayesTrain {
	int VocabularySize, CountofWordsInTeens,CountofWordsInTwenties ,CountofWordsInThirties ;
	static StopWord stopWord;
	public NaiveBayesTrain(){
		stopWord = new StopWord();
		
		StopWord.StopWordCreate();
		VocabularySize = 0;
		CountofWordsInTeens = 0;
		CountofWordsInThirties = 0;
		CountofWordsInTwenties=0;
	}
	/**
	 * Trains the naive bayes algo by creating map of the word probabilities given the classification classes
	 * @param cleanXmlFolder
	 * @param numberoftraingposts
	 * @return
	 */
	public HashMap<String, HashMap<String, Integer>> trainNaiveBayes(
			File cleanXmlFolder, int numberoftraingposts) {
		HashMap<String, HashMap<String, Integer>> NaiveBayesMap = new HashMap<String, HashMap<String,Integer>>();
		File [] files;
		files = cleanXmlFolder.listFiles();
		int count =0,countOfFilesProcessed=0;
	    while(count<2500){
	    	/**
			 * take one xml clean file and find updtae the probability map due to it.
			 */
			for (final File fileEntry : files) {
				if(count<2500){
					count++;
					System.out.println("trained using "+count+"files");
			        	String fileName = CreateDataset.proceesFileName(fileEntry);
			        	PersonBlogs pbBlogs;
			        	pbBlogs  = CreateDataset.CreatePersonBlog(fileEntry.getAbsoluteFile(),fileName);
			        	if(pbBlogs!=null){
			        		countOfFilesProcessed++;
			        		updateNaiveBayesMap(pbBlogs,NaiveBayesMap);
			        	}

				}else{
					break;
				}
						         
			}
			System.out.println("The no of files processed are....... "+countOfFilesProcessed);
	
	    }
				return NaiveBayesMap;
	}
	/**
	 * Given a persons blogs updates the naive bayes map of word probabilities
	 * @param pbBlogs
	 * @param naiveBayesMap
	 */
	private void updateNaiveBayesMap(PersonBlogs pbBlogs,
			HashMap<String, HashMap<String, Integer>> naiveBayesMap) {
		String AgeClass = pbBlogs.getBloggerAgeClass();
		for(int i =0;i<pbBlogs.getPosts().size();i++){
			String post = pbBlogs.getPosts().get(i);
			String [] cleanPostTokens = NaiveBayesTrain.cleanPost(post);
			updateNaiveBayesMapForSinglePost(cleanPostTokens,naiveBayesMap,AgeClass);
		}
	}
	
/**
 * remove the stop words, punctuation marks etc and use the post to update the naive Bayes map
 */
public static String[] cleanPost(String post){
	String cleanPost = NaiveBayesTrain.removePuctuationMarks(post);
	String [] postTokens = cleanPost.split("\\s+");
	String [] cleanPostTokens = stopWord.removeStopWords(postTokens);
	return cleanPostTokens;
}
/**
 * update the naive bayes map for a single post	
 * @param cleanPostTokens
 * @param naiveBayesMap
 * @param ageClass 
 */
private void updateNaiveBayesMapForSinglePost(String[] cleanPostTokens,
			HashMap<String, HashMap<String, Integer>> naiveBayesMap, String ageClass) {
		for(int i=0;i<cleanPostTokens.length;i++){
			updateClassWordCount(ageClass);
			/**
			 * if the word is already present just update its count in the respective class
			 */
			if(naiveBayesMap.containsKey(cleanPostTokens[i])){
				HashMap<String , Integer> classMap;
				classMap = naiveBayesMap.get(cleanPostTokens[i]);
				/**
				 * if the word is present given the age class of the blogger increase its frequency by 1
				 */
				if(classMap.containsKey(ageClass)){
					int val = classMap.get(ageClass);
					classMap.put(ageClass, val+1);
				}else{
					classMap.put(ageClass, 1);
				}
			}
			/**
			 * word not present hence create new entry
			 */
			else{
				HashMap<String, Integer> classMap = new HashMap<String, Integer>();
				classMap.put(ageClass, 1);
				naiveBayesMap.put(cleanPostTokens[i], classMap);
			}
		}
		
	}

/**
 * update the word count of the respective class
 * @param ageClass
 */
private void updateClassWordCount(String ageClass) {
	if(ageClass.equalsIgnoreCase("teens")){
		this.setCountofWordsInTeens(getCountofWordsInTeens()+1);
	}else if(ageClass.equalsIgnoreCase("twenties")){
		this.setCountofWordsInTwenties(getCountofWordsInTwenties()+1);
	}else{
		this.setCountofWordsInThirties(getCountofWordsInThirties()+1);
	}
}
/**
 * removes all punctuation marks from  a post
 * @param post
 * @return
 */
	private static String removePuctuationMarks(String post) {
		String postWithoutFullStpos = post.replaceAll("\\.", " "); 
		String cleanLine = postWithoutFullStpos.replaceAll("[^\\w&&[^\\s]]", "");
		return cleanLine;
	}
	/**
	 * @return the vocabularySize
	 */
	public int getVocabularySize() {
		return VocabularySize;
	}

	/**
	 * @param vocabularySize the vocabularySize to set
	 */
	public void setVocabularySize(int vocabularySize) {
		VocabularySize = vocabularySize;
	}

	/**
	 * @return the countofWordsInTeens
	 */
	public int getCountofWordsInTeens() {
		return CountofWordsInTeens;
	}

	/**
	 * @param countofWordsInTeens the countofWordsInTeens to set
	 */
	public void setCountofWordsInTeens(int countofWordsInTeens) {
		CountofWordsInTeens = countofWordsInTeens;
	}

	/**
	 * @return the countofWordsInTwenties
	 */
	public int getCountofWordsInTwenties() {
		return CountofWordsInTwenties;
	}

	/**
	 * @param countofWordsInTwenties the countofWordsInTwenties to set
	 */
	public void setCountofWordsInTwenties(int countofWordsInTwenties) {
		CountofWordsInTwenties = countofWordsInTwenties;
	}

	/**
	 * @return the countofWordsInThirties
	 */
	public int getCountofWordsInThirties() {
		return CountofWordsInThirties;
	}

	/**
	 * @param countofWordsInThirties the countofWordsInThirties to set
	 */
	public void setCountofWordsInThirties(int countofWordsInThirties) {
		CountofWordsInThirties = countofWordsInThirties;
	}

}
