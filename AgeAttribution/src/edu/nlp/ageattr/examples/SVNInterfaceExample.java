package edu.nlp.ageattr.examples;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import edu.nlp.ageattr.SVNInterface;

public class SVNInterfaceExample implements SVNInterface {

	Attribute attribute = new Attribute("attr1");
	Attribute attribute2 = new Attribute("attr2");
	private String trainingSet = SVNInterface.POSTAGGEDFILE;
	
	@Override
	public int getNumberOfAttributes() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public void addAttributes(FastVector fastVector) {
		// TODO Auto-generated method stub
		
		fastVector.addElement(attribute);
		fastVector.addElement(attribute2);
	}

	@Override
	public void addVector(FastVector fastVector, Instances instances) {
		// TODO Auto-generated method stub
		
		 Instance iExample = new Instance(fastVector.capacity());
		 iExample.setValue((Attribute)fastVector.elementAt(0), 1.0);      
		 iExample.setValue((Attribute)fastVector.elementAt(1), 0.5);      
		 iExample.setValue((Attribute)fastVector.elementAt(2), "20s");
		 
		  
		 // add the instance
		 instances.add(iExample);
		 System.out.println(instances);
		
	}

}
