package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.common.TranslatorException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;

public interface WordTranslator {
	//set
	void addNewMap(String fromWord, String toWord)throws DictionaryException, TranslatorException;
	//get
	ArrayList<String> getAllUnMappedWords();
	ConcurrentHashMap<String, String> getAllWords();//all mapped
	ArrayList<String> getAllESWords() throws DictionaryException;
	String getTranslatedWord(String fromWord);
	void Commit();
}
