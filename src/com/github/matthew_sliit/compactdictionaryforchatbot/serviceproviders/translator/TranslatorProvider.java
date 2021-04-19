package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.common.TranslatorException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.SentenceTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.WordTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class TranslatorProvider implements BundleActivator {
	// Bundle's context.
    private BundleContext m_context = null;
	private ServiceTracker enDictionary_tracker = null, snDictionary_tracker = null;
	@Override
	public void start(BundleContext context) throws Exception {
		m_context = context;
		WordDictionary english = null, sinhala = null;
		enDictionary_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + WordDictionary.class.getName() + ")" +"(language_code=EN))"),null);
		enDictionary_tracker.open();
		snDictionary_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + WordDictionary.class.getName() + ")" +"(language_code=ES))"),null);
		snDictionary_tracker.open();
		//get dictionary services
		english = (WordDictionary) enDictionary_tracker.getService();
		sinhala = (WordDictionary) snDictionary_tracker.getService();
		if(english==null || sinhala==null) {
			System.out.println("Dictionary Services are Unvailable!");
			stop(context);
		}
		//get ready to register translator provider services
		Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("translation", "EN2SN_Word");
        //register service
        context.registerService(WordTranslator.class.getName(), new EnglishWordToSpanishWord(english,sinhala), props);
        props.clear();
        props.put("translation", "EN2SN_Sentence");
        context.registerService(SentenceTranslator.class.getName(), new EnglishSentenceToSpanishSentence(english,sinhala), props);
        System.out.println("Translator Service Started!");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		System.out.println("Translator Service Stopped!");
	}

}
