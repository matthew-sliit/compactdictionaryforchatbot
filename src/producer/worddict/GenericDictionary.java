package producer.worddict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import producer.worddict.commons.DictionaryException;
import producer.worddict.commons.WordData;
import producer.worddict.service.WordDictionary;

public class GenericDictionary implements WordDictionary {
	ConcurrentHashMap<String, WordData> words;
	public String DictionaryType = "ES";
	public String dictionaryLang = "xDictionary";
	//preferences
	//Preferences preferences = Preferences.userNodeForPackage(GenericDictionary.class);
	//gson
	//Gson gson = new Gson();
	public GenericDictionary() {
		// TODO Auto-generated constructor stub
	}
	public GenericDictionary(String locale, String language) {
		DictionaryType = locale;
		dictionaryLang = language+"Dictionary";
		
		words = new ConcurrentHashMap<String, WordData>();
		/*
		//get all from preferences
		String savedWords = preferences.get(DictionaryType, null);
		if(savedWords!=null) {
			java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, WordData>>(){}.getType();
			words = gson.fromJson(savedWords, type);
		}*/
	}
	
	@Override
	public void addNewWord(String word, String type, String meaning) throws DictionaryException{
		if(word.isBlank()) {
			throw new DictionaryException("Word is just a blank!");
		}
		if(word.matches("[0-9]+")) {
			throw new DictionaryException("Word cannot have numbers!");
		}
		if(words.containsKey(word)) {
			throw new DictionaryException("Word{"+word+"} already added!");
		}
		words.put(word, new WordData(word, type, meaning, new ArrayList<String>()));//hash map
	}

	@Override
	public String getWordMeaning(String word) throws DictionaryException {
		// word.type.meaning
		if(words.isEmpty()) {
			throw new DictionaryException("Dictionary has no words!");
		}
		return word +"."+words.get(word).toString();
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
	public String getLocale() {
		return DictionaryType;
	}

	@Override
	public void Commit() {
		/*
		//unsafe, if preferences.put doesn't run by a failure; all words could be lost
		preferences.remove(DictionaryType);
		//save to preferences
		String jsonWords = gson.toJson(words);//convert object to json string
		preferences.put(DictionaryType, jsonWords);//store in preferences
		*/
	}

	@Override
	public void removeWord(String word) {
		words.remove(word);
	}

	@Override
	public String getSimpleName() {
		// TODO Auto-generated method stub
		return this.dictionaryLang;
	}

	@Override
	public String getWordType(String word) throws DictionaryException{
		if(!words.containsKey(word)) {
			throw new DictionaryException("Dictionary does not have the word: " + word);
		}
		return this.words.get(word).type;
	}

	@Override
	public void addSynonym(String word, String synonym) throws DictionaryException {
		// TODO Auto-generated method stub
	}

	@Override
	public ArrayList<String> getSynonyms(String word) throws DictionaryException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void selfUpdate() {
		words = new ConcurrentHashMap<String, WordData>();
		/*
		//get all from preferences
		String savedWords = preferences.get(DictionaryType, null);
		if(savedWords!=null) {
			java.lang.reflect.Type type = new TypeToken<HashMap<String, WordData>>(){}.getType();
			words = gson.fromJson(savedWords, type);
		}	*/			
	}
}
