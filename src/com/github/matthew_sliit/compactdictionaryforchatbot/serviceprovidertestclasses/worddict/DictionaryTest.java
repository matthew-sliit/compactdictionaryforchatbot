package com.github.matthew_sliit.compactdictionaryforchatbot.serviceprovidertestclasses.worddict;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.EnglishDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.FrenchDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.GenericDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.SinhalaDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.SpanishDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class DictionaryTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordDictionary wordDictionary = null;
		Scanner input = new Scanner(System.in);
		String value = "", dictionaryLang = "EX"; Boolean isGeneric = false;
		System.out.println("Dictionary Service Started!");
		System.out.println("=================================================");
		System.out.println("Enter Language: (FR|ES|EN|SN) or langShort.language");
		value = input.nextLine();
		if(value.equalsIgnoreCase("FR")) {
			wordDictionary = new FrenchDictionary();
			dictionaryLang = "FR";
		}else if(value.equalsIgnoreCase("ES")) {
			wordDictionary = new SpanishDictionary();
			dictionaryLang = "ES";
		}else if(value.equalsIgnoreCase("EN")) {
			wordDictionary = new EnglishDictionary();
			dictionaryLang = "EN";
		}else if(value.equalsIgnoreCase("SN")) {
			wordDictionary = new SinhalaDictionary();
			dictionaryLang = "SN";
		}else if(value.contains(".")){
			String[] lang = value.split("\\.");
			wordDictionary = new GenericDictionary(lang[0],lang[1]);
			dictionaryLang = lang[0]+"."+lang[1];
			isGeneric = true;
			//input.close();
			//return;
		}else {
			System.out.println("Dictionary Service Stopped!");
			input.close();
			return;
		}
		System.out.println("Selected: "+wordDictionary.getClass().getSimpleName());
		try {
			while(true){
				System.out.println("================================================================");
				System.out.println("[addnew word.type.meaning | get word | has word | commit | help]");
				System.out.println("================================================================");
				value = input.nextLine();
				try {
					//addnew මව.න�?ම පදයක්.පරීක්ෂණයට පමණි
					if(value.startsWith("addnew ")) {
						//addNew espana.nombre propio.también conocido como españa
						String[] splitLine = value.split("\\.");//split where dot exists
						splitLine[0] = (String) splitLine[0].subSequence(7, splitLine[0].length());//remove addNew keyword
						if(wordDictionary.hasWord(splitLine[0])) {
							System.out.println("Word Already Exists!");
						}else {
							System.out.println("Adding new word!");
							showOutput(splitLine);
							wordDictionary.addNewWord(splitLine[0], splitLine[1], splitLine[2]);
						}
					}else if(value.startsWith("get ")) {
						//get word
						String word = (String) value.subSequence(4, value.length());
						word = word.replaceAll("\\s+","");//remove whitespace
						System.out.println("Result:" + wordDictionary.getWordMeaning(word));
					}else if(value.startsWith("remove ")) {
						//get word
						String word = (String) value.subSequence(7, value.length());
						if(wordDictionary.hasWord(word)) {
							wordDictionary.removeWord(word);
						}
						System.out.println("Word {"+word+"} not in "+wordDictionary.getSimpleName());
					}
					else if(value.startsWith("has ")){
						String word = (String) value.subSequence(4, value.length());
						word = word.replaceAll("\\s+","");//remove whitespace
						System.out.println("Result:" + wordDictionary.hasWord(word));
					}else if(value.startsWith("getall")||value.startsWith("showall")){
						for(String word : wordDictionary.getAllWords()) {
							System.out.println(word);
						}
					}else if(value.contains("commit")) {
						wordDictionary.Commit();//save as preferences
						if(dictionaryLang.equals("EN")) {
							wordDictionary = new EnglishDictionary();
						}else if(dictionaryLang.equals("SN")) {
							wordDictionary = new SinhalaDictionary();//reset
						}else if(isGeneric) {
							String[] lang = dictionaryLang.split("\\.");
							wordDictionary = new GenericDictionary(lang[0],lang[1]);
						}
					}else {
						System.out.println("Dictionary Service Stopped!");
						break;
					}
				}catch (DictionaryException | IndexOutOfBoundsException e) {
					System.out.println("Exception caught:"+wordDictionary.getSimpleName()+" ex="+e.getMessage());
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception caught:"+wordDictionary.getSimpleName()+" ex="+e.getMessage());
		}

			
		input.close();
	}
	private static void showOutput(String[] data) {
		System.out.println("Word:"+data[0]);
		System.out.println("Type:"+data[1]);
		System.out.println("Meaning:"+data[2]);
	}
	
}
