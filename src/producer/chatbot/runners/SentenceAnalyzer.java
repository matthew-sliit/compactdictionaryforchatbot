package producer.chatbot.runners;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import producer.chatbot.EnglishSentenceStructure;
import producer.chatbot.service.ChatbotMemory;

public class SentenceAnalyzer extends EnglishSentenceStructure implements Runnable{
	private ArrayList<String> words;
	private Boolean isGreeting = false, resolved = false, extractData = false, getUserInput = false;
	private String generatedReply = "";
	private SentenceWordTypeIdentifier sentenceIdentifier;
	private PerspectiveIdentifier perspectiveIdentifier;
	private ChatbotMemory botmem;
	public SentenceAnalyzer(ArrayList<String> words, PerspectiveIdentifier id, SentenceWordTypeIdentifier sentenceIdentifier, ChatbotMemory botmem) {
		this.words = words;
		this.perspectiveIdentifier = id;
		this.sentenceIdentifier = sentenceIdentifier;
		this.botmem = botmem;
	}
	public String getReply() {
		return this.generatedReply;
	}
	public boolean shouldExtractData() {
		return this.extractData;
	}
	public ChatbotMemory updateChatBotMemory() {
		return this.botmem;
	}
	@Override
	public void run() {
		//starts with question word?
		//System.out.println("SentenceAnalyzer started! " + words.toString());
		botmem.recallLighMemoryUsingGSON();//refresh botmind
		botmem.recallDeepMemoryUsingGSON();//refresh botmind
		if(words.contains("@yuna")) {
			//special case
			botmem.brainwash();
			generatedReply = "Yuno brainwashed!";
		}
		else if(!botmem.getTemporaryMemory().isEmpty()) {
			//used when a 2nd round of data is required
			ArrayList<String> nouns = sentenceIdentifier.wordsForType.get(TypeOrder.NOUN.index);
			if(nouns.size()==0 && words.size()==1) {
				//no nouns? has words, save 
				botmem.memorizetolightmem(words.get(0), botmem.getTemporaryMemory());//first noun
				botmem.save();
				extractData = false;
				generatedReply = "noted!";
			}else if(nouns.size()>1) {
				//complex situation, many nouns
				generatedReply = "Please tell in simple words!";
				getUserInput = true;//prevent rerunning extractData condition
			}else {
				//only 1 noun
				botmem.memorizetolightmem(nouns.get(0), botmem.getTemporaryMemory());//first noun
				botmem.save();
				extractData = false;
				generatedReply = "noted!";
			}
		}
		//sentence starts with What
		else if(words.get(0).equals(Q.WHAT.string)) {
			//getInfo
			if(words.contains("time")) {
				generatedReply = ""+ java.time.LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME).subSequence(0, 8);
			}else if(words.contains("date") || words.contains("day")){
				LocalDate date = LocalDate.now();
				DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("d MMM yyyy");
				generatedReply = ""+date.format(dateformat);
			}
			if(perspectiveIdentifier.personforms[persons.P1SINGULAR.index]>0 && perspectiveIdentifier.personforms[persons.P2.index]>0) {
				//has 1st person and 2nd person view point, what do you call me?
				generatedReply = "";
			}
			else if(perspectiveIdentifier.personforms[persons.P2.index]>0) {
				//has words..your/you
				if(words.contains("name")) {
					//what is your name?
					generatedReply = WHAT_MY_NAME;
				}
			}else if(perspectiveIdentifier.personforms[persons.P1SINGULAR.index]>0) {
				//has word me, refers to chatter, has my
				if(words.contains("name")) {
					//what is my name?
					String username = botmem.getValueFromLightMemory(ChatbotMemory.KEY_USERNAME);
					if(username.isBlank()) {
						generatedReply = "You never told me"; 
					}else {
						generatedReply = "Your name is " + username;
					}
				}
				String key1=null, key2=null;
				for(String word:LIKES) {
					//what do i like?
					if(words.contains(word)) {
						String like = botmem.getValueFromLightMemory(word);
						if(like.isBlank()) {
							generatedReply = "Tell me";
							key1 = word;
						}else {
							generatedReply = like;
						}
						break;
					}	
				}
				for(String word : LISTENWORDS) {
					if(words.contains(word)) {
						key2 = "verb."+word;//only verbs
						//key2 = sentenceIdentifier.wordsWithType.get(word)+"."+word; //supports all types
						break;
					}
				}
				if(key1!=null && key2!=null) {
					//what do i like to buy?
					ArrayList<String> information = botmem.getValuesFromDeepMemory(key1, key2);
					
					if(information !=null) {
						StringBuilder builder = new StringBuilder();
						for(String s : information) {
							builder.append(s); builder.append("& ");
						}
						generatedReply = builder.toString();
						generatedReply = generatedReply.substring(0, generatedReply.lastIndexOf("&"));
				    }else {
				    	generatedReply = "I don't know";
				    }
				}
			}
		}
		//sentence starts with Who
		else if(words.get(0).equals(Q.WHO.string)) {
			//getIdentity
			if(perspectiveIdentifier.personforms[persons.P2.index]>0) {
				//has word 'you'/'your'
				for(String d : devs) {//who is your creator?
					for(String word : words) {
						if(word.startsWith(d)) {
							generatedReply = WHO_DEVELOPED_ME;
							resolved = true;
						}
					}
				}
				if(sentenceIdentifier.wordsWithType.containsValue(Type.NOUN.string)){
					//who is _noun_, president... person.. object
					generatedReply = "Sorry I'm not in a level to understand that!";
					resolved = true;
				}
				if(!resolved) {
					//who are you
					generatedReply = WHOAMI;
				}
			}
			if(perspectiveIdentifier.personforms[persons.P1SINGULAR.index]>0) {
				//1st person
				//who am i? 
				generatedReply = botmem.getValueFromLightMemory(ChatbotMemory.KEY_USERNAME);
			}
		}//sentence starts with When
		else if(words.get(0).equals(Q.WHEN.string)) {
			//getTime of an event
		}//sentence starts with Where
		else if(words.get(0).equals(Q.WHERE.string)) {
			//a place
			String key1=null, key2=null;
			for(String word : LIKES) {
				if(words.contains(word)) {
					key1 = word;
					break;
				}
			}
			//String listenword = null;
			for(String word : LISTENWORDS) {
				if(words.contains(word)) {
					key2 = "verb."+word;//only verbs
					//key2 = sentenceIdentifier.wordsWithType.get(word)+"."+word; //supports all types
					//listenword = word;
					break;
				}
			}
			if(key1!=null && key2!=null) {
				//where do i like to go to?
				ArrayList<String> information = botmem.getValuesFromDeepMemory(key1, key2);
				if(information !=null) {
					StringBuilder builder = new StringBuilder();
					for(String s : information) {
						builder.append(s); builder.append("& ");
					}
					generatedReply = builder.toString();
					generatedReply = generatedReply.substring(0, generatedReply.lastIndexOf("&"));
			    }else {
			    	generatedReply = "I don't know";
			    }
			}
		}//sentence starts with Which
		else if(words.get(0).equals(Q.WHICH.string)) {
			//selection
		}//sentence starts with Do
		else if(words.get(0).equals(Q.DO.string)) {
			//something about chatter, load from preferences
			if(perspectiveIdentifier.personforms[persons.P1SINGULAR.index]>0) {
				//1st person only, about chatter
				//do you know what i like? do you know me?
			}
		}
		//sentence does NOT start with question words, but can have question mark in the end
		else {
			//check if greeting
			for(String word : greet) {
				if(words.contains(word)) {
					isGreeting = true;
					break;
				}
			}
			if(isGreeting) {
				generatedReply = "Hello!";
			}
			else if(perspectiveIdentifier.personforms[persons.P2.index]>0 && perspectiveIdentifier.personforms[persons.P1SINGULAR.index]>0) {
				//1st and 2nd person present
				//you know what i like?
			}else if(perspectiveIdentifier.personforms[persons.P1SINGULAR.index]>0) {
				//1st person only, about chatter
				//I like icecream, Tell me what i like?
				//String listenword = "";
				//I like to go to Atlantic city in Atlantic ocean
				String key1=null, key2=null;
				for(String word : LIKES) {
					if(words.contains(word)) {
						key1 = word;
						break;
					}
				}
				String listenword = null;
				for(String word : LISTENWORDS) {
					if(words.contains(word)) {
						key2 = "verb."+word;//only verbs
						//key2 = sentenceIdentifier.wordsWithType.get(word)+"."+word; //supports all types
						listenword = word;
						break;
					}
				}
				if(key1!=null && key2!=null) {
					//ArrayList<String> values = new ArrayList<String>();
					//from listenword to end, copy into arraylist
					String data = "";
					for(int i=(words.indexOf(listenword)+1);i<words.size();i++) {
						data += words.get(i) + " ";
					}
					botmem.memorizetodeepmem(key1, key2, data);
					botmem.save();
				}else {
					extractData = true;
				}
			}
			//3rd person handle?
		}
		//start extracting data from user to store in memory
		if(extractData && !getUserInput) {
			//identify noun, if many words are unknown then problem
			ArrayList<String> nouns = sentenceIdentifier.wordsForType.get(TypeOrder.NOUN.index);
			ArrayList<String> unknowns = sentenceIdentifier.wordsForType.get(TypeOrder.UNKNOWN.index);
			String whateveritis = "";
			//has capitalized words, could mean something
			for(int i=0;i<unknowns.size();i++) {
				if(Character.isUpperCase(unknowns.get(i).charAt(0))) {
					whateveritis += unknowns.get(i) + " ";
				}
			}
			Boolean exit = false;
			try {
				whateveritis = whateveritis.substring(0, whateveritis.length()-1);
			}catch (IndexOutOfBoundsException e) {
				exit = true;
			}
			if(whateveritis.isBlank() && !exit) {
				//if not in dictionary(unknown) and is not a Capitalized word then ignore?
				generatedReply = "Okay";
				extractData = false;
			}else if(words.contains("call") || words.contains("name")){
				//if words has 'call' or 'name'
				botmem.memorizetolightmem(ChatbotMemory.KEY_USERNAME, whateveritis);
				botmem.save();//save as preference
				extractData = false;
				generatedReply = "noted "+whateveritis + "!";
			}else {
				botmem.temporarymemorize(whateveritis);
				generatedReply = "Which is a?"; //we need a key from user
			}
		}
		getUserInput = false;//reset flag
		if(botmem.getPreviousReply()!="" && botmem.getPreviousReply().equals(generatedReply)) {
			generatedReply += "! You really like me to repeat huh";
		}
		botmem.setReply(generatedReply);
		
	}//end run
}
