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

import weka.classifiers.functions.LibSVM;
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
	 private Instances       trainInstances;
	 private Instances       testInstances;
	 private Instance		 instance;
	 private Attribute		 classAttribute;
	 private LibSVM			 libsvm;
	 
	 /**
	 *  Sub-class should add SVN interfaces objects in this arraylist. 
	 */
	protected ArrayList<SVNInterface> arraylist = new ArrayList<SVNInterface>();
	
	private void init() {
		this.addSVNInterfaces();
		this.setFastVectorCapacity();
		this.addAttributes();
		this.trainInstances = new Instances("trainInstances", this.fastvector, 10);
		this.testInstances = new Instances("testInstances", this.fastvector, 10);
		this.trainInstances.setClassIndex(fastvector.capacity()-1);
		this.testInstances.setClassIndex(fastvector.capacity()-1);
		this.libsvm = new LibSVM();
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
			svnInterface.addVector(this.fastvector, this.trainInstances);
			svnInterface.addVector(this.fastvector, this.testInstances);
		}
	}
	
	public void trainClassifier() {
		System.err.println("Train Classifier");
		try {
			libsvm.buildClassifier(this.trainInstances);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testClassifier() {
		System.err.println("test Classifier");
		for(int i=0; i<this.testInstances.numInstances(); i++) {
			Double prediction = 0.0;
			try {
				prediction = libsvm.classifyInstance(this.testInstances.instance(i));
				
				System.out.print("ID: " + testInstances.instance(i).value(0));
                System.out.print(", actual: " + testInstances.classAttribute().value((int) testInstances.instance(i).classValue()));
                System.out.println(", predicted: " + testInstances.classAttribute().value(prediction.intValue()));
                
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public final void run() {
		this.init();
		this.populateVector();
		trainClassifier();
		testClassifier();
	}
}
