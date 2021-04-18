package producer.chatbot;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import producer.chatbot.service.ChatbotService;
import producer.worddict.GenericDictionary;
import producer.worddict.service.WordDictionary;

public class BotProvider implements BundleActivator {
	// Bundle's context.
    private BundleContext m_context = null;
    // The service tacker object.
    private ServiceTracker dictionary_tracker = null;
    
	@Override
	public void start(BundleContext arg0) throws Exception {
		 WordDictionary engdictionary = null;
		//filter class, g& for generic
		//will track for EnglishDictionary
		m_context = arg0;
		dictionary_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + WordDictionary.class.getName() + ")" +"(language_code=EN))"),null);
		dictionary_tracker.open();
        //cast filter to Interface
		engdictionary = (WordDictionary) dictionary_tracker.getService();
        if(engdictionary == null) {
        	System.out.println("Dictionary Services are Unavailable!");
        }
        //property to uniquely identify class
		Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("chatbot_lang", "EN");
        arg0.registerService(ChatbotService.class.getName(), new EnglishChatbot(engdictionary), props);
		System.out.println("ChatBot Service Started!");	
		// TODO Add translator to bot?
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO logging?
		System.out.println("ChatBot Service Stopped!");	
	}

}
