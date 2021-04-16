package testclasses.testtranslators;

import java.util.Map;
import java.util.Scanner;

import translator.SpanishWordToFrenchWord;
import worddict.FrenchDictionary;
import worddict.SpanishDictionary;
import worddict.service.WordDictionary;

public class SpanishWordToFrenchWordTest {
	public static void main(String[] args) {
		SpanishWordToFrenchWord translatorES2FR = new SpanishWordToFrenchWord();
		Scanner input = new Scanner(System.in);
		String value = "";
		System.out.println("Dictionary Translator Service Started for ES-to-FR");	
		WordDictionary es = new SpanishDictionary();
		WordDictionary fr = new FrenchDictionary();
		try {
			//how are you in spanish -> Como estas?, are word -> not direct
			//Spanish Dictionary
			es.addNewWord("como", "adverbio", "pregunta sobre la condición");
			es.addNewWord("estan", "el nombre", "segunda persona presente singular");
			es.addNewWord("tu", "el pronombre", "utilizado para referirse a cualquier persona en general");
			es.Commit();
			System.out.println("Test values added for Spanish Dictionary");
			//French Dictionary
			//how are you in french -> Comment allez vous?
			fr.addNewWord("comment", "adverbe", "se renseigner sur l'état");
			fr.addNewWord("sommes", "nom", "Deuxième personne du singulier présent");
			fr.addNewWord("tu", "pronom", "utilisé pour désigner toute personne en général");
			fr.Commit();
			System.out.println("Test values added for French Dictionary");
			while(true) {
				System.out.println("=================================================");
				value = input.nextLine();
				if(value.startsWith("addnew ")) {
					//addNewMap FromWord.ToWord
					String newMap = (String) value.subSequence(7, value.length());
					String[] splitWords = newMap.split("\\.");
					translatorES2FR.addNewMap(splitWords[0], splitWords[1]);
					
				}else if(value.startsWith("getallunmapped")) {
					for(String word: translatorES2FR.getAllUnMappedWords()) {
						System.out.println(word);
					}
				}else if(value.equals("getall") || value.equals("showall")) { 
					for(Map.Entry<String, String> entry : translatorES2FR.getAllWords().entrySet()) {
						System.out.println(entry.getKey()+"->"+entry.getValue());
					}
				}else if(value.startsWith("tword ")) {
					//word to French
					String translateWord = (String) value.subSequence(14, value.length());
					if(translatorES2FR.getTranslatedWord(translateWord).equals("+unmapped")) {
						System.out.println("Word is not yet translated!");
					}else {
						System.out.println(translatorES2FR.getTranslatedWord(translateWord));
					}
				}else if(value.startsWith("tsent ")) {
					//words to French
				}else if(value.equals("showtowords")) {
					for(String word : fr.getAllWords()) {
						System.out.println(word);
					}
				}
				else {
					break;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		//Spanish Dictionary
		es.removeWord("como");
		es.removeWord("estan");
		es.removeWord("tu");
		es.Commit();
		//French Dictionary
		fr.removeWord("comment");
		fr.removeWord("sommes");
		fr.removeWord("tu");
		fr.Commit();
		System.out.println("Dictionary Test values removed");
		System.out.println("Dictionary Translator Service Stopped for ES-to-FR");	
		input.close();
	}
}
