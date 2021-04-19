package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot;

public abstract class EnglishSentenceStructure {
	public EnglishSentenceStructure() {
	}
	//view point
	//1st person v=1,v=2 ,2nd person v=3, 3rd person v=4,v=5
	public enum persons{
		P1SINGULAR(0),P1PLURAL(1),P2(2),P3SINGULAR(3),P3PLURAL(4);
		public final int index;
		public static int size = 5;
		public int type() {return this.index;}
		persons(int p) {
			this.index = p;
		}
	}
	//question words
	public enum Q{
		WHAT("what"), WHERE("where"), WHEN("when"), WHICH("which"), WHO("who"),
		HOW("how"), DO("do");
		public final String string;
		public static int size = 6;
		public String type() {
			return this.string;
		}
		Q(String typeName) {
			this.string = typeName;
		}
	}
	//usage for: Type.QUESTION.string returns "Question"
	public enum Type{
		QUESTION("Question"),VERB("Verb"),DETERMINER("Determiner"),NOUN("Noun"),ADJECTIVE("Adjective"),ADVERB("Adverb"),
		PREPOSITION("Preposition"),PRONOUN("Pronoun"),PROVERB("Proverb"),PLURALNOUN("PluralNoun"),CONJUNCTION("Conjunction"),
		INTERJECTION("Interjection"),EXCLAMATION("Exclamation"),PUNCTUATION("Punctunation"), UNKNOWN("unx01");
		public final String string;
		public static int size = 15;
		public String type() {
			return this.string;
		}
		Type(String typeName) {
			this.string = typeName;
		}
	}
	//usage for: TypeOrder.QUESTION.index returns 0
	public enum TypeOrder{
		QUESTION(0),VERB(1),DETERMINER(2),NOUN(3),ADJECTIVE(4),ADVERB(5),PREPOSITION(6),PRONOUN(7),PROVERB(8),
		PLURALNOUN(9),CONJUNCTION(10),INTJECTION(11),EXCLAMATION(12),PUNCTUATION(13), UNKNOWN(14);
		public static int size = 15;
		public final int index;
		public int type() {
			return this.index;
		}
		TypeOrder(int typeName) {
			this.index = typeName;
		}
	}
	public static String[] wordtypes = {Type.QUESTION.string,Type.VERB.string,Type.DETERMINER.string,Type.NOUN.string,Type.ADJECTIVE.string,Type.ADVERB.string,
			Type.PREPOSITION.string,Type.PRONOUN.string,Type.PROVERB.string, Type.PLURALNOUN.string, Type.CONJUNCTION.string, Type.INTERJECTION.string,
			Type.INTERJECTION.string,Type.EXCLAMATION.string,Type.PUNCTUATION.string};
	public static final String[] PERSON1SINGULAR = {"i","me","myself","my"};
	public static final String[] PERSON1PLURAL = {"we","us"};
	public static final String[] PERSON2 = {"you","your","yourself"};
	public static final String[] PERSON3SINGULAR = {"he","his","him","her","hers","she","it"};
	public static final String[] PERSON3PLURAL = {"they","them","themselves","themself"};
	//gender
	public static final String[] MALE = {"him","he","his"};
	public static final String[] FEMALE = {"she","her","hers"};
	//common punctuation, simply
	public static final String[] PUNCTUATIONS = {".","!","?",",",";",":"};
	//2nd person models
	public static final String[] GENERICMODELS = {"You are $","I am $","Your name is $","Hello $"};//$ = anything
	//1st person models
	public static final String[] naming = {"call me $","my name is $","I am $"};
	//emotions
	public static final String[] happy = {"I'm feeling happy","Good day to you $","Yes $"};
	public static final String[] sad = {"bad","sad","worried","worry"};
	//listen to these words: set1 
	public static final String[] LIKES = {"like","want","wish"};
	//listen to these words: set2
	public static final String[] LISTENWORDS = {"go","dream","buy","get","have"};//for saving in deep memory
	//greet
	public static final String[] greet = {"hello","welcome","Hey","hi"};
	//about bot
	public static final String WHOAMI = "I am just a simple chatbot";
	public static final String WHAT_MY_NAME = "Yuno";
	public static final String WHO_DEVELOPED_ME = "A Bunch of SLIIT students";
	
	//developers/owners
	public String[] devs = {"master","owner","creat","developer","ma"};//creator, created, creators, made, maker
	//split sentence with whitespace as separator
	public static String[] splitSentence(String sent) {
		return sent.split("\\s+");
	}
}
