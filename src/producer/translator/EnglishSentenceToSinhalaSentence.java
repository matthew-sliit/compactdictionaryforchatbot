package producer.translator;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import producer.translator.common.TranslatorException;
import producer.translator.service.SentenceTranslator;
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
		public EnglishSentenceToSinhalaSentence(WordDictionary englishDictionary, WordDictionary sinhalaDictionary) {
		   this.english = englishDictionary; this.sinhala = sinhalaDictionary;
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
			//reset sentences holder
			sentences=new ConcurrentHashMap<String, String>();
			english.selfUpdate();
			sinhala.selfUpdate();
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
            Boolean hasEndingPunctuation = false;
			for(String a:arr) {
				if(!a.equals("?")) {
					value+=a+" ";
				}else {
					hasEndingPunctuation = true;
					value = (String)value.subSequence(0, value.length()-1);
					value+=a;
				}
           	    if(!sinhala.hasWord(a)) {
           		  throw new TranslatorException(sinhala.getSimpleName()+" does not have the word {"+a+"}");
           	    }
            }
			if(!hasEndingPunctuation) {
				value = (String)value.subSequence(0, value.length()-1);
			}
			for(String a:keys) {
				if(!a.equals("?")) {
					key+=a+" ";
				}else {
					key = (String)key.subSequence(0, key.length()-1);
					key+=a;
				}
				if(!english.hasWord(a)) {
	      		      throw new TranslatorException(english.getSimpleName()+" does not have the word {"+a+"}");
	      	    } 
            }
			if(!hasEndingPunctuation) {
				key = (String)key.subSequence(0, key.length()-1);
			}
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
			String[] sentence1 = null,sentence2 = null, s = null; int size =0;
			size = string.split("\\s+").length;
			s = string.split("\\s+");
			if(string.endsWith("?")) {
				sentence1 = new String[size+1];//extra 1 for symbol
				for(int i=0;i<size;i++) {
					sentence1[i] = s[i];
				}
				sentence1[size] = (String) sentence1[size-1].subSequence(sentence1[size-1].length()-1, sentence1[size-1].length());
				//System.out.println("sentence[size]="+sentence[size]);
				sentence1[size-1] = (String) sentence1[size-1].subSequence(0, sentence1[size-1].length()-1);
			}else {
				sentence1 = string.split("\\s+");
			}
			size = string.split("\\s+").length;
			s = string2.split("\\s+");
			if(string2.endsWith("?")) {
				size = string2.split("\\s+").length;
				sentence2 = new String[size+1];//extra 1 for symbol
				for(int i=0;i<size;i++) {
					sentence2[i] = s[i];
				}
				sentence2[size] = (String) sentence2[size-1].subSequence(sentence2[size-1].length()-1, sentence2[size-1].length());
				//System.out.println("sentence[size]="+sentence[size]);
				sentence2[size-1] = (String) sentence2[size-1].subSequence(0, sentence2[size-1].length()-1);
			}else {
				sentence2 = string2.split("\\s+");
			}
			addNewSentence(sentence1, sentence2);
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

