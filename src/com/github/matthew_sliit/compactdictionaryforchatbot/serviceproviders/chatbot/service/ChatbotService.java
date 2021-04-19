package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.service;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;

public interface ChatbotService{
	String greetUser();
	String generateBotReply(String msg)throws DictionaryException, InterruptedException;
}
