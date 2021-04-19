package com.github.matthew_sliit.compactdictionaryforchatbot.serviceprovidertestclasses.chatbot;

import java.util.Scanner;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.EnglishChatbot;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.service.ChatbotService;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.EnglishSentenceToSpanishSentence;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.EnglishWordToSpanishWord;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.translator.service.SentenceTranslator;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.GenericDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class ChatBotTest {
	public static void main(String[] args) {
		ChatbotService chatbot = new EnglishChatbot(new GenericDictionary("EN","English"));
		SentenceTranslator stranslator = new EnglishSentenceToSpanishSentence(new GenericDictionary("EN","English"), new GenericDictionary("ES","Spanish"));
		Scanner input; Boolean userIsNative = true;
		try {
			input = new Scanner(System.in);
		    System.out.println("Chatbot Started!");
		    String value, response, prev_response;
		    System.out.println(">>"+chatbot.greetUser());
			while(true) {
				response = null;
				value = input.nextLine();
				if(value.isBlank()) {
					continue;
				}else if(value.contains("stop")) {
					System.out.println("Chatbot Stopped!");
					break;
				}else {
					try {
						if(stranslator.getAllSentences().containsValue(value)) {
							response = chatbot.generateBotReply(stranslator.getENTranslation(value));
						}	
					}catch (NullPointerException ignored) {
					}
					if(response == null) {
						//then not foreign
						response = chatbot.generateBotReply(value);
					}else {
						//again translate back to original language
						//ignore it if first letter is capital, returns a name or thing
						if(!Character.isUpperCase(response.charAt(0))) {
							//System.out.println("ESTranslated:"+stranslator.getSNTranslation(response));
							response = stranslator.getSNTranslation(response);
						}
					}
					
					/*
					if(response.equals("") || !userIsNative) {
						prev_response = response;//temp save respose
						//try a translated approach
						System.out.println("ENTranslated:"+stranslator.getENTranslation(value));
						response = chatbot.generateBotReply(stranslator.getENTranslation(value));
						System.out.println("chatbot:"+response);
						if(!response.equals("")) {
							userIsNative = false;//not a native user? then try translation?
						}else {
							userIsNative = true;
							response = prev_response;
						}
					}
					if(!userIsNative) {
						//again translate back to their language
						//ignore it if first letter is capital, returns a name or thing
						if(!Character.isUpperCase(response.charAt(0))) {
							//System.out.println("ESTranslated:"+stranslator.getSNTranslation(response));
							response = stranslator.getSNTranslation(response);
						}
					}
					*/
					System.out.println(">>"+response);
				}
			}
			input.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
