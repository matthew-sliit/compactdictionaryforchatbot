package producer.chatbot.commons;

import java.util.Map;

import producer.chatbot.BotMind;
import producer.chatbot.service.ChatbotMemory;

public class ChatBotException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3398301955622066446L;
	public ChatBotException(String msg) {
		super(msg);
	}
}
