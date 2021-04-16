package testclasses.testtranslators;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import translator.EnglishWordToSinhalaWord;
import translator.common.TranslatorException;
import translator.service.WordTranslator;
import worddict.GenericDictionary;
import worddict.commons.DictionaryException;
import worddict.service.WordDictionary;

public class TestEnglishWordToSinhala {

		public static void main(String[] args) {
			WordTranslator translatorES2FR = new EnglishWordToSinhalaWord();
			Scanner input = new Scanner(System.in);
			String value = "";
			System.out.println("Dictionary Translator Service Started for EN-to-SN");	
			WordDictionary en = new GenericDictionary("EN","English");
			WordDictionary sn = new GenericDictionary("SN","Sinhala");
			String[] ignor= {"are","is"};
			try {
				//English Dictionary
				en.addNewWord("how", "Adverb", "condition");
				en.addNewWord("are", "Verb", "test");
				en.addNewWord("you", "Pronoun", "test");
				//en.addNewWord("where", "Adverb","condition");
				en.Commit();
				System.out.println("Test values added for English Dictionary");
				//Sinhala Dictionary
				sn.addNewWord("කෙසේද", "Adverb", "se renseigner sur l'état");
				sn.addNewWord("වේ", "Verb", "Deuxième personne du singulier présent");
				sn.addNewWord("ඔබට", "Pronoun", "test");
				//sn.addNewWord("කොහෙද", "Pronoun", "test");
				sn.Commit();
				
				System.out.println("Test values added for Sinhala Dictionary");
				while(true) {
					try {
					System.out.println("=================================================");
					value = input.nextLine();
					if(value.startsWith("addnew ")) {
						//addNewMap FromWord.ToWord
						String newMap = (String) value.subSequence(7, value.length());
						String[] splitWords = newMap.split("\\.");
						//System.out.println(splitWords[0]+" -> "+splitWords[1]);
						translatorES2FR.addNewMap(splitWords[0], splitWords[1]);
						System.out.println("Your word was added successfully to the dictionary!");
						
				      
						
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
						String translateWord = (String) value.subSequence(6, value.length());
						if(translatorES2FR.getTranslatedWord(translateWord).equals("+unmapped")) {
							System.out.println("Word is not yet translated!");
						}else {
							System.out.println(translatorES2FR.getTranslatedWord(translateWord));
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
                        	 sent+=translatorES2FR.getTranslatedWord(st.nextToken())+" ";
                        	 
                         }
						}
                        System.out.println("result = "+sent);
						//words to French
					}else if(value.equals("show2words")) {
						for(String word : sn.getAllWords()) {
							System.out.println(word);
						}
					}else if(value.startsWith("commit")) {
						translatorES2FR.Commit();
					}
					
					else {
						System.out.println("You entered a wrong keyword!!\n");

						break;
					}
				}
					catch(TranslatorException e) {
						System.out.println(e.getLocalizedMessage());
					}
				}
				
			}catch (DictionaryException e) {
				// TODO: handle exception
				System.out.println(e.getLocalizedMessage());
			}
			
			
			//English Dictionary
			en.removeWord("how");
			en.removeWord("are");
			en.removeWord("you");
			en.Commit();
			//Sinhala Dictionary
			sn.removeWord("කෙසේද");
			sn.removeWord("වේ");
			sn.removeWord("ඔබට");
			sn.Commit();
			System.out.println("Dictionary Test values removed");
			System.out.println("Dictionary Translator Service Stopped for EN-to-SN");	
			input.close();
		}
	}

