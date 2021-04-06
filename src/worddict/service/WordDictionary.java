package worddict.service;

import java.util.ArrayList;

import worddict.commons.DictionaryException;

public interface WordDictionary {
	//set
	void addNewWord(String word, String type, String meaning) throws Exception;
	void Commit();//save as preferences
	void removeWord(String word);//remove from hash map
	//get
	String getLocale();
	String getWordMeaning(String word)throws DictionaryException;
	ArrayList<String> getAllWords() throws DictionaryException;
	Boolean hasWord(String word)throws DictionaryException;
}
