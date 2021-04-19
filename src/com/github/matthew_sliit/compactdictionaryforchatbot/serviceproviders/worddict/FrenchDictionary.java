package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.WordData;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FrenchDictionary implements WordDictionary {
	ConcurrentHashMap<String, WordData> words;
	public static final String DictionaryType = "FR";
	//preferences
	Preferences preferences = Preferences.userNodeForPackage(FrenchDictionary.class);
	//gson
	Gson gson = new Gson();
	
	public FrenchDictionary() {
		words = new ConcurrentHashMap<String, WordData>();
		//get all from preferences
		String savedWords = preferences.get(DictionaryType, null);
		if(savedWords!=null) {
			java.lang.reflect.Type type = new TypeToken<HashMap<String, WordData>>(){}.getType();
			words = gson.fromJson(savedWords, type);
		}
	}
	@Override
	public void addNewWord(String word, String type, String meaning) throws DictionaryException {
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
	public String getWordMeaning(String word) throws DictionaryException {
		if(words.isEmpty()) {
			throw new DictionaryException("Dictionary has no words!");
		}
		return word +"."+words.get(word).toString();
	}

	@Override
	public ArrayList<String> getAllWords() throws DictionaryException {
		if(words.isEmpty()) {
			throw new DictionaryException("Dictionary has no words!");
		}
		ArrayList<String> allwords = new ArrayList<String>();
		for(Map.Entry<String, WordData> entry : words.entrySet()) {
			allwords.add(entry.getKey());
		}
		return allwords;
	}

	@Override
	public Boolean hasWord(String word) {
		// 
		if(words.isEmpty()) {
			return false;
		}
		else if(words.containsKey(word)) {
			return true;
		}
		return false;
	}

	@Override
	public String getLocale() {
		return DictionaryType;
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
	public String getSimpleName() {
		return this.getClass().getSimpleName();
	}
	@Override
	public String getWordType(String word) {
		return this.words.get(word).type;
	}
	@Override
	public ArrayList<String> getSynonyms(String word) throws DictionaryException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void addSynonym(String word, String synonym) throws DictionaryException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void selfUpdate() {
		// TODO Auto-generated method stub
		
	}

}
