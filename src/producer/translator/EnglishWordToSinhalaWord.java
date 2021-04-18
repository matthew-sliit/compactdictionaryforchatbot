package producer.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import producer.translator.common.TranslatorException;
import producer.translator.service.WordTranslator;
import producer.worddict.GenericDictionary;
import producer.worddict.commons.DictionaryException;
import producer.worddict.commons.WordData;
import producer.worddict.service.WordDictionary;

public class EnglishWordToSinhalaWord implements WordTranslator {

	//HasMap<Spanish word,French word> unoyuno
	ConcurrentHashMap<String, String> unoyuno = new ConcurrentHashMap<String, String>();
	WordDictionary english = null;
	WordDictionary sinhala = null;
	public static final String PREFERENCES_KEY = "ENtoSNWord";

	Preferences preferences = Preferences.userNodeForPackage(EnglishWordToSinhalaWord.class);
	//gson
	Gson gson = new Gson();
	public EnglishWordToSinhalaWord(WordDictionary englishDictionary, WordDictionary sinhalaDictionary) {
		this.english = englishDictionary; this.sinhala = sinhalaDictionary;
		unoyuno = new ConcurrentHashMap<String, String>();
		//get all from preferences
		String savedWords = preferences.get(PREFERENCES_KEY , null);
		if(savedWords!=null) {
			java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, String>>(){}.getType();
			unoyuno = gson.fromJson(savedWords, type);
		}	
	  }
	
	@Override
	public void addNewMap(String fromWord, String toWord) throws DictionaryException,TranslatorException {
		selfUpdate();
		
		//unoyuno.put(fromWord, toWord);
		if(unoyuno.isEmpty()) {
			//error
			//System.out.println("isEmpty");
		}
		if(unoyuno.get(fromWord)!="+unmapped" && unoyuno.get(fromWord)!=null){
			//already mapped, edit?
			//System.out.println("already mapped:"+unoyuno.get(fromWord));
			throw new TranslatorException("Exception Word already mapped as "+unoyuno.get(fromWord));
		}
		if(!sinhala.hasWord(toWord)) {
			//prevent map to undefined word, toWord by default is +unmapped
			System.out.println("word not in dictionary");
		}else {
			unoyuno.put(fromWord, toWord);
			//fromWord.System.out.println("mapped");
		}
		Commit();
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
	public void Commit() {
		//unsafe, if preferences.put doesn't run by a failure; all words could be lost
		preferences.remove(PREFERENCES_KEY );
		//save to preferences
		String jsonWords = gson.toJson(unoyuno);//convert object to json string
		preferences.put(PREFERENCES_KEY , jsonWords);//store in preferences
	}

	@Override
	public ConcurrentHashMap<String, String> getAllWords() {
		selfUpdate();
		return unoyuno;
	}
	
	private void selfUpdate() {
		//english = new GenericDictionary("EN","English");
		//sinhala = new GenericDictionary("SN","Sinhala");
		//reset this class
		unoyuno = new ConcurrentHashMap<String, String>();
		//get all from preferences
		String savedWords = preferences.get(PREFERENCES_KEY , null);
		if(savedWords!=null) {
			java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, String>>(){}.getType();
			unoyuno = gson.fromJson(savedWords, type);
		}			
		english.selfUpdate();
		sinhala.selfUpdate();
		try {
			for(String word : english.getAllWords()) {
				if(!unoyuno.containsKey(word)) {
					unoyuno.put(word, "+unmapped");//new word
				}else {
					if(!sinhala.hasWord(unoyuno.get(word))) {
						unoyuno.put(word, "+unmapped");//word removed from French dictionary
					}
				}
			}
		} catch (DictionaryException e) {
			//empty dictionary?
			e.printStackTrace();
		}
	}

	@Override
	public String getTranslatedWord(String fromWord) {
		return unoyuno.get(fromWord);//returns value for specific key
	}

	@Override
	public ArrayList<String> getAllESWords() throws DictionaryException {
		// TODO Auto-generated method stub
		return this.sinhala.getAllWords();
	}
}