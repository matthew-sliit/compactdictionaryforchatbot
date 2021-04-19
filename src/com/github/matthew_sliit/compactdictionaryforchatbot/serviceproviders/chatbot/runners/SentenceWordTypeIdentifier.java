package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.runners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.EnglishSentenceStructure;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class SentenceWordTypeIdentifier extends EnglishSentenceStructure implements Runnable{
	public HashMap<String, String> wordsWithType;//word,Type
	public ArrayList<ArrayList<String>> wordsForType = null;//Type,[word,word..]
	private ArrayList<String> words;
	private WordDictionary dictionary;
	public SentenceWordTypeIdentifier(ArrayList<String> words,WordDictionary dict) {
		this.dictionary = dict;
		this.words = words;
		this.wordsWithType = new LinkedHashMap<String, String>();
		this.wordsForType = new ArrayList<ArrayList<String>>();
	}
	@Override
	public void run(){
		for(int i=0;i<Type.size;i++) {
			wordsForType.add(i, new ArrayList<String>());
		}
		for(String word : words) {
			try {
				if(dictionary.hasWord(word)) {
					String giventype = dictionary.getWordType(word);
					wordsWithType.put(word, giventype);
					for(int i=0;i<wordtypes.length;i++) {
						if(giventype.equals(wordtypes[i])) {
							wordsForType.get(i).add(word);
						}
					}
				}else {
					//not an existing word? unknown type?
					wordsWithType.put(word, Type.UNKNOWN.string);
					wordsForType.get(TypeOrder.UNKNOWN.index).add(word);
				}
			} catch (DictionaryException ignored) {
				//undefined dict?
			}
		}
	}
}
