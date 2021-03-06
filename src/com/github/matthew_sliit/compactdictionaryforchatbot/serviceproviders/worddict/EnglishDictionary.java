package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.WordData;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EnglishDictionary implements WordDictionary {
	ConcurrentHashMap<String, WordData> words;
	public static final String DictionaryType = "EN";
	//preferences
	Preferences preferences = Preferences.userNodeForPackage(EnglishDictionary.class);
	//gson
	Gson gson = new Gson();
	
	public EnglishDictionary() {
	
		words = new ConcurrentHashMap<String, WordData>();
		
		//get all from preferences
		String savedWords = preferences.get(DictionaryType, null);
		if(savedWords!=null) {
			java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, WordData>>(){}.getType();
			words = gson.fromJson(savedWords, type);
		}
	}
	
	@Override
	public void addNewWord(String word, String type, String meaning) throws DictionaryException {
		//selfUpdate();
		if(word.isBlank()) {
			throw new DictionaryException("Word is just a blank!");
		}
		if(word.matches("[0-9]+")) {
			throw new DictionaryException("Word cannot have numbers!");
		}
		if(words.containsKey(word)) {
			throw new DictionaryException("Word already added!");
		}
		words.put(word, new WordData(word, type, meaning, new ArrayList<String>()));
	}
	

	@Override
	public void addSynonym(String word, String synonym) throws DictionaryException{
		//selfUpdate();
		Boolean hw = hasWord(word);
	
		if(hw == false) System.out.println("Word doesnt exist for the synonym"); 
		else{
			words.get(word).addSyn(synonym);
		}
	}
	


	@Override
	public void Commit() {
		//unsafe, if preferences.put doesn't run by a failure; all words will be lost
		preferences.remove(DictionaryType);
		//save to preferences
		String jsonWords = gson.toJson(words);//convert object to json string
	    preferences.put(DictionaryType, jsonWords);//store in preferences

	}

	@Override
	public void removeWord(String word) {
		words.remove(word);
	}

	@Override
	public String getLocale() {
		return DictionaryType;
	}

	@Override
	public String getWordMeaning(String word) throws DictionaryException {
		if(words.isEmpty()) {
			throw new DictionaryException("Dictionary has no words!");
		}
		return word +" - "+words.get(word).toString();
		
	}

	@Override
	public ArrayList<String> getAllWords() throws DictionaryException {
		TreeMap<String, WordData> sorted = new TreeMap<>();
		sorted.putAll(words);
       
		if(words.isEmpty()) {
			throw new DictionaryException("Dictionary has no words!");
		}
		ArrayList<String> allwords = new ArrayList<String>();
		for(Map.Entry<String, WordData> entry : sorted.entrySet()) {
			allwords.add(entry.getKey());
		}
		return allwords;
	}

	@Override
	public Boolean hasWord(String word) {
		if(words.isEmpty()) {
			return false;
		}
		else if(words.containsKey(word)) {
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<String> getSynonyms(String word) throws DictionaryException {
		if(!hasWord(word)) {
			throw new DictionaryException("Dictionary does not have the word! "+word);
		}
		return this.words.get(word).getSynSet();
	}

	@Override
	public String getSimpleName() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}

	@Override
	public String getWordType(String word) throws DictionaryException {
		if(!words.containsKey(word)) {
			throw new DictionaryException("Dictionary does not have the word: " + word);
		}
		return this.words.get(word).type;
	}

	@Override
	public void selfUpdate() {
		words = new ConcurrentHashMap<String, WordData>();
		//get all from preferences
		String savedWords = preferences.get(DictionaryType, null);
		if(savedWords!=null) {
			java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, WordData>>(){}.getType();
			words = gson.fromJson(savedWords, type);
		}		
	}

}
