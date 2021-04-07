package worddict;

import java.util.ArrayList;

import worddict.commons.DictionaryException;
import worddict.service.WordDictionary;

public class EnglishDictionary implements WordDictionary {

	@Override
	public void addNewWord(String word, String type, String meaning) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void Commit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeWord(String word) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWordMeaning(String word) throws DictionaryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getAllWords() throws DictionaryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean hasWord(String word) throws DictionaryException {
		// TODO Auto-generated method stub
		return null;
	}

}
