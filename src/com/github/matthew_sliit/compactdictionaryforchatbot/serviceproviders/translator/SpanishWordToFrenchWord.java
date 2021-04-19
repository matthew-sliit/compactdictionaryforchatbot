package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.WordTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.FrenchDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.SpanishDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class SpanishWordToFrenchWord implements WordTranslator {
	//HasMap<Spanish word,French word> unoyuno
	ConcurrentHashMap<String, String> unoyuno = new ConcurrentHashMap<String, String>();
	WordDictionary espanola = null;
	WordDictionary francis = null;
	
	public SpanishWordToFrenchWord() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addNewMap(String fromWord, String toWord) throws DictionaryException {
		selfUpdate();
		if(unoyuno.isEmpty()) {
			//error
		}
		if(unoyuno.get(toWord)!="+unmapped"){
			//already mapped, edit?
		}
		if(!francis.hasWord(toWord)) {
			//prevent map to undefined word, toWord by default is +unmapped
		}else {
			unoyuno.put(fromWord, toWord);
		}
	}

	@Override
	public ArrayList<String> getAllUnMappedWords() {
		selfUpdate();//check for new words beforehand
		ArrayList<String> unmappedWords = new ArrayList<String>();
		for(Map.Entry<String, String> entry : unoyuno.entrySet()) {
			if(entry.getValue().equals("+unmapped")) {
				unmappedWords.add(entry.getKey());
			}
		}
		return unmappedWords;
	}

	@Override
	public ConcurrentHashMap<String, String> getAllWords() {
		selfUpdate();
		return unoyuno;
	}
	
	private void selfUpdate() {
		espanola = new SpanishDictionary();
		francis = new FrenchDictionary();
		try {
			for(String word : espanola.getAllWords()) {
				if(!unoyuno.containsKey(word)) {
					unoyuno.put(word, "+unmapped");//new word
				}else {
					if(!francis.hasWord(unoyuno.get(word))) {
						unoyuno.put(word, "+unmapped");//word removed from French dictionary
					}
				}
			}
		} catch (DictionaryException e) {
			//empty dictionary?
		}
	}

	@Override
	public String getTranslatedWord(String fromWord) {
		return unoyuno.get(fromWord);//returns value for specific key
	}

	

	@Override
	public void Commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<String> getAllESWords() throws DictionaryException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
