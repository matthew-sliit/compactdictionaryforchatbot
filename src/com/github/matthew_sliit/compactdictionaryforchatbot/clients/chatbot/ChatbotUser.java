package com.github.matthew_sliit.compactdictionaryforchatbot.clients.chatbot;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.service.ChatbotService;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.SentenceTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;

public class ChatbotUser implements BundleActivator {
	// Bundle's context.
    private BundleContext m_context = null;
    // The service tacker object.
    private ServiceTracker chatbotsvc_tracker = null, translatorsvc_tracker = null;
    
	@SuppressWarnings("unchecked")
	@Override
	public void start(BundleContext arg0) throws Exception {
		System.out.println("Chatbot Client Started!");
		//track for English Chat Bot service
		m_context = arg0;
		chatbotsvc_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + ChatbotService.class.getName() + ")" +"(chatbot_lang=EN))"),null);
		chatbotsvc_tracker.open();
		//get implemented class
		ChatbotService botsvc = (ChatbotService) chatbotsvc_tracker.getService();
		try {
			if(botsvc==null) {
				System.out.println("Chatbot service provider unavailable!");
			}
		}catch (NullPointerException e) {
			System.out.println("Chatbot service provider unavailable!");
		}
		//track for translator Service, needed for different languages
		translatorsvc_tracker = new ServiceTracker(m_context,m_context.createFilter("(&(objectClass=" + SentenceTranslator.class.getName() + ")" +"(translation=EN2SN_Sentence))"),null);
		translatorsvc_tracker.open();
		SentenceTranslator stranslator = (SentenceTranslator) translatorsvc_tracker.getService();
		try {
			if(stranslator == null) {
				System.out.println("Translator service provider unavailable!");
			}
		}catch (NullPointerException e) {
			System.out.println("Translator service provider unavailable!");
		}
		//get input from user
		//thread safe
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String value, response = null, prev_response = null; Boolean userIsNative = true;//English user
		System.out.println(">>"+botsvc.greetUser());
		while(true) {
			try {
				response = null;//reinitialize
				value = input.readLine();
				if(value.contains("botstop") || value.contains("bye") || value.contains("sayonara") || value.contains("exit")) {
					System.out.println("Chatbot Stopping!");
					break;
				}else {
					//System.out.println("Getting Response!");
					//try translating the input
					try {
						if(stranslator.getAllSentences().containsValue(value)) {
							response = botsvc.generateBotReply(stranslator.getENTranslation(value));
						}	
					}catch (NullPointerException ignored) {
					}
					//if no translation
					if(response == null) {
						//then not foreign, means input in English
						response = botsvc.generateBotReply(value);
					}else {
						//for non English speaker
						//again translate English sentence from bot back to original language
						//ignore it if first letter is capital, returns a name or thing
						if(!Character.isUpperCase(response.charAt(0))) {
							//System.out.println("ESTranslated:"+stranslator.getSNTranslation(response));
							prev_response = response;
							response = stranslator.getSNTranslation(response);
						}
						//if not matching translation then reply with English?
						if(response == null) {
							response = prev_response;
						}
					}
					System.out.println(">>"+response);
				}
			}catch (DictionaryException | InterruptedException | NullPointerException e) {
				// TODO: log to file?
				System.out.println("botEX:"+e.getLocalizedMessage());
			}
		}
	}
	@Override
	public void stop(BundleContext arg0) throws Exception {
		// TODO log to file?
		System.out.println("Chatbot Client Stopped!");
	}

}
