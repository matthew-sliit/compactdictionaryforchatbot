package com.github.matthew_sliit.compactdictionaryforchatbot.serviceprovidertestclasses.testtranslators;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.EnglishSentenceToSpanishSentence;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.EnglishWordToSpanishWord;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.common.TranslatorException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.SentenceTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.WordTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.GenericDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class TestSentenceTranslatorEN2ES {
	public static void main(String[] args) {
		WordDictionary en = new GenericDictionary("EN","English");
		WordDictionary sn = new GenericDictionary("ES","Spanish");
		SentenceTranslator sentTranslator = new EnglishSentenceToSpanishSentence(en, sn);
		WordTranslator wordtranslator = new EnglishWordToSpanishWord(en, sn);
		Scanner input = new Scanner(System.in);
		String value = "";
		System.out.println("Dictionary Translator Service Started for EN-to-ES");	
		while(true) {
			try {
				System.out.println("=================================================");
				value = input.nextLine();
				if(value.startsWith("addsent ")) {
					//addNewMap FromWord.ToWord
					String newMap = (String) value.subSequence(8, value.length());
					String[] splitWords = newMap.split("\\.");
					//System.out.println(splitWords[0]+" -> "+splitWords[1]);
					sentTranslator.addNewSentence(splitWords[0], splitWords[1]);
					System.out.println("Sentence was successfully translated!");
				}else if(value.startsWith("addword ")) {
					//addNewMap FromWord.ToWord
					String newMap = (String) value.subSequence(8, value.length());
					String[] splitWords = newMap.split("\\.");
					//System.out.println(splitWords[0]+" -> "+splitWords[1]);
					wordtranslator.addNewMap(splitWords[0], splitWords[1]);
					System.out.println("Word was successfully translated!");
				}else if(value.startsWith("tword ")) {
					//word to French
					String translateWord = (String) value.subSequence(6, value.length());
					if(wordtranslator.getTranslatedWord(translateWord).equals("+unmapped")) {
						System.out.println("Word is not yet translated!");
					}else {
						System.out.println(wordtranslator.getTranslatedWord(translateWord));
					}
				}else if(value.startsWith("getunmap") || value.startsWith("showunmap")) {
					for(String word: wordtranslator.getAllUnMappedWords()) {
						System.out.println(word);
					}
				}
				else if(value.equals("getall") || value.equals("showall")) { 
					for(Map.Entry<String, String> entry : sentTranslator.getAllSentences().entrySet()) {
						System.out.println(entry.getKey()+"->"+entry.getValue());
					}
				}else if(value.equals("getallwords") || value.equals("showallwords")) { 
					for(Map.Entry<String, String> entry : wordtranslator.getAllWords().entrySet()) {
						System.out.println(entry.getKey()+"->"+entry.getValue());
					}
				}else if(value.startsWith("en2sn ")) {
					//word to French
					String englishSentence= (String) value.subSequence(6, value.length());
					if(sentTranslator.getSNTranslation(englishSentence)!=null) {
						System.out.println(sentTranslator.getSNTranslation(englishSentence));
					}else {
						System.out.println("Sentence is not yet translated!");
					}
				}else if(value.startsWith("sn2en ")) {
					//word to French
					String sinhalaSentence= (String) value.subSequence(6, value.length());
					if(sentTranslator.getENTranslation(sinhalaSentence)!=null) {
						System.out.println(sentTranslator.getENTranslation(sinhalaSentence));
					}else {
						System.out.println("Sentence is not yet translated!");
					}
				}else if(value.startsWith("commit")) {
					sentTranslator.Commit();
					wordtranslator.Commit();
					//self update handles this
					//stranslator=new EnglishSentenceToSinhalaSentence();
				}else if(value.equals("showen")) {
					for(String word : en.getAllWords()) {
						System.out.println(word);
					}
				}else if(value.equals("showes")) {
					for(String word : sn.getAllWords()) {
						System.out.println(word);
					}
				}else if(value.equals("reset")) {
					sentTranslator.removeall();
				}else {
					System.out.println("You entered a wrong keyword!!\n");
					break;
				}
			}catch (DictionaryException | TranslatorException e) {
				// TODO: handle exception
				System.out.println(e.getLocalizedMessage());
			}
		}
		System.out.println("Dictionary Translator Service Stopped for EN-to-ES");	
	}
}
