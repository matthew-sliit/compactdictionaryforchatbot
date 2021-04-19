package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict;

import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class DictionaryProvider implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		Hashtable<String, String> props = new Hashtable<String, String>();
        props.put("language_code", "EN");
        //register service for dictionary
        //english
        context.registerService(WordDictionary.class.getName(), new EnglishDictionary(), props);
        props.clear();
        //sinhala
        props.put("language_code", "SN");
        context.registerService(WordDictionary.class.getName(), new SinhalaDictionary(), props);
        props.clear();
        //spanish
        props.put("language_code", "ES");
        context.registerService(WordDictionary.class.getName(), new SpanishDictionary(), props);
        props.clear();
        //generic - any
        props.put("language_code", "g$");
        context.registerService(WordDictionary.class.getName(), new GenericDictionary(), props);
		System.out.println("Dictionary services registered and started successfully");	
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Dictionary Services Stopped!");
	}

}
