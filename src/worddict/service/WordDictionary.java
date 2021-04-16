package worddict.service;

import java.util.ArrayList;

import worddict.commons.DictionaryException;

public interface WordDictionary {
	//set
	void addNewWord(String word, String type, String meaning) throws DictionaryException;
	void addSynonym(String word, String synonym) throws DictionaryException;
	void Commit();//save as preferences
	void removeWord(String word);//remove from hash map
	//get
	String getLocale();
	ArrayList<String> getSynonyms(String word) throws DictionaryException;
	String getSimpleName();//xDictionary, returns this.getClass().getSimpleName();
	String getWordType(String word)throws DictionaryException;//verb, adverb, noun..
	String getWordMeaning(String word)throws DictionaryException;
	ArrayList<String> getAllWords() throws DictionaryException;
	Boolean hasWord(String word)throws DictionaryException;
}
