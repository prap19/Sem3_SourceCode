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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
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
	 private FastVector      	fastvector;
	 private FastVector		 	fastvectorClass;
	 private Instances       	trainInstances;
	 private Instances       	testInstances;
	 private Instance		 	trainInstance;
	 private Instance		 	testInstance;
	 private Attribute		 	classAttribute;
	 private SMO			 	smo;
	 private int				FileIndex;
	 private int 				noOfTrainingInstances;
	 private int 				noOfTestingInstances;
	 private PopulateFile       populateFile;
	 public  HashMap<String, Integer> featureList; 
	 //public static final String RSRC_TRAIN_POSDATA ="rsrc"+File.separator+"TrainPOSDataset";
	 public static final String RSRC_TRAIN_TXT ="C:\\Data\\AgePredictionDataset\\"+File.separator+"3000TrainTextFiles";
	 //public static final String RSRC_TEST_POSDATA ="rsrc"+File.separator+"TestPOSDataset";
	 public static final String RSRC_TEST_TXT ="C:\\Data\\AgePredictionDataset\\"+File.separator+"TestTextFiles";
		 	
	 
	 /**
	 *  Sub-class should add SVN interfaces objects in this arraylist. 
	 */
	protected ArrayList<SVNInterface> arraylist = new ArrayList<SVNInterface>();
	
	private void init() {
		
		this.addSVNInterfaces();
		this.setFastVectorCapacity();
		this.featureList = new HashMap<String, Integer>();
		this.addAttributes();
		this.trainInstances = new Instances("trainInstances", this.fastvector, 10);
		this.testInstances = new Instances("testInstances", this.fastvector, 10);
		this.trainInstances.setClassIndex(fastvector.capacity()-1);
		this.testInstances.setClassIndex(fastvector.capacity()-1);
		this.smo = new SMO();
		/**
		 * setting up the parameters for the SMO Classifier
		 */
		try {
			smo.setOptions(weka.core.Utils.splitOptions("-G 0.01 -R "));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setNoOfTrainingInstances(RSRC_TRAIN_TXT);
		this.setNoOfTestingInstances(RSRC_TEST_TXT);
		this.populateFile = new PopulateFile();
		
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
			svnInterface.addAttributes(this.fastvector,this.featureList);						
		}
		
		this.fastvectorClass = new FastVector(3);
		this.fastvectorClass.addElement("10s");
		this.fastvectorClass.addElement("20s");
		this.fastvectorClass.addElement("30s");
		this.classAttribute = new Attribute("theClass", this.fastvectorClass);
		this.fastvector.addElement(this.classAttribute)	;
	}
	
	protected void displayAttributes() {
		for(int i=0; i<this.fastvector.size(); i++)
			System.out.println(fastvector.elementAt(i).toString());
	}
	/**
	 * @param no 3 if true is for populating the training instance and if false it is for testing instance
	 */
	private void populateVector() {
		for(int i=0; i<this.getNoOfTrainingInstances(); i++){
			//FileMap = getAllFeatureFilesForIthInstance(i,RSRC_TRAIN_TXT,RSRC_TRAIN_POSDATA);
			FileIndex = i;
			this.populateFile.setFileMap(FileIndex);
			for(SVNInterface svnInterface: this.arraylist) {
				trainInstance = new Instance(this.fastvector.capacity());
				svnInterface.addVector(this.fastvector, trainInstance,true,this.populateFile,this.featureList);
				this.trainInstances.add(trainInstance);
			}
			
		}
		
		for(int i=0; i<this.getNoOfTestingInstances(); i++){
			for(SVNInterface svnInterface: this.arraylist) {
				//FileMap = getAllFeatureFilesForIthInstance(i,RSRC_TEST_TXT,RSRC_TEST_POSDATA);
				FileIndex = i;
				this.populateFile.setFileMap(FileIndex);
				testInstance = new Instance(this.fastvector.capacity());
				svnInterface.addVector(this.fastvector, testInstance,false,this.populateFile,this.featureList);
				this.testInstances.add(testInstance);
			}
		}	
	}
	

	public void setNoOfTrainingInstances(String FolderName){
		final File folder = new File(FolderName);
		this.noOfTrainingInstances = folder.listFiles().length;
	}
	
	public int getNoOfTrainingInstances(){
		return this.noOfTrainingInstances;
	}
	
	public void setNoOfTestingInstances(String FolderName){
		final File folder = new File(FolderName);
		this.noOfTestingInstances = folder.listFiles().length;
	}
	
	public int getNoOfTestingInstances(){
		return this.noOfTestingInstances;
	}
	
	public void trainClassifier() {
		System.err.println("Train Classifier");
		try {
			smo.buildClassifier(this.trainInstances);
			
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
				prediction = smo.classifyInstance(this.testInstances.instance(i));
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
