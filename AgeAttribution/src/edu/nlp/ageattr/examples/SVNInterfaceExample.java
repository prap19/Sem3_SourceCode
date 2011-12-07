package edu.nlp.ageattr.examples;

import java.io.File;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import edu.nlp.ageattr.PopulateFile;
import edu.nlp.ageattr.SVNInterface;

public class SVNInterfaceExample implements SVNInterface {

	Attribute attribute = new Attribute("attr1");
	Attribute attribute2 = new Attribute("attr2");
	//private String trainingSet = SVNInterface.RSRC_TRAIN_TXT;
	
	
	@Override
	public int getNumberOfAttributes() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void addAttributes(FastVector fastVector ,HashMap<String,Integer> featureList) {
		// TODO Auto-generated method stub
		int featureCount = featureList.size();
		fastVector.addElement(attribute);
		featureList.put("attr1", featureCount++);
		fastVector.addElement(attribute2);
		featureList.put("attr2", featureCount++);
	}

	@Override
	public void addVector(FastVector fastVector, Instance instance, boolean train,PopulateFile populateFile,HashMap<String,Integer> featureList) {
		
		if(train == true){
			File textFile = populateFile.getFileMap(RSRC_TRAIN_TXT);
			
			//Instance iExample = new Instance(fastVector.capacity());
			 instance.setValue((Attribute)fastVector.elementAt(0), 1.0);      
			 instance.setValue((Attribute)fastVector.elementAt(1), 0.5);      
			 instance.setValue((Attribute)fastVector.elementAt(2), "20s");
			 
			// add the instance
			// instances.add(iExample);
			 
	/*		 Instance iExample1 = new Instance(fastVector.capacity());
			 iExample1.setValue((Attribute)fastVector.elementAt(0), 2.0);      
			 iExample1.setValue((Attribute)fastVector.elementAt(1), 0.5);      
			 iExample1.setValue((Attribute)fastVector.elementAt(2), "30s");
		
			// add the instance
			 instances.add(iExample1);
	*/			 
			
			 System.out.println("the training instace is "+instance);	
		}else{
			Instance iExample = new Instance(fastVector.capacity());
			 iExample.setValue((Attribute)fastVector.elementAt(0), 1.0);      
			 iExample.setValue((Attribute)fastVector.elementAt(1), 0.5);      
			 iExample.setValue((Attribute)fastVector.elementAt(2), "20s");
			 
			// add the instance
			// instances.add(iExample);
		}
		 
		
	}

	@Override
	public File getDataFile(String FolderName) {
		return null;
	}

}
