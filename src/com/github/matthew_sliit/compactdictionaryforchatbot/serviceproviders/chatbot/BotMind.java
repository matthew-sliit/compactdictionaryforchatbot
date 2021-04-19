package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.service.ChatbotMemory;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.WordData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class BotMind implements ChatbotMemory{
	public String previousReply = "";//instance specific
	private final String lighmemoryname = "lightmemory";//preferences key for light memory
	ConcurrentHashMap<String, String> lightmem;//about user: key,value only
	private final String deepmemoryname = "deepmemory";//preferences key for deep memory
	ConcurrentHashMap<String, ArrayList<String>> deepmem;//about user: key,values
	String memorizevalue = "";
	//preferences
	Preferences preferences = Preferences.userNodeForPackage(ChatbotMemory.class);
	//gson
	Gson gson = new Gson();
	public BotMind() {
		lightmem = new ConcurrentHashMap<String, String>();
		deepmem = new ConcurrentHashMap<String, ArrayList<String>>();
		recallLighMemoryUsingGSON();//about user
		recallDeepMemoryUsingGSON();//about places
	}
	@Override
	public void save() {
		//unsafe, if preferences.put doesn't run by a failure; all data could be lost
		preferences.remove(lighmemoryname);
		//save usermemory to preferences
		String json1 = gson.toJson(lightmem);//convert hashmap to json string
		preferences.put(lighmemoryname, json1);//store in preferences
		if(deepmem.size()>0) {
			preferences.remove(deepmemoryname);
			//save placesmemory to preferences
			json1 = gson.toJson(deepmem);//convert hashmap to json string
			preferences.put(deepmemoryname, json1);//store in preferences
		}
		/*
		System.out.println("saved!");
		for(Map.Entry<String, String> e:lightmem.entrySet()) {
			System.out.println("lightmem:"+e.getKey() + " -> "+e.getValue());
		}
		for(Entry<String, ArrayList<String>> e:deepmem.entrySet()) {
			System.out.println("deepmem:"+e.getKey() + " -> "+e.getValue().toString());
		}*/
	}
	@Override
	public void recallLighMemoryUsingGSON() {
		String memory = preferences.get(lighmemoryname, null);
		if(memory!=null) {
			java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, String>>(){}.getType();
			lightmem = gson.fromJson(memory, type);
		}
	}
	@Override
	public void recallDeepMemoryUsingGSON() {
		// TODO Auto-generated method stub
		String memory = preferences.get(deepmemoryname, null);
		if(memory!=null) {
			java.lang.reflect.Type type = new TypeToken<ConcurrentHashMap<String, ArrayList<String>>>(){}.getType();//get type of HashMap<K,V>
			deepmem = gson.fromJson(memory, type);
		}
	}
	@Override
	public void brainwash() {
		lightmem = new ConcurrentHashMap<String, String>();
		deepmem = new ConcurrentHashMap<String, ArrayList<String>>();
		//remove all
		preferences.remove(lighmemoryname);
		preferences.remove(deepmemoryname);
		try {
			preferences.removeNode();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		preferences = Preferences.userNodeForPackage(ChatbotMemory.class);
	}
	@Override
	public void memorizetolightmem(String key) {
		this.memorizevalue = "";//reset
		this.lightmem.put(key, null);//like$word.word..,thing
	}
	@Override
	public void memorizetolightmem(String key, String data) {
		this.memorizevalue = "";//reset
		this.lightmem.put(key, data);
		
	}
	@Override
	public void temporarymemorize(String value) {
		memorizevalue = value;
	}
	@Override
	public String getTemporaryMemory() {
		return this.memorizevalue;
	}
	public String getPreviousReply() {
		return this.previousReply;
	}
	public void setReply(String reply) {
		this.previousReply = reply;
	}
	@Override
	public String getValueFromLightMemory(String keyword) {
		try {
		if(lightmem.containsKey(keyword) || lightmem.containsValue(keyword)) {
			for(Map.Entry<String, String> entry : lightmem.entrySet()) {
				if(keyword.equals(entry.getKey())){
					return entry.getValue();
				}
				if(keyword.equals(entry.getValue())) {
					return entry.getKey();
				}
			}
			return null;
		}
		}catch (NullPointerException e) {
		}
		return null;		
	}
	@Override
	public ConcurrentHashMap<String, String> retrieveLighMemory() {
		return this.lightmem;
	}
	/**
	 * 
	 */
	//only key1 and key2
	@Override
	public void memorizetodeepmem(String key1, String key2) {
		String key = key1 + DEEPMEM_KEY_DELIMINATOR + key2;
		this.deepmem.put(key, new ArrayList<String>());
	}
	//add value
	@Override
	public void memorizetodeepmem(String key1, String key2, String value) {
		if(!deepmem.isEmpty()) {
			if(deepmem.containsKey(getDeepMemoryKey(key1, key2))) {
				deepmem.get(getDeepMemoryKey(key1, key2)).add(value);
			}else {
				memorizetodeepmem(key1, key2);
				deepmem.get(getDeepMemoryKey(key1, key2)).add(value);
			}
		}else {
			memorizetodeepmem(key1, key2);
			deepmem.get(getDeepMemoryKey(key1, key2)).add(value);
		}
	}
	//add key1, key2 and values
	@Override
	public void memorizetodeepmem(String key1, String key2, ArrayList<String> values) {
		String key = key1 + DEEPMEM_KEY_DELIMINATOR + key2;
		this.deepmem.put(key, values);
	}
	//get key2
	@Override
	public ArrayList<String> getKey2FromDeepMemory(String keyword) {
		ArrayList<String> key2s = new ArrayList<String>();
		String key2 = "";
		for(Map.Entry<String, ArrayList<String>> entry : deepmem.entrySet()) {
			if(entry.getKey().startsWith(keyword)) {
				//get word after DEEPMEM_KEY_DELIMINATOR
				key2 = entry.getKey().substring(entry.getKey().lastIndexOf(DEEPMEM_KEY_DELIMINATOR));
				//ignore listenword$verb.word which will have values
				//focus on listenword$word
				if(!key2.contains(".")) {
					key2s.add(key2);
				}
			}
		}
		return key2s;
	}
	//get values
	@Override
	public ArrayList<String> getValuesFromDeepMemory(String key1, String key2) {
		String key = key1+DEEPMEM_KEY_DELIMINATOR+key2;
		if(!deepmem.containsKey(key)) {
			return null;
		}
		return this.deepmem.get(key);
	}
	private String getDeepMemoryKey(String key1, String key2) {
		return key1 + DEEPMEM_KEY_DELIMINATOR + key2;
	}
}
