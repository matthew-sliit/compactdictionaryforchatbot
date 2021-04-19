package com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.runners;

import java.util.ArrayList;

import com.github.matthew_sliit.compactdictionaryforchatbot.serviceproviders.chatbot.EnglishSentenceStructure;

public class PerspectiveIdentifier extends EnglishSentenceStructure implements Runnable {
	public Boolean isQuestion = false, hasgender = false, gendermale = false, genderfemale;
	public int[] personforms;//1stsp or 2nd or 3rdsp
	ArrayList<String> words;
	public PerspectiveIdentifier(ArrayList<String> words) {
		this.words = new ArrayList<String>();
		this.words = words;
		personforms = new int[persons.size];
	}
	@Override
	public void run() {
		for(String word : words) {
			//1st person singular?
			for(int i=0;i<PERSON1SINGULAR.length;i++) {
				if(word.equals(PERSON1SINGULAR[i])) {
					personforms[persons.P1SINGULAR.index]++;
				}
			}
			//1sr person plural?
			for(int i=0;i<PERSON1PLURAL.length;i++) {
				if(word.equals(PERSON1PLURAL[i])) {
					personforms[persons.P1PLURAL.index]++;
				}
			}
			//2nd person?
			for(int i=0;i<PERSON2.length;i++) {
				if(word.equals(PERSON2[i])) {
					personforms[persons.P2.index]++;
				}
			}
			//3rd person singular?
			for(int i=0;i<PERSON3SINGULAR.length;i++) {
				if(word.equals(PERSON3SINGULAR[i])) {
					personforms[persons.P3SINGULAR.index]++;
				}
			}
			//3rd person plural?
			for(int i=0;i<PERSON3PLURAL.length;i++) {
				if(word.equals(PERSON3PLURAL[i])) {
					personforms[persons.P3PLURAL.index]++;
				}
			}
			//has gender? is male?
			for(int i=0;i<MALE.length;i++) {
				if(word.equals(MALE[i])) {
					hasgender = true;
					gendermale = true;
				}
			}
			//has gender? is female?
			for(int i=0;i<FEMALE.length;i++) {
				if(word.equals(FEMALE[i])) {
					hasgender = true;
					genderfemale = true;
				}
			}
		}
	}

}
