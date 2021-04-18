package testclasses.chatbot;

import java.util.Scanner;

import producer.chatbot.EnglishChatbot;
import producer.chatbot.service.ChatbotService;
import producer.translator.EnglishSentenceToSinhalaSentence;
import producer.translator.EnglishWordToSinhalaWord;
import producer.translator.service.SentenceTranslator;
import producer.worddict.GenericDictionary;
import producer.worddict.service.WordDictionary;

public class ChatBotTest {
	public static void main(String[] args) {
		ChatbotService chatbot = new EnglishChatbot(new GenericDictionary("EN","English"));
		SentenceTranslator stranslator = new EnglishSentenceToSinhalaSentence(new GenericDictionary("EN","English"), new GenericDictionary("ES","Spanish"));
		Scanner input; Boolean userIsNative = true;
		try {
			input = new Scanner(System.in);
		    System.out.println("Chatbot Started!");
		    String value, response, prev_response;
		    System.out.println(">>"+chatbot.greetUser());
			while(true) {
				value = input.nextLine();
				if(value.isBlank()) {
					continue;
				}else if(value.contains("stop")) {
					System.out.println("Chatbot Stopped!");
					break;
				}else {
					response = chatbot.generateBotReply(stranslator.getENTranslation(value));
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
