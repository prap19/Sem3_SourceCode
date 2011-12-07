package edu.nlp.ageattr.examples;

import edu.nlp.ageattr.AbstractSVNAlgo;
import edu.nlp.ageattr.interfaces.AcronymFeature;
import edu.nlp.ageattr.interfaces.EmoticonFeature;
import edu.nlp.ageattr.interfaces.LingosFeature;
import edu.nlp.ageattr.interfaces.PunctuationFeature;

public class ExtendAbstractSVNAlgo extends AbstractSVNAlgo {

	@Override
	protected void addSVNInterfaces() {
		// TODO Auto-generated method stub
		this.arraylist.add(new BagOfWords());
		this.arraylist.add(new AcronymFeature());
		this.arraylist.add(new LingosFeature());
		this.arraylist.add(new PunctuationFeature());
		//this.arraylist.add(new EmoticonFeature());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		(new ExtendAbstractSVNAlgo()).run();
	}

}
