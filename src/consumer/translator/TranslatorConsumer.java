package consumer.translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import producer.translator.common.TranslatorException;
import producer.translator.service.SentenceTranslator;
import producer.translator.service.WordTranslator;
import producer.worddict.commons.DictionaryException;
import producer.worddict.service.WordDictionary;

public class TranslatorConsumer implements BundleActivator {
	// Bundle's context.
    private BundleContext m_context = null;
    // The service tacker object.
    private ServiceTracker stranslator_tracker = null, translator_tracker = null;
    
	@Override
	public void start(BundleContext context) throws Exception {
		 m_context = context;
		 // Create a service tracker to monitor dictionary services.
		 WordTranslator wordTranslator = null; SentenceTranslator sentTranslator = null;
		 BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); String value = "";
		 //filter out translators
		 translator_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + WordTranslator.class.getName() + ")" +"(translation=EN2SN_Word))"),null);
		 translator_tracker.open();
		 stranslator_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + SentenceTranslator.class.getName() + ")" +"(translation=EN2SN_Sentence))"),null);
		 stranslator_tracker.open();
		 //get translator services
		 wordTranslator = (WordTranslator) translator_tracker.getService();
		 sentTranslator = (SentenceTranslator) stranslator_tracker.getService();
		 if(wordTranslator==null||sentTranslator==null) {
			 System.out.println("Translator Services are NOT available!");
			 stop(context);
		 }
		 //TODO
		 while(true) {
			 try {
				 System.out.println("=================================================");
				 value = input.readLine();
				 if(value.startsWith("addnew ")) {
						//addNewMap FromWord.ToWord
						String newMap = (String) value.subSequence(7, value.length());
						String[] splitWords = newMap.split("\\.");
						//System.out.println(splitWords[0]+" -> "+splitWords[1]);
						wordTranslator.addNewMap(splitWords[0], splitWords[1]);
						System.out.println("Your word was added successfully to the dictionary!");
				 }
				 else if(value.equals("getallsentences") || value.equals("showalls")) { 
					 for(Map.Entry<String, String> entry : sentTranslator.getAllSentences().entrySet()) {
							System.out.println(entry.getKey()+"->"+entry.getValue());
					 }	
				}else if(value.equals("getallwords") || value.equals("showall")) { 
					for(Map.Entry<String, String> entry : wordTranslator.getAllWords().entrySet()) {
						System.out.println(entry.getKey()+"->"+entry.getValue());
					}
				}else {
					//this is a must
					break;
				}
			 }catch (DictionaryException | TranslatorException | NullPointerException e) {
				// TODO: handle exception
			}
		 }
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopped Translator Services!");
		// TODO
	}

}
