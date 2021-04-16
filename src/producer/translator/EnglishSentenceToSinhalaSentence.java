package producer.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import producer.translator.common.TranslatorException;
import producer.translator.service.SentenceTranslator;
import producer.worddict.EnglishDictionary;
import producer.worddict.GenericDictionary;
import producer.worddict.SinhalaDictionary;
import producer.worddict.commons.DictionaryException;
import producer.worddict.commons.WordData;
import producer.worddict.service.WordDictionary;

public class EnglishSentenceToSinhalaSentence implements SentenceTranslator {

	    //HasMap<Spanish word,French word> unoyuno
		//HashMap<String, String> unoyuno = new HashMap<String, String>();
		WordDictionary english = null;
		WordDictionary sinhala = null;
		public static final String PREFERENCES_KEY = "EN2SNSentences";
		ConcurrentHashMap<String, String> sentences;
		Preferences preferences = Preferences.userNodeForPackage(EnglishSentenceToSinhalaSentence.class);
		//gson
		Gson gson = new Gson();
		public EnglishSentenceToSinhalaSentence() {
			// TODO Auto-generated constructor stub
		   sentences=new ConcurrentHashMap<String, String>();
		   //get all from preferences
		   String savedWords = preferences.get(PREFERENCES_KEY , null);
			if(savedWords!=null) {
				java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, String>>(){}.getType();
				sentences = gson.fromJson(savedWords, type);
			}		
		}
		@Override
		public void Commit() {
			//unsafe, if preferences.put doesn't run by a failure; all words could be lost
			preferences.remove(PREFERENCES_KEY);
			//save to preferences
			String jsonWords = gson.toJson(sentences);//convert object to json string
			preferences.put(PREFERENCES_KEY, jsonWords);//store in preferences
		}
		@Override
		public void selfUpdate() {
			english = new EnglishDictionary();
			sinhala = new SinhalaDictionary();
			 String savedWords = preferences.get(PREFERENCES_KEY , null);
				if(savedWords!=null) {
					java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, String>>(){}.getType();
					sentences = gson.fromJson(savedWords, type);
				}		
		}
		//english to sinhala
		@Override
		public void addNewSentence(String[] keys,String[] arr) throws TranslatorException {
			selfUpdate();
            String key="";
            String value="";
			for(String a:arr) {
			  value+=a+" ";
           	  if(!sinhala.hasWord(a)) {
           		  throw new TranslatorException(sinhala.getSimpleName()+" does not have the word {"+a+"}");
           	  }
            }
			value = (String)value.subSequence(0, value.length()-1);
			for(String a:keys) {
          	  key+=a+" ";
          	  if(!english.hasWord(a)) {
      		      throw new TranslatorException(english.getSimpleName()+" does not have the word {"+a+"}");
      	      } 
            }
			key = (String)key.subSequence(0, key.length()-1);
			if(sentences.contains(key)) {
				throw new TranslatorException("Sentence already translated!");
			}
			System.out.println(key+">"+value);
            sentences.put(key,value); 
            Commit();
		}

		@Override
		public void removeall() {
			preferences.remove(PREFERENCES_KEY);
			try {
				preferences.removeNode();
				preferences = Preferences.userNodeForPackage(EnglishSentenceToSinhalaSentence.class);
			} catch (BackingStoreException ignored) {}
			selfUpdate();
		}

		@Override
		public void addNewSentence(String string, String string2) throws TranslatorException{
			if(sentences.containsKey(string)) {
				throw new TranslatorException("Sentence already translated!");
			}
			addNewSentence(string.split("\\s+"), string2.split("\\s+"));
		}

		@Override
		public String getENTranslation(String sn_sentence) {
			selfUpdate();
			if(sentences.containsValue(sn_sentence)) {
				for(Map.Entry<String, String> e : sentences.entrySet()) {
					if(e.getValue().equals(sn_sentence)) {
						return e.getKey();
					}
				}
			}
			return null;
		}
		@Override
		public String getSNTranslation(String en_sentence) {
			selfUpdate();
			if(sentences.containsKey(en_sentence)) {
				return this.sentences.get(en_sentence);
			}
			return null;
		}

		@Override
		public ConcurrentHashMap<String, String> getAllSentences() {
			selfUpdate();
			return this.sentences;
		}
		
}

