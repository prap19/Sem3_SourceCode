/**
 * Extend this class and implement addSVNInterfaces(). addSVNInterfaces() will add SVN interfaces in arraylist.
 * Example: 
 * 		void addSVNInterfaces() {
 * 			arraylist.add(new SentenceComplex());
 * 			arraylist.add(new WordCount());
 * 		}
 * 
 * SentenceComplex(), WordCount() is an SVNInterface implementation. 
 * Arraylist contains the combination of SVNInterfaces that needs an analysis.   
 * The analysis will contain features from both the set SentenceComplexity and WordCount. 
 */
package edu.nlp.ageattr;

import java.util.ArrayList;
import weka.core.FastVector;
import weka.core.Instances;

/**
 * @author prasad
 *
 */
public abstract class AbstractSVNAlgo {
	//TODO: Create dataInstances object in trainer method
	
	
	// Weka Objects
	 private FastVector      atts;
	 private Instances       dataInstances;
	
	 ArrayList<SVNInterface> arraylist = new ArrayList<SVNInterface>();
	
	
	 private void setFastVectorCapacity() {
		 int num=0;
		 for(SVNInterface svnInterface: this.arraylist) {
			 	num+= svnInterface.getNumberOfAttributes();
			}
		 this.atts = new FastVector(num);
	 }
	 
	/**
	 * Extend this method to add combination of SVNInterfaces in arraylist
	 */
	abstract void addSVNInterfaces();
	
	private void addAttributes() {

		for(SVNInterface svnInterface: this.arraylist) {
			svnInterface.addAttributes(this.atts);						
		//	svnInterface.addVector();
		}
	}
	
	private void populateVector() {

		for(SVNInterface svnInterface: this.arraylist) {
			svnInterface.addVector(this.atts);						
		}
	}
	
	public void trainClassifier() {
		System.out.println("Train Classifier");
	}
	
	public void testClassifier() {
		System.out.println("test Classifier");
	}
	
	public final void run() {
		this.addSVNInterfaces();
		this.addAttributes();
		this.setFastVectorCapacity();
		this.populateVector();
		trainClassifier();
		testClassifier();
	}
}
