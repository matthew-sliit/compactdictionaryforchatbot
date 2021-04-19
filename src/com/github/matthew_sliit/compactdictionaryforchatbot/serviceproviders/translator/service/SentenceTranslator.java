package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.common.TranslatorException;

public interface SentenceTranslator {
	//add new
	void addNewSentence(String string, String string2)throws TranslatorException;
	void addNewSentence(String[] keys, String[] arr)throws TranslatorException;
	void Commit();
	void selfUpdate();
	//remove all translations
	void removeall();
	//get
	//ArrayList<String> getAllUnMappedWords();
	String getSNTranslation(String en_sentence);
	String getENTranslation(String sn_sentence);
	//String getTranslatedWord(String fromWord);
	ConcurrentHashMap<String, String> getAllSentences();
}
