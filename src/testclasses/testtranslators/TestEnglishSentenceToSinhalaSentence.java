package testclasses.testtranslators;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import producer.translator.EnglishSentenceToSinhalaSentence;
import producer.translator.EnglishWordToSinhalaWord;
import producer.translator.common.TranslatorException;
import producer.translator.service.SentenceTranslator;
import producer.translator.service.WordTranslator;
import producer.worddict.GenericDictionary;
import producer.worddict.commons.DictionaryException;
import producer.worddict.service.WordDictionary;

public class TestEnglishSentenceToSinhalaSentence {

	public static void main(String[] args) {
		WordDictionary en = new GenericDictionary("EN","English");
		WordDictionary sn = new GenericDictionary("SN","Sinhala");
		WordTranslator translator = new EnglishWordToSinhalaWord(en,sn);
		SentenceTranslator stranslator=new EnglishSentenceToSinhalaSentence(en,sn); 
		Scanner input = new Scanner(System.in);
		String value = "";
		System.out.println("Dictionary Translator Service Started for EN-to-SN");	
		String[] ignor= {"are","is"};
		try {
			/*
			//English Dictionary
			en.addNewWord("how", "Adverb", "condition");
			en.addNewWord("are", "Verb", "test");
			en.addNewWord("you", "Pronoun", "test");
			en.addNewWord("where", "Adverb","condition");
			en.Commit();
			System.out.println("Test values added for English Dictionary");
			//Sinhala Dictionary
			sn.addNewWord("à¶šà·™à·ƒà·šà¶¯", "Adverb", "se renseigner sur l'Ã©tat");
			sn.addNewWord("à·€à·š", "Verb", "DeuxiÃ¨me personne du singulier prÃ©sent");
			sn.addNewWord("à¶”à¶¶à¶§", "Pronoun", "test");
			sn.addNewWord("à¶šà·œà·„à·™à¶¯", "Pronoun", "test");
			sn.Commit();
			System.out.println("Test values added for Sinhala Dictionary");
			*/
			stranslator.removeall();
			String[][] englishWords = {
					{"how","Question","condition"},
					{"are","Verb","Test"},
					{"you","Pronoun","Test"},
					{"where","Question","condition"},
					{"what","Question","condition"},
					{"doing","Verb","Test"}
			};
			String[][] sinhalaWords = {
					{"කෙසේද","Question","condition"},
					{"are","Verb","Test"},
					{"ඔබට","Pronoun","Test"},//you
					{"ඔයා","Pronoun","Test"},//you
					{"කොහෙද","Question","condition"},//where
					{"කරන්නේ","Verb","Test"},
					{"කුමක්","Verb","Test"},
					{"ද","Punctuation","Test"}
			};
			addTestValuesToDictionary(en,sn,englishWords,sinhalaWords);
            

			while(true) {
				System.out.println("=================================================");
				value = input.nextLine();
				try {
					//addnew how are you.ඔබට කෙසේද
					//addnew where are you.ඔයා කොහෙද
					//addnew what are you doing.ඔයා කරන්නේ කුමක් ද
					if(value.startsWith("addnew ")) {
						//addNewMap FromWord.ToWord
						String newMap = (String) value.subSequence(7, value.length());
						String[] splitWords = newMap.split("\\.");
						//System.out.println(splitWords[0]+" -> "+splitWords[1]);
						stranslator.addNewSentence(splitWords[0], splitWords[1]);		
					}else if(value.startsWith("getallunmapped")) {
						for(String word: translator.getAllUnMappedWords()) {
							System.out.println(word);
						}
					}else if(value.equals("getall") || value.equals("showall")) { 
						for(Map.Entry<String, String> entry :stranslator.getAllSentences().entrySet()) {
							System.out.println(entry.getKey()+"->"+entry.getValue());
						}
					}else if(value.startsWith("en2sn ")) {
						//word to French
						String englishSentence= (String) value.subSequence(6, value.length());
						if(stranslator.getSNTranslation(englishSentence)!=null) {
							System.out.println(stranslator.getSNTranslation(englishSentence));
						}else {
							System.out.println("Sentence is not yet translated!");
						}
					}else if(value.startsWith("sn2en ")) {
						//word to French
						String sinhalaSentence= (String) value.subSequence(6, value.length());
						if(stranslator.getENTranslation(sinhalaSentence)!=null) {
							System.out.println(stranslator.getENTranslation(sinhalaSentence));
						}else {
							System.out.println("Sentence is not yet translated!");
						}
					}else if(value.startsWith("tword ")) {
						String translateWords = (String) value.subSequence(6, value.length());
	                    String sent=" ";
	                    Boolean accept=true;
	                    StringTokenizer st=new StringTokenizer(translateWords," ");
	                    while (st.hasMoreTokens()) {
	                       accept=true;
	                       for (String s:ignor) {
							 if(st.nextToken().equals(s)) {
								accept=false;
							 }
						   }
	                     if(accept) {
	                    	 sent+=translator.getTranslatedWord(st.nextToken())+" ";
	                     }
						}
	                    System.out.println("result = "+sent);
					}else if(value.equals("show2words")) {
						for(String word : sn.getAllWords()) {
							System.out.println(word);
						}
					}else if(value.startsWith("commit")) {
						stranslator.Commit();
						//self update handles this
						//stranslator=new EnglishSentenceToSinhalaSentence();
					}
					else {
						break;
					}
				}catch (TranslatorException e) {
					System.out.println("ex:"+e.getLocalizedMessage());
					//e.printStackTrace();
				}
			}
			removeValuesFromDictionary(en, sn, englishWords, sinhalaWords);
		}catch (DictionaryException e) {
			// TODO: handle exception
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		/*
		//Spanish Dictionary
		en.removeWord("where");
		en.removeWord("how");
		en.removeWord("are");
		en.removeWord("you");
		en.Commit();
		//French Dictionary
		sn.removeWord("à¶šà·œà·„à·™à¶¯");
		sn.removeWord("à¶šà·™à·ƒà·šà¶¯");
		sn.removeWord("à·€à·š");
		sn.removeWord("à¶”à¶¶à¶§");
		sn.Commit();
		//System.out.println("Dictionary Test values removed");
		 */
		System.out.println("Dictionary Translator Service Stopped for EN-to-SN");	
		input.close();
	}
	private static void addTestValuesToDictionary(WordDictionary en, WordDictionary sn, String[][] engWords, String[][] sinWords)throws DictionaryException{
		for(int i=0;i<engWords.length;i++) {
			if(!en.hasWord(engWords[i][0])) {
				System.out.println(engWords[i][0]+" "+ engWords[i][1]+" "+ engWords[i][2]);
				en.addNewWord(engWords[i][0], engWords[i][1], engWords[i][2]);
			}
		}
		en.Commit();
		System.out.println("Test values added for English Dictionary");
		for(int i=0;i<sinWords.length;i++) {
			if(!sn.hasWord(sinWords[i][0])) {
				sn.addNewWord(sinWords[i][0], sinWords[i][1], sinWords[i][2]);
			}
		}
		sn.Commit();
		System.out.println("Test values added for Sinhala Dictionary");
	}
	private static void removeValuesFromDictionary(WordDictionary en, WordDictionary sn, String[][] engWords, String[][] sinWords)throws DictionaryException{
		for(int i=0;i<engWords.length;i++) {
			if(!en.hasWord(engWords[i][0])) {
				en.removeWord(engWords[i][0]);
			}
		}
		en.Commit();
		for(int i=0;i<sinWords.length;i++) {
			if(!sn.hasWord(sinWords[i][0])) {
				sn.removeWord(engWords[i][0]);
			}
		}
		sn.Commit();
		System.out.println("Dictionary Test values removed");
	}
}

