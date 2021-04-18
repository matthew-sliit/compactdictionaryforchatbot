package producer.chatbot.service;

import producer.worddict.commons.DictionaryException;

public interface ChatbotService{
	String greetUser();
	String generateBotReply(String msg)throws DictionaryException, InterruptedException;
}
