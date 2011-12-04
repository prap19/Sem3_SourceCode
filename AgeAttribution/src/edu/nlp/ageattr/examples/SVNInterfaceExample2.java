package edu.nlp.ageattr.examples;

import java.io.File;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import edu.nlp.ageattr.SVNInterface;

public class SVNInterfaceExample2 implements SVNInterface{
	Attribute attribute3 = new Attribute("attr3");
	
	@Override
	public int getNumberOfAttributes() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void addAttributes(FastVector fastVector) {
		// TODO Auto-generated method stub
		fastVector.addElement(attribute3);
	}

	@Override
	public void addVector(FastVector fastVector, Instance instance, boolean train,int fileIndex) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public File getDataFile(String FolderName, int fileIndex) {
		return null;
		
	}

}
