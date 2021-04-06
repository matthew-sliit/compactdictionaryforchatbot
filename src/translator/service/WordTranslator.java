package translator.service;

import java.util.ArrayList;
import java.util.HashMap;

import worddict.commons.DictionaryException;

public interface WordTranslator {
	//set
	void addNewMap(String fromWord, String toWord)throws DictionaryException;
	//get
	ArrayList<String> getAllUnMappedWords();
	HashMap<String, String> getAllWords();//all mapped
	String getTranslatedWord(String fromWord);
}
