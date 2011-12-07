package edu.nlp.ageattr.interfaces;

import java.io.File;
import java.util.HashMap;

import weka.core.FastVector;
import weka.core.Instance;
import edu.nlp.ageattr.PopulateFile;
import edu.nlp.ageattr.SVNInterface;

public class POSCollocationFeature implements SVNInterface {

	@Override
	public int getNumberOfAttributes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addAttributes(FastVector fastVector,
			HashMap<String, Integer> featureList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addVector(FastVector fastVector, Instance trainInstance,
			boolean train, PopulateFile populateFile,
			HashMap<String, Integer> featureList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public File getDataFile(String FolderName) {
		// TODO Auto-generated method stub
		return null;
	}

}
