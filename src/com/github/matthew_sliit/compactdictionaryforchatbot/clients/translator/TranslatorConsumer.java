package com.github.matthew_sliit.compactdictionaryforchatbot.clients.translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.common.TranslatorException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.SentenceTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.WordTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class TranslatorConsumer implements BundleActivator {
	// Bundle's context.
    private BundleContext m_context = null;
    // The service tacker object.
    private ServiceTracker stranslator_tracker = null, translator_tracker = null;
    
	@Override
	public void start(BundleContext context) throws Exception {
		 m_context = context;
		 // Create a service tracker to monitor dictionary services.
		 WordTranslator wordtranslator = null; SentenceTranslator sentTranslator = null;
		 BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); String value = "";
		 //filter out translators
		 translator_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + WordTranslator.class.getName() + ")" +"(translation=EN2SN_Word))"),null);
		 translator_tracker.open();
		 stranslator_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + SentenceTranslator.class.getName() + ")" +"(translation=EN2SN_Sentence))"),null);
		 stranslator_tracker.open();
		 //get translator services
		 wordtranslator = (WordTranslator) translator_tracker.getService();
		 sentTranslator = (SentenceTranslator) stranslator_tracker.getService();
		 if(wordtranslator==null||sentTranslator==null) {
			 System.out.println("Translator Services are NOT available!");
			 stop(context);
		 }
		 //TODO
		 while(true) {
				try {
					System.out.println("=================================================");
					value = input.readLine();
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
						for(Map.Entry<String, String> entry : wordtranslator.getAllWords().entrySet()) {
							System.out.println(entry.getKey()+"->"+entry.getValue());
						}
					}else if(value.equals("showes")) {
						for(String word : wordtranslator.getAllESWords()) {
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
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopped Translator Services!");
		// TODO
	}

}
