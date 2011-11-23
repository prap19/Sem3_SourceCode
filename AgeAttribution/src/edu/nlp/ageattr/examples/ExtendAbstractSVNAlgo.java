package edu.nlp.ageattr.examples;

import edu.nlp.ageattr.AbstractSVNAlgo;

public class ExtendAbstractSVNAlgo extends AbstractSVNAlgo {

	@Override
	protected void addSVNInterfaces() {
		// TODO Auto-generated method stub
		this.arraylist.add(new SVNInterfaceExample());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		(new ExtendAbstractSVNAlgo()).run();
	}

}
