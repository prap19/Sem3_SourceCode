package edu.nlp.ageattr;

import java.io.File;
import java.util.HashMap;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * SVNInterface implementations are actually feature vector implementations
 * Implement this interface when you want to include new types of feature vector.
 * 
 * @author prasad
 *
 */
public interface SVNInterface {
	//TODO: Change the location of testing data.
	
	
	//Training Dataset
	public static final String RSRC_TRAIN_POSDATA ="RSRC_TRAIN_POSDATA";
	public static final String RSRC_TRAIN_TXT ="RSRC_TRAIN_TXT";
	
	//TestingDataset
	public static final String RSRC_TEST_POSDATA ="RSRC_TEST_POSDATA";
	public static final String RSRC_TEST_TXT ="RSRC_TEST_TXT";
	
	/**
	 * This method will return number of attributes that the implemented class will add in FastVector.
	 * This is capacity parameter required while creating a FastVector object.
	 * @return number of attributes.
	 */
	public int getNumberOfAttributes();
	
	/**
	 * This method will add attribute names in feature vector(weka object in this case)
	 * @param featureList 
	 */
	public void addAttributes(FastVector fastVector, HashMap<String,Integer> featureList);
	
	
	/**
	 * This method will evaluate the values in the feature vector
	 * @param train 
	 * @param populateFile 
	 * @param featureList 
	 */
	public void addVector(FastVector fastVector, Instance trainInstance, boolean train, PopulateFile populateFile, HashMap<String,Integer> featureList);
	
	/**
	 * This method will get required file for populating the feature vector from a specified data folder at a specified Index 
	 * @param FolderName
	 * @param fileIndex
	 * @return
	 */
	public File getDataFile(String FolderName);
}
