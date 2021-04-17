package consumer.worddict;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import producer.worddict.EnglishDictionary;
import producer.worddict.SinhalaDictionary;
import producer.worddict.commons.DictionaryException;
import producer.worddict.service.WordDictionary;

public class DictionaryConsumer implements BundleActivator {
	// Bundle's context.
    private BundleContext m_context = null;
    // The service tacker object.
    private ServiceTracker dictionary_tracker = null;
    
    private Boolean commited = true;
	@Override
	public void start(BundleContext context) throws Exception {
		 m_context = context;
		 // Create a service tracker to monitor dictionary services.
		 WordDictionary wordDictionary = null;
		 BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		 //initialize
		 String value1 = "", dictionaryLang = "EX"; Boolean isGeneric = false, exitcode=false;	
		 int value;
	     System.out.println("Dictionary Handler Started!");
	     System.out.println("Enter Locale: (EN|SN)");
			value1 = input.readLine();
			if(value1.equalsIgnoreCase("EN")) {
				//wordDictionary = new EnglishDictionary();
				dictionaryLang = "EN";
			}else if(value1.equalsIgnoreCase("SN")) {
				//wordDictionary = new SinhalaDictionary();
				dictionaryLang = "SN";
			}else {
				System.out.println("Undefined Language! Exiting!");
				exitcode = true;
				//input.close();
				//return;
			}
	 		//track dictionary service
	 		dictionary_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + WordDictionary.class.getName() + ")" +"(language_code="+dictionaryLang+"))"),null);
			dictionary_tracker.open();
			wordDictionary = (WordDictionary) dictionary_tracker.getService();
			//show dictionary type
			try {
				System.out.println("Selected: "+wordDictionary.getSimpleName());
			}catch (NullPointerException e) {
				System.out.println("Selected Dictionary Service is Not Available!");
			}
	     while (true && !exitcode)
         {
	    	try {
	    		System.out.println("=================================================");
				System.out.println("1-Add new word | 2-Add synonym | 3-Get word | 4-Get synonyms |5-Has word | 6-Get all words |"
			+ "7-Commit | 8-Remove word | 9-Exit");
				System.out.print("Enter the number : ");
				value = Integer.parseInt(input.readLine());
				System.out.println("=================================================");
				
				if(value == 1) {
					//addNew word
					System.out.print("Word : ");
					String newword = input.readLine();
					if(wordDictionary.hasWord(newword)) {
						System.out.println("Word Already Exists!");
					}else {
						System.out.print("Type : ");
						String type = input.readLine();
						System.out.print("Meaning : ");
						String meaning = input.readLine();
						System.out.println("-------------------------------------------------");
						System.out.println("Adding new word!");
						showOutput(newword,type,meaning);
						wordDictionary.addNewWord(newword, type, meaning);
					}
				}else if(value == 2) {
					//add synonym
					System.out.print("Word : ");
					String word = input.readLine();
					System.out.print("Synonym : ");
					String syn = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println("Adding synonym of the word!");
					wordDictionary.addSynonym(word, syn);					
					
				}else if(value == 3) {
					//get word
					System.out.print("Word : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println( wordDictionary.getWordMeaning(word));					
					
				
				}else if(value == 4) {
					//get synonyms						
					System.out.print("Word : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println("Synonyms :" + wordDictionary.getSynonyms(word));
				}
				else if(value == 5){
					System.out.print("Word : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					System.out.println("Result :" + wordDictionary.hasWord(word));
					
				}else if(value == 6){
					for(String word : wordDictionary.getAllWords()) {
						System.out.println(word);
					}
				}else if(value == 7) {
					wordDictionary.Commit();//save as preferences
					wordDictionary.selfUpdate();
					//wordDictionary = new EnglishDictionary();//reset
				}else if(value == 8) {
					System.out.print("Word : ");
					String word = input.readLine();
					System.out.println("-------------------------------------------------");
					wordDictionary.removeWord(word);
					System.out.println("Word removed successfully!");
				}else {
					exitcode = true;
					break;
				}
	    	}catch (NumberFormatException e) {
	    		exitcode = true;
				break;
			}catch (DictionaryException | IndexOutOfBoundsException | NullPointerException e) {
	    		System.out.println("Exception caught:"+wordDictionary.getSimpleName()+" ex="+e.getMessage());
			}
         }
	     input.close();
	     System.out.println("Dictionary Word Adder Stopping!");
	     stop(context);
	}
	private static void showOutput(String newword,String type,String meaning) {
		System.out.println("Word:" + newword);
		System.out.println("Type:" + type);
		System.out.println("Meaning:" + meaning);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// auto commit at end
		if(dictionary_tracker!=null) {
			if(!commited) {
			 	WordDictionary wordDictionary = (WordDictionary) dictionary_tracker.getService();
				wordDictionary.Commit();
			}
		}
		System.out.println("Dictionary Word Adder Stopped!");
	}

}
