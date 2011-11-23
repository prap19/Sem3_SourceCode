package edu.nlp.ageattr;

import weka.core.FastVector;
import weka.core.Instances;

/**
 * SVNInterface implementations are actually feature vector implementations
 * Implement this interface when you want to include new types of feature vector.
 * 
 * @author prasad
 *
 */
public interface SVNInterface {
	//TODO: Add weka objects into parameters
	
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
	 */
	public void addVector(FastVector fastVector, Instances instances);
}
