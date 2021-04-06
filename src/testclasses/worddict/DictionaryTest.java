package testclasses.worddict;

import java.util.ArrayList;
import java.util.Scanner;

import worddict.FrenchDictionary;
import worddict.SpanishDictionary;
import worddict.commons.DictionaryException;
import worddict.service.WordDictionary;

public class DictionaryTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WordDictionary wordDictionary = null;
		Scanner input = new Scanner(System.in);
		String value = "";
		System.out.println("Dictionary Service Started!");
		System.out.println("=================================================");
		System.out.println("Enter Locale: (FR|ES)");
		value = input.nextLine();
		if(value.equalsIgnoreCase("FR")) {
			wordDictionary = new FrenchDictionary();
		}else if(value.equalsIgnoreCase("ES")) {
			wordDictionary = new SpanishDictionary();
		}else {
			System.out.println("Undefined Locale! Exiting!");
			input.close();
			return;
		}
		System.out.println("Selected: "+wordDictionary.getClass().getSimpleName());
		try {
			while(true){
				System.out.println("=================================================");
				value = input.nextLine();
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
					wordDictionary = new SpanishDictionary();//reset
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
	private static void showOutput(String[] data) {
		System.out.println("Word:"+data[0]);
		System.out.println("Type:"+data[1]);
		System.out.println("Meaning:"+data[2]);
	}
}
