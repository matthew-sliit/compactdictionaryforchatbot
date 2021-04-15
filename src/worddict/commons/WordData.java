package worddict.commons;

import java.util.ArrayList;

public class WordData {
	public WordData() {
		
	}
	public WordData(String word, String type, String meaning, ArrayList<String> synonymns) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.meaning = meaning;
		this.synonymns = synonymns;
	}
	public String type=null;
	public String meaning=null;
	public ArrayList<String> synonymns=new ArrayList<String>();
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return type+"."+meaning;
	}
	public ArrayList<String> getSynSet() {
		return this.synonymns;
	}

	public void addSyn(String synonym){
		this.synonymns.add(synonym);
	}
	
	public void removeSyn(String synonym){
		this.synonymns.remove(synonym);
	}
}
