package translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

import com.google.gson.Gson;

import translator.common.TranslatorException;
import translator.service.SentenceTranslator;
import worddict.GenericDictionary;
import worddict.commons.DictionaryException;
import worddict.commons.WordData;
import worddict.service.WordDictionary;

public class EnglishSentenceToSinhalaSentence implements SentenceTranslator {

	//HasMap<Spanish word,French word> unoyuno
		HashMap<String, String> unoyuno = new HashMap<String, String>();
		WordDictionary english = null;
		WordDictionary sinhala = null;
		public static final String DictionaryType = "ES";
		ConcurrentHashMap<String, String> sentences;


		
		Preferences preferences = Preferences.userNodeForPackage(GenericDictionary.class);
		//gson
		Gson gson = new Gson();
		
		public EnglishSentenceToSinhalaSentence() {
			// TODO Auto-generated constructor stub
		   sentences=new ConcurrentHashMap<String, String>();
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
				//System.out.println("word not in dictionary");
			}else {
				unoyuno.put(fromWord, toWord);
				//fromWord.System.out.println("mapped");
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
		public void Commit() {
			//unsafe, if preferences.put doesn't run by a failure; all words could be lost
			preferences.remove(DictionaryType);
			//save to preferences
			String jsonWords = gson.toJson(sentences);//convert object to json string
			preferences.put(DictionaryType, jsonWords);//store in preferences
		}

		@Override
		public HashMap<String, String> getAllWords() {
			selfUpdate();
			return unoyuno;
		}
		
		private void selfUpdate() {
			english = new GenericDictionary("EN","English");
			sinhala = new GenericDictionary("SN","Sinhala");
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
		public void addNewSentence(String[] keys,String[] arr) {
            String key="";
            String value="";
			for(String a:arr) {
            	  value+=a+"";
            	  
              }
			
			for(String a:keys) {
          	  key+=a+"";
          	  
            }
			System.out.println(key+">"+value);
             sentences.put(key,value); 
             
		}

		@Override
		public String getSentences(String sentence) {
			// TODO Auto-generated method stub
			return this.sentences.get(sentence);
		}
		
}

