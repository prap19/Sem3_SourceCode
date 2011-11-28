package edu.nlp.ageattr.Baseline;
/**
 * @author piyush
 */
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import edu.nlp.ageattr.processDataset.CreateDataset;
import edu.nlp.ageattr.processDataset.PersonBlogs;

public class NaiveBayesTest {
	int numOfClasses = 3;
	public float testNaiveBayes(
			HashMap<String, HashMap<String, Integer>> naiveBayesMap,
			File cleanxmlfoldertest, NaiveBayesTrain naiveBayes) {
		float accuracy = 0;
		int classified = 0,misclassified =0,count=0;
		File [] files;
		files = cleanxmlfoldertest.listFiles();
			for (final File fileEntry : files) {
		        count++;
		        System.out.println("the test count is "+count);
				if(count>1000){
		        	break;
		        }
				String fileName = CreateDataset.proceesFileName(fileEntry); 
		       if(isPersonCorrectlyClassified(fileEntry,fileName,naiveBayesMap,naiveBayes)){
		    	   classified++;
		       }else{
		    	   misclassified++;
		       }
		   }
	
			 accuracy = (float) classified/(classified+misclassified);	   
		 return accuracy;
	}
/**
 * returns true i fthe person was correctly classifed acoording to the naive bayes algorithm
 * @param fileEntry
 * @param fileName
 * @param naiveBayesMap
 * @param naiveBayes 
 * @return
 */
	private boolean isPersonCorrectlyClassified(File fileEntry,
			String fileName,
			HashMap<String, HashMap<String, Integer>> naiveBayesMap, NaiveBayesTrain naiveBayes) {
		PersonBlogs pbBlogs;
    	pbBlogs  = CreateDataset.CreatePersonBlog(fileEntry.getAbsoluteFile(),fileName);
    	if(pbBlogs!=null){
    		String AgeClass = pbBlogs.getBloggerAgeClass();
    		if(AgeClass.equalsIgnoreCase(predictClassFromBlogs(pbBlogs,naiveBayesMap,naiveBayes))){
    			return true;
    		}
    	}
		return false;
	}

/**
 * predict a persons age from his provided blogs	
 * @param pbBlogs
 * @param naiveBayesMap
 * @param naiveBayes 
 * @return
 */
private String predictClassFromBlogs(PersonBlogs pbBlogs,
		HashMap<String, HashMap<String, Integer>> naiveBayesMap, NaiveBayesTrain naiveBayes) {
	String predictedAgeClass="";
	double product=0, classProduct=-999999;
	for(int i=0;i<numOfClasses;i++){
		
		product = calculateProductOfProbGivenClass(pbBlogs.getPosts(),getClassName(i),naiveBayesMap,naiveBayes);
		if(classProduct<product){
			classProduct = product;
			predictedAgeClass = getClassName(i);
		}
	}
	return predictedAgeClass;
}

/**
 * calculates the product of the word probabilties in a blog given the bloggers age class 
 * @param posts
 * @param className
 * @param naiveBayesMap 
 * @param naiveBayes 
 * @return
 */
private double calculateProductOfProbGivenClass(ArrayList<String> posts,
		String className, HashMap<String,HashMap<String,Integer>> naiveBayesMap, NaiveBayesTrain naiveBayes) {
	double product =0;
	for(int i=0;i<posts.size();i++){
		String post = posts.get(i);
		String [] cleanPostTokens = NaiveBayesTrain.cleanPost(post);
		for(int j=0;j<cleanPostTokens.length;j++){
			product +=Math.log(findProbFromNaiveBayesMap(cleanPostTokens[j],naiveBayesMap,naiveBayes,className)); 
		}
	}
	return product;
}
/**
 * given a word calculates the probability of that word from the trained naive bayes map
 * @param string
 * @param naiveBayesMap 
 * @param naiveBayes 
 * @param className 
 * @return
 */
private double findProbFromNaiveBayesMap(String word, HashMap<String,HashMap<String,Integer>> naiveBayesMap,
		NaiveBayesTrain naiveBayes, String className) {
	double probability = 0;
	int wordCount =0;
	if(naiveBayesMap.containsKey(word)){
		HashMap<String, Integer> temp = naiveBayesMap.get(word);
		if(temp.containsKey(className)){
			wordCount = naiveBayesMap.get(word).get(className);
		}
	}
	probability = (double)(wordCount + 1)/(naiveBayesMap.size()+getClassWordCount(className,naiveBayes));
	return probability;
}

private int getClassWordCount(String className, NaiveBayesTrain naiveBayes) {
	if(className.equals("teens")){
		return(naiveBayes.getCountofWordsInTeens());	
	}else if(className.equals("twenties")){
		return(naiveBayes.getCountofWordsInTwenties());
	}else{
		return(naiveBayes.getCountofWordsInThirties());
	}
}
/**
 * returns the class name corresponding to a number
 * @param i
 * @return
 */
private String getClassName(int i) {
	if(i==0){
		return "teens";
	}else if(i==1){
		return "twenties";
	}else{
		return "thirtees";
	}
}
}
