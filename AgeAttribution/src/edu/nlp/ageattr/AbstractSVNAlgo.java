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

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author prasad
 *
 */
public abstract class AbstractSVNAlgo {
	//TODO: Create instances object in trainer method
	
	// Weka Objects
	 private FastVector      fastvector;
	 private FastVector		 fastvectorClass;
	 private Instances       instances;
	 private Instance		 instance;
	 private Attribute		 classAttribute;
	 
	 /**
	 *  Sub-class should add SVN interfaces objects in this arraylist. 
	 */
	protected ArrayList<SVNInterface> arraylist = new ArrayList<SVNInterface>();
	
	private void init() {
		this.addSVNInterfaces();
		this.setFastVectorCapacity();
		this.addAttributes();
		this.instances = new Instances("myInstances", this.fastvector, 10);
		this.instances.setClassIndex(fastvector.capacity()-1);
		
	}
	
	 private void setFastVectorCapacity() {
		 int num=0;
		 for(SVNInterface svnInterface: this.arraylist) {
			 	num+= svnInterface.getNumberOfAttributes();
			}
		 this.fastvector = new FastVector(num+1);   // 1 extra for Class attribute
	 }
	 
	/**
	 * Extend this method to add combination of SVNInterfaces in arraylist
	 */
	abstract protected void addSVNInterfaces();
	
	private void addAttributes() {

		for(SVNInterface svnInterface: this.arraylist) {
			svnInterface.addAttributes(this.fastvector);						
		}
		
		this.fastvectorClass = new FastVector(3);
		this.fastvectorClass.addElement("20s");
		this.fastvectorClass.addElement("30s");
		this.fastvectorClass.addElement("40s");
		this.classAttribute = new Attribute("theClass", this.fastvectorClass);
		this.fastvector.addElement(this.classAttribute)	;
	}
	
	protected void displayAttributes() {
		for(int i=0; i<this.fastvector.size(); i++)
			System.out.println(fastvector.elementAt(i).toString());
	}
	
	private void populateVector() {

		for(SVNInterface svnInterface: this.arraylist) {
			svnInterface.addVector(this.fastvector, this.instances);
		}
	}
	
	public void trainClassifier() {
		System.out.println("Train Classifier");
	}
	
	public void testClassifier() {
		System.out.println("test Classifier");
	}
	
	public final void run() {
		this.init();
		this.populateVector();
		trainClassifier();
		testClassifier();
	}
}
