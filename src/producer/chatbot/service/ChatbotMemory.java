package producer.chatbot.service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public interface ChatbotMemory {
	final String KEY_USERNAME = "username";
	final String DEEPMEM_KEY_DELIMINATOR = "$";
	//set
	void temporarymemorize(String value);
	void memorizetolightmem(String key);
	void memorizetolightmem(String key, String data);
	void setReply(String reply);
	void save();//save light and deep memory as preferences
	void recallLighMemoryUsingGSON();//load preferences
	void recallDeepMemoryUsingGSON();//load preferences
	//get
	//temp string variable, not light or deep
	String getTemporaryMemory();
	String getPreviousReply();
	String getValueFromLightMemory(String keyword);
	ConcurrentHashMap<String, String> retrieveLighMemory();
	//reset all memories
	void brainwash();
	/**
	 * DEEP MEMORY
	 * key1 combined with key2 with $ as separator, values then entered
	 * [key1$key2,values]
	 */
	//setter
	//for only key1$key2
	void memorizetodeepmem(String key1, String key2);
	//for key1$key2 and values
	void memorizetodeepmem(String key1, String key2, ArrayList<String> values);
	void memorizetodeepmem(String key1, String key2, String value);
	//getter
	ArrayList<String> getKey2FromDeepMemory(String key1);
	ArrayList<String> getValuesFromDeepMemory(String key1,String type2);
}
