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
import edu.nlp.ageattr.helper.LexicalContentFeatures.TopWords;
import edu.nlp.ageattr.helper.wordFrequency.*;;

public class AgePredictionMain {
	static final File cleanXmlFolderTest = new File(System.getenv("NLP")+"/CleanXML");
	static final File DataTextFolder = new File(System.getenv("NLP")+"/TextFiles");
	static final File XmlDataset = new File(System.getenv("NLP")+"/CleanXmlDataset");
	static final File SingleTeensFile = new File(System.getenv("NLP")+"//ConcatenatedFiles//TeensFileTrain.txt");
	static final File SingleTwentiesFile = new File(System.getenv("NLP")+"//ConcatenatedFiles//TwentiesFileTrain.txt");
	static final File SingleThiriesFile = new File(System.getenv("NLP")+"//ConcatenatedFiles//ThirteesFileTrain.txt");
	
	static final File TeensFileWordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//OrderedLowerTeensFileTrainOut.txt");
	static final File TwentiesFileWordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//OrderedLowerTwentiesFileTrainOut.txt");
	static final File ThiriesFileWordFreq = new File(System.getenv("NLP")+"//ConcatenatedFiles//OrderedLowerThirteesFileTrainOut.txt");
	
	static final File StopWordFile = new File("rsrc/NewStopWordList.txt");
	static final File SamplePOSTaggedDataset = new File(System.getenv("NLP")+"/samplePOSDataset");
	static final File POSTaggedDataset = new File(System.getenv("NLP")+"/POSDataset");
	static final File sample = new File(System.getenv("NLP")+"/smallSample");
	static final File FrequencyOutputFile = new File(System.getenv("NLP")+"/ConcatenatedFiles/TeensOutputFile.txt");
	static final File TrainFolder = new File(System.getenv("NLP")+"//TrainTextFiles");
	static final File TestFolder = new File(System.getenv("NLP")+"/TestTextFiles");
	
	static final int numberOfTraingPosts = 9000;
	static HashMap<String, HashMap<String, Integer>> naiveBayesMap;
	static ArrayList<PersonBlogs> TrainingPosts;
	static CreateDataset createDataset;
	static NaiveBayesTrain naiveBayes;
	static NaiveBayesTest naiveBayesTest;
	static SampleWeka sampleWeka;
	static WordFrequencyCmd wCmd;
	static TopWords topWords;
	public static void main(String[] args) throws FileNotFoundException {
		/**
		 * create clean xml files
		 */
		createDataset = new CreateDataset();
		//final File folder = new File("rsrc/blogs/blogs");
		//final File cleanXmlFolder = new File("rsrc/CleanXMLTrain");
		//String cleanXmlFolderName = "C:\\Users\\piyush\\workspace\\AgePrediction\\rsrc\\CleanXMLTrain\\";
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
	//	createDataset.createPosTaggedFiles(sample,SamplePOSTaggedDataset);
		
		/**
		 * testing weka classifer
		 */
/*		sampleWeka = new SampleWeka();
		sampleWeka.addFeatures();*/
		/**
		 * creating concatenated twenties, teens and Thirtees files 
		 */
	//	createDataset.createConcatenatedFiles(TrainFolder,SingleTeensFile,SingleThiriesFile,SingleTwentiesFile);
		/**
		 * creating word frequency files
		 */
/*		wCmd = new WordFrequencyCmd();
		System.out.println("starting teens file");
		wCmd.get50MostFrequentlyOccuringWords(SingleTeensFile, StopWordFile,TeensFileWordFreq);
		System.out.println("starting twenties file");
		wCmd.get50MostFrequentlyOccuringWords(SingleTwentiesFile, StopWordFile,TwentiesFileWordFreq);
		System.out.println("starting thirtees file");
		wCmd.get50MostFrequentlyOccuringWords(SingleThiriesFile, StopWordFile,ThiriesFileWordFreq);*/
		/**
		 * create Top 50 wor map
		 */
		topWords = new TopWords();
		topWords.create();
	}

}
