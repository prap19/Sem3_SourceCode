package edu.nlp.ageattr.processDataset;
/**
 * @author piyush
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


import edu.nlp.ageattr.Baseline.NaiveBayesTest;
import edu.nlp.ageattr.Baseline.NaiveBayesTrain;
import edu.nlp.ageattr.helper.SampleWeka;

public class AgePredictionMain {
	static final File cleanXmlFolderTest = new File("rsrc/CleanXML");
	static final File DataTextFolder = new File("rsrc/TextFiles");
	static final File XmlDataset = new File("rsrc/CleanXmlDataset");
	static final File POSTaggedDataset = new File("rsrc/POSDataset");
	static final int numberOfTraingPosts = 9000;
	static HashMap<String, HashMap<String, Integer>> naiveBayesMap;
	static ArrayList<PersonBlogs> TrainingPosts;
	static CreateDataset createDataset;
	static NaiveBayesTrain naiveBayes;
	static NaiveBayesTest naiveBayesTest;
	static SampleWeka sampleWeka;
	public static void main(String[] args) throws FileNotFoundException {
		/**
		 * create clean xml files
		 */
		createDataset = new CreateDataset();
		final File folder = new File("rsrc/blogs/blogs");
		final File cleanXmlFolder = new File("rsrc/CleanXMLTrain");
		String cleanXmlFolderName = "C:\\Users\\piyush\\workspace\\AgePrediction\\rsrc\\CleanXMLTrain\\";
	//	createDataset.createCleanXMLFiles(folder,cleanXmlFolderName);
		
		/**
		 * create Dataset of the person blogs from the clean xml files
		 */
/*		TrainingPosts = createDataset.createTrainingData(cleanXmlFolder);
		System.out.println("The training post size is "+TrainingPosts.size());
*/		
		/**
		 * implement naive bayes algo
		 */
/*		naiveBayes = new NaiveBayesTrain();
		naiveBayesMap = naiveBayes.trainNaiveBayes(cleanXmlFolder,numberOfTraingPosts);
		System.out.println("the no of words in the naive bayes map are "+naiveBayesMap.size());
		naiveBayesTest = new NaiveBayesTest();
		float accuracy = naiveBayesTest.testNaiveBayes(naiveBayesMap,cleanXmlFolderTest,naiveBayes);
		System.out.println("The accuracy using naive bayes is "+accuracy);
*/		
		/**
		 * create text data files 
		 */
		//createDataset.createTextDataFiles(XmlDataset,DataTextFolder);
		/**
		 * create pos tagged text files
		 */
	//	createDataset.createPosTaggedFiles(DataTextFolder,POSTaggedDataset);
		
		/**
		 * testing weka classifer
		 */
		sampleWeka = new SampleWeka();
		sampleWeka.addFeatures();
	}

}
