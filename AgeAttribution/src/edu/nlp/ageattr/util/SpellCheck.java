package edu.nlp.ageattr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

public class SpellCheck implements SpellCheckListener{

	 private static SpellDictionary dictionary;

     private static SpellChecker spellCheck;
     private List suggestions = new ArrayList();
     private static SpellCheck spCheck;
     
	public SpellCheck() {
		// TODO Auto-generated constructor stub
		 try {
			dictionary= new SpellDictionaryHashMap(new File("rsrc\\english.0"));
			spellCheck = new SpellChecker(dictionary);
			spellCheck.addSpellCheckListener(spCheck);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Boolean isWrongWord(String word) {
		
		if(spCheck == null)
			spCheck = new SpellCheck();
		if(spCheck.suggestions == null)
			System.out.println("spell check is null");
		try {
			spCheck.spellCheck.checkSpelling(new StringWordTokenizer(word));
		}catch(Exception e) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public void spellingError(SpellCheckEvent event) {
		this.suggestions = event.getSuggestions();
		System.out.println("spelling error");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(SpellCheck.isWrongWord("computer"));
	}

}
