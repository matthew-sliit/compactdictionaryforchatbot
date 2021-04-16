package testclasses.worddict;

import java.util.Scanner;

import worddict.EnglishDictionary;
import worddict.FrenchDictionary;
import worddict.SinhalaDictionary;
import worddict.SpanishDictionary;
import worddict.service.WordDictionary;

public class EnglishDicTest {

	public static void main(String[] args) {
		WordDictionary wordDictionary = new EnglishDictionary();
		Scanner input = new Scanner(System.in);
		int value;
		String value1;
		System.out.println("Dictionary Service Started!");
		
		System.out.println("Enter Locale: (EN|SN)");
		value1 = input.nextLine();
		if(value1.equalsIgnoreCase("EN")) {
			wordDictionary = new EnglishDictionary();
		}else if(value1.equalsIgnoreCase("SN")) {
			wordDictionary = new SinhalaDictionary();
		}else {
			System.out.println("Undefined Locale! Exiting!");
			input.close();
			return;
		}
		System.out.println("Selected: "+wordDictionary.getClass().getSimpleName());
		

		try {
			while(true){
				System.out.println("=================================================");
					System.out.println("1-Add new word | 2-Add synonym | 3-Get word | 4-Get synonyms |5-Has word | 6-Get all words |"
				+ "7-Commit | 8-Remove word");
					System.out.print("Enter the number : ");
					value = input.nextInt();
					System.out.println("=================================================");
					
					if(value == 1) {
						//addNew word
						System.out.print("Word : ");
						String newword = input.next();
						if(wordDictionary.hasWord(newword)) {
							System.out.println("Word Already Exists!");
						}else {
							System.out.print("Type : ");
							String type = input.next();
							System.out.print("Meaning : ");
							String meaning = input.next();
							System.out.println("-------------------------------------------------");
							System.out.println("Adding new word!");
							showOutput(newword,type,meaning);
							wordDictionary.addNewWord(newword, type, meaning);
						}
					}else if(value == 2) {
						//add synonym
						System.out.print("Word : ");
						String word = input.next();
						System.out.print("Synonym : ");
						String syn = input.next();
						System.out.println("-------------------------------------------------");
						System.out.println("Adding synonym of the word!");
						wordDictionary.addSynonym(word, syn);					
						
					}else if(value == 3) {
						//get word
						System.out.print("Word : ");
						String word = input.next();
						System.out.println("-------------------------------------------------");
						System.out.println( wordDictionary.getWordMeaning(word));					
						
					
					}else if(value == 4) {
						//get synonyms						
						System.out.print("Word : ");
						String word = input.next();
						System.out.println("-------------------------------------------------");
						System.out.println("Synonyms :" + wordDictionary.getSynonyms(word));
					}
					else if(value == 5){
						System.out.print("Word : ");
						String word = input.next();
						System.out.println("-------------------------------------------------");
						System.out.println("Result :" + wordDictionary.hasWord(word));
						
					}else if(value == 6){
						for(String word : wordDictionary.getAllWords()) {
							System.out.println(word);
						}
					}else if(value == 7) {
						wordDictionary.Commit();//save as preferences
						wordDictionary = new EnglishDictionary();//reset
					}else if(value == 8) {
						System.out.print("Word : ");
						String word = input.next();
						System.out.println("-------------------------------------------------");
						wordDictionary.removeWord(word);
						System.out.println("Word removed successfully!");
					}else {
						System.out.println("Dictionary Service Stopped!");
						break;
					}
					
			}
			}catch (Exception e) {
					// TODO: handle exception
					System.out.println("Exception caught:"+wordDictionary.getClass().getName()+" ex="+e.getMessage());
			}
		
		input.close();
	}
	
	private static void showOutput(String newword,String type,String meaning) {
		System.out.println("Word:" + newword);
		System.out.println("Type:" + type);
		System.out.println("Meaning:" + meaning);
	}
}
