package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.commons;

import java.util.Map;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.BotMind;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.service.ChatbotMemory;

public class ChatBotException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3398301955622066446L;
	public ChatBotException(String msg) {
		super(msg);
	}
}
