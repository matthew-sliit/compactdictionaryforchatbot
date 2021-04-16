package translator.service;

import java.util.ArrayList;
import java.util.HashMap;

public interface SentenceTranslator {

	HashMap<String, String> getAllWords();

	String getTranslatedWord(String fromWord);

	void addNewSentence(String[] keys, String[] arr);

	String getSentences(String sentence);

	ArrayList<String> getAllUnMappedWords();

	void Commit();

	void removeall();

	void addNewSentence(String string, String string2);

	
}
