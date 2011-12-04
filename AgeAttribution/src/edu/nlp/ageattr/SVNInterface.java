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
	public static final String RSRC_TRAIN_POSDATA ="rsrc"+File.separator+"POSDataset";
	public static final String RSRC_TRAIN_TXT ="rsrc"+File.separator+"TextFiles";
	
	
	//TestingDataset
	public static final String RSRC_TEST_POSDATA ="rsrc"+File.separator+"POSDataset";
	public static final String RSRC_TEST_TXT ="rsrc"+File.separator+"TextFiles";
	
	/**
	 * This method will return number of attributes that the implemented class will add in FastVector.
	 * This is capacity parameter required while creating a FastVector object.
	 * @return number of attributes.
	 */
	public int getNumberOfAttributes();
	
	/**
	 * This method will add attribute names in feature vector(weka object in this case)
	 */
	public void addAttributes(FastVector fastVector);
	
	
	/**
	 * This method will evaluate the values in the feature vector
	 * @param train 
	 * @param fileIndex 
	 */
	public void addVector(FastVector fastVector, Instance trainInstance, boolean train, int fileIndex);
	
	/**
	 * This method will get required file for populating the feature vector from a specified data folder at a specified Index 
	 * @param FolderName
	 * @param fileIndex
	 * @return
	 */
	public File getDataFile(String FolderName,int fileIndex);
}
