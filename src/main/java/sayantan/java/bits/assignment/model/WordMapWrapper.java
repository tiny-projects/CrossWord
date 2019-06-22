package sayantan.java.bits.assignment.model;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * Wrapper Class to store a word that is already existing in the table and a MAP
 * of the index of a character of this word that is matched with which index of
 * character of the <b>WordToBeInserted</b> if it is matched with multiple
 * character of the <b>WordToBeInserted</b>, multiple object is created .
 * </p>
 * <p>
 * I did not use list inside this class as this would make this class a
 * <u>hierarchical</u>, and it will be difficult to choose a random combination
 * when this classes object is used to store all possible crossing combination
 * when a new word to be inserted
 * </P>
 */
public class WordMapWrapper {

	private Word word;
	private Map<Integer, Integer> postionMap = new HashMap<>();

	public WordMapWrapper() {
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	public Map<Integer, Integer> getPostionMap() {
		return postionMap;
	}

	public void setPostionMap(int existingWordCharPosition, int wordToEnterCharacterPosition) {

		this.postionMap.put(existingWordCharPosition, wordToEnterCharacterPosition);
	}

	@Override
	public String toString() {
		return "WordMapWrapper [word=" + word + ", postionMap=" + postionMap + "]";
	}

}
