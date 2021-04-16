package testclasses.testtranslators;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import translator.EnglishSentenceToSinhalaSentence;
import translator.EnglishWordToSinhalaWord;
import translator.common.TranslatorException;
import translator.service.SentenceTranslator;
import translator.service.WordTranslator;
import worddict.GenericDictionary;
import worddict.commons.DictionaryException;
import worddict.service.WordDictionary;

public class TestEnglishSentenceToSinhalaSentence {

	public static void main(String[] args) {
		WordTranslator translator = new EnglishWordToSinhalaWord();
		SentenceTranslator stranslator=new EnglishSentenceToSinhalaSentence(); 
		Scanner input = new Scanner(System.in);
		String value = "";
		System.out.println("Dictionary Translator Service Started for EN-to-SN");	
		WordDictionary en = new GenericDictionary("EN","English");
		WordDictionary sn = new GenericDictionary("SN","Sinhala");
		String[] ignor= {"are","is"};
		try {
			//addnew how are you.ඔබට  කෙසේද
			//English Dictionary
			synchronized (en) {
				
			en.addNewWord("how", "Adverb", "condition");
			en.addNewWord("are", "Verb", "test");
			en.addNewWord("you", "Pronoun", "test");
			en.addNewWord("where", "Adverb","condition");
			en.Commit();
			System.out.println("Test values added for English Dictionary");
			//Sinhala Dictionary
			sn.addNewWord("කෙසේද", "Adverb", "se renseigner sur l'état");
			sn.addNewWord("වේ", "Verb", "Deuxième personne du singulier présent");
			sn.addNewWord("ඔබට", "Pronoun", "test");
			sn.addNewWord("කොහෙද", "Pronoun", "test");
			sn.Commit();
			}
			System.out.println("Test values added for Sinhala Dictionary");
			//English Dictionary
			//stranslator.addNewSentence(new String[] {"how ", "are ", "you"},new String[] {"ඔබට  ", "කෙසේද"});
			//stranslator.addNewSentence(new String[] {"where ", "are ", "you"},new String[] {"ඔබ  ", "කොහෙද"});
             stranslator.removeall();
			//en.addNewWord("are", "Verb", "test");
			//en.addNewWord("you", "Pronoun", "test");
			//en.Commit();
			//System.out.println("Test values added for Spanish Dictionary");
			//System.out.println(stranslator.getSentences("how are you"));
		
			//System.out.println("Test values added for French Dictionary");
			while(true) {
				System.out.println("=================================================");
				value = input.nextLine();
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
					for(Map.Entry<String, String> entry :stranslator.getAllWords().entrySet()) {
						System.out.println(entry.getKey()+"->"+entry.getValue());
					}
				}else if(value.startsWith("tword ")) {
					//word to French
					String translateWord = (String) value.subSequence(6, value.length());
					if(translator.getTranslatedWord(translateWord).equals("+unmapped")) {
						System.out.println("Word is not yet translated!");
					}else {
						System.out.println(translator.getTranslatedWord(translateWord));
					}
				}else if(value.startsWith("tsent ")) {
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
					//words to French
				}else if(value.equals("show2words")) {
					for(String word : sn.getAllWords()) {
						System.out.println(word);
					}
				}else if(value.startsWith("commit")) {
					stranslator.Commit();
					stranslator=new EnglishSentenceToSinhalaSentence();
				}
				else {
					break;
				}
			}
			
		}catch (DictionaryException e) {
			// TODO: handle exception
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		
		//Spanish Dictionary
		en.removeWord("where");
		en.removeWord("how");
		en.removeWord("are");
		en.removeWord("you");
		en.Commit();
		//French Dictionary
		sn.removeWord("කොහෙද");
		sn.removeWord("කෙසේද");
		sn.removeWord("වේ");
		sn.removeWord("ඔබට");
		sn.Commit();
		//System.out.println("Dictionary Test values removed");
		System.out.println("Dictionary Translator Service Stopped for EN-to-SN");	
		input.close();
	}
}

