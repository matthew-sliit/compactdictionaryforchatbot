package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.runners.PerspectiveIdentifier;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.runners.SentenceAnalyzer;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.runners.SentenceWordTypeIdentifier;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.service.ChatbotMemory;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.service.ChatbotService;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.GenericDictionary;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.commons.DictionaryException;
import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.worddict.service.WordDictionary;

public class EnglishChatbot extends EnglishSentenceStructure implements ChatbotService{
	ArrayList<String> words;
	ConcurrentHashMap<String, String> wordsWithType;
	
	WordDictionary englishdictionary = null;
	ChatbotMemory botmem;
	public EnglishChatbot(WordDictionary enDictionary) {
		super();
		wordsWithType = new ConcurrentHashMap<>();
		words = new ArrayList<String>();
		botmem = new BotMind();
		this.englishdictionary = enDictionary;
	}
	
	@Override
	public String generateBotReply(String msg) throws DictionaryException, InterruptedException{
		StringTokenizer stringTokenizer = new StringTokenizer(msg," ");
		words.clear();
		wordsWithType.clear();
		int wordIndex = 0;
		while(stringTokenizer.hasMoreTokens()) {
			if(wordIndex==0) {
				words.add(stringTokenizer.nextToken().toLowerCase());//always starting word to lower case
				wordIndex=1;//avoid others from being set to lower case
			}else {
				String word =  stringTokenizer.nextToken();
				if(word.equals("I")) {
					//sometimes letter 'I' is capitalized
					words.add(word.toLowerCase());//always starting word to lower case
				}else {
					words.add(word);//allow uppercase for names and objects
				}
			}
		}
		//when sentence ends with punctuation
		String lastword = words.get(words.size()-1);
		String lastletter = lastword.substring(lastword.length()-1);
		for(String p : PUNCTUATIONS) {
			if(lastletter.equals(p)) {
				lastword = lastword.substring(0, lastword.length()-1);
				words.set(words.size()-1, lastword);
				words.add(p);//add last letter punctuation as word
				break;
			}
		}
		//System.out.println("lastword="+lastword);
		//save words to dictionary before everything else
		initializeDictionary();
		//identify 1st, 2nd or 3rd person
		PerspectiveIdentifier sentenceIdentifier = new PerspectiveIdentifier(words);
		//attach type to each word
		SentenceWordTypeIdentifier sentenceWordTypeIdentifier = new SentenceWordTypeIdentifier(words, englishdictionary);
		Thread t1 = new Thread(sentenceIdentifier);//to identify persons
		Thread t2 = new Thread(sentenceWordTypeIdentifier);//to sort out types of words
		//order doesn't matter
		t1.run(); t2.run(); 
		//finish up both threads
		t1.join();t2.join();
		//then analyze sentence from given data
		SentenceAnalyzer sentenceAnalyzer = new SentenceAnalyzer(words, sentenceIdentifier, sentenceWordTypeIdentifier, botmem);
		Thread analyzer = new Thread(sentenceAnalyzer);
		analyzer.start();//start
		analyzer.join();//end
		//keep botmem instance updated at all times, required for 2nd round data pass
		this.botmem = sentenceAnalyzer.updateChatBotMemory();
		
		return sentenceAnalyzer.getReply();
	}
	private void initializeDictionary() throws DictionaryException{
		//words predefined in EnglishSentenceStructure should
		//be defined in dictionary, unless dictionary already has it
		for(String word : PERSON1SINGULAR) {
			if(!englishdictionary.hasWord(word)) {
				englishdictionary.addNewWord(word, Type.PRONOUN.string, "used to refer to myself, the speaker");
			}
		}
		for(String word : PERSON1PLURAL) {
			if(!englishdictionary.hasWord(word)) {
				englishdictionary.addNewWord(word, Type.PRONOUN.string, "used to refer to myself with one or more other people");
			}
		}
		//you
		if(!englishdictionary.hasWord(PERSON2[0])) {
			englishdictionary.addNewWord(PERSON2[0], Type.PRONOUN.string, "used to refer to a person in general");
		}
		//your
		if(!englishdictionary.hasWord(PERSON2[1])) {
			englishdictionary.addNewWord(PERSON2[1], Type.DETERMINER.string, "used to refer to a person in general");
		}
		//yourself
		if(!englishdictionary.hasWord(PERSON2[2])) {
			englishdictionary.addNewWord(PERSON2[2], Type.PRONOUN.string, "used to refer to a person in general");
		}
		//3rd person singular
		for(String word : PERSON3SINGULAR) {
			if(word.equals("his") && !englishdictionary.hasWord(word)) {
				englishdictionary.addNewWord(word, Type.DETERMINER.string, "Test");
			}
			else if(!englishdictionary.hasWord(word)) {
				englishdictionary.addNewWord(word, Type.PRONOUN.string, "Test");
			}
		}
		//3rd person plural
		for(String word : PERSON3PLURAL) {
			if(!englishdictionary.hasWord(word)) {
				englishdictionary.addNewWord(word, Type.PRONOUN.string, "Test");
			}
		}
		//punctuation, is actually a noun, but for easy identification
		for(String symbol : PUNCTUATIONS) {
			if(!englishdictionary.hasWord(symbol)) {
				englishdictionary.addNewWord(symbol, Type.PUNCTUATION.string, "Punctuation mark");
			}
		}
		//listenword
		for(String word : LISTENWORDS) {
			if(!englishdictionary.hasWord(word)) {
				englishdictionary.addNewWord(word, Type.VERB.string, "Test");
			}
		}
		//likes
		for(String word : LIKES) {
			if(!englishdictionary.hasWord(word)) {
				englishdictionary.addNewWord(word, Type.VERB.string, "Test");
			}
		}
		
	}
	@Override
	public String greetUser() {
		int numOfGreetModels = greet.length-1;
		String greetwith = greet[getRand(0, numOfGreetModels)];
		return greetwith;
	}
	private int getRand(int min, int max) {
		return (int) Math.floor(Math.random()*(max-min+1)+min);
	}
}
