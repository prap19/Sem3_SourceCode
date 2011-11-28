package edu.nlp.ageattr.helper;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class SampleWeka {
	public void addFeatures(){
		 FastVector      atts;
	     Instances       data;
	     double[]        vals;
	     
	     // 1. set up attributes
	     atts = new FastVector();
	     // - numeric
	     atts.addElement(new Attribute("att1"));
	     atts.addElement(new Attribute("att2"));
	     atts.addElement(new Attribute("att3"));
	     
	     
	     // 2. create Instances object
	     
	     data = new Instances("MyRelation", atts, 0);
	     // 3. fill with data
	     // first instance
	     vals = new double[data.numAttributes()];
	     // - numeric
	     vals[0] = Math.PI;
	     vals[1] = 1;
	     vals[2] = 2;
	     // add
	     data.add(new Instance(1.0, vals));
	 
	     // second instance
	     vals = new double[data.numAttributes()];  
	     // - numeric
	     vals[0] = Math.E;
	     vals[1] = 3;
	     vals[2] = 4;
	     
	     // add
	     data.add(new Instance(1.0, vals));
	 
	     // 4. output data
	     System.out.println(data);
	}

}
