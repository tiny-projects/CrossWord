package sayantan.java.bits.assignment.generator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import sayantan.java.bits.assignment.UTILS.CrossWordUtils;
import sayantan.java.bits.assignment.model.Word;
import sayantan.java.bits.assignment.model.WordMapWrapper;

/**
 * <p>
 * <b>Cross Word Generator<b> to parse a dictionary file with words and meaning
 * an and randomly choosing certain number of words and generate purely random
 * and dynamic crossword table and with the clues vertically and horizontally
 * </p>
 * 
 * @author Sayantan Dey
 *
 */

// *****************************************************
//*    ____                 __        __            _  *
//*   / ___|_ __ ___  ___ __\ \      / /__  _ __ __| | *
//*  | |   | '__/ _ \/ __/ __\ \ /\ / / _ \| '__/ _` | *
//*  | |___| | | (_) \__ \__ \\ V  V / (_) | | | (_| | *
//*   \____|_|  \___/|___/___/ \_/\_/ \___/|_|  \__,_| *
//*												       *
//* ****************************************************
//                      

public class CrossWordGenerator {
	List<Integer> wordPositionAlreadyCovered = new ArrayList<Integer>();
	List<Word> wordList = new ArrayList<Word>();
	List<Word> filledWordList = new ArrayList<>();
	private static String filePath = "Dictionary.txt";
	public static int wordCounts;
	private static int horizontalCounter = 0;
	private static int verticalCounter = 0;
	private static char[][][] defaultTemplate = new char[17][17][4];
	private static char[][][] solutionBoard = new char[17][17][4];

	private static int totalWordEntered = 0;

	private void initializeTemplate() {

		for (int col = 0; col <= 16; col++) {
			if (col == 0) {
				defaultTemplate[0][0] = "GO".toCharArray();
				solutionBoard[0][0] = "GO".toCharArray();
				continue;
			}
			String placeholder = String.format("%2d", col);
			placeholder += " ";
			defaultTemplate[0][col] = placeholder.toCharArray();
			solutionBoard[0][col] = placeholder.toCharArray();

		}
		for (int row = 1; row <= 16; row++) {
			for (int col = 0; col <= 16; col++) {
				if (col == 0) {
					String placeholder = Integer.toString(row);
					placeholder = String.format("%2d", row);
					defaultTemplate[row][0] = placeholder.toCharArray();
					solutionBoard[row][0] = placeholder.toCharArray();
					continue;
				}
				defaultTemplate[row][col] = "[#]".toCharArray();
				solutionBoard[row][col] = "[.]".toCharArray();
			}
		}
		System.out.println("***[INFO] Template initialization complete");
	}

	public void createNew() {
		try {
			initializeTemplate();
			readRandomWords();
			generate();
			printCrossWord();
			printClue();
			printSolution();
		} catch (Exception e) {
			System.out.println("*** [ERROR] Error Creating New Game: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

	/** Print the meaning or explanation for the word (CLUE) **/
	private void printClue() {
		List<String> horizontalClues = new ArrayList<String>();
		List<String> verticalCLues = new ArrayList<String>();
		for (Word word : filledWordList) {
			if (!word.isEntered())
				continue;
			if (word.isOrientationHorizontal()) {
				horizontalClues.add(
						word.getValue() + " " + Integer.toString(word.getStartingPosition()) + " " + word.getMeaning());
			} else {
				verticalCLues.add(
						word.getValue() + " " + Integer.toString(word.getStartingPosition()) + " " + word.getMeaning());
			}
		}
		System.out.println("HORIZONTAL");
		System.out.println("______________");
		horizontalClues.forEach(System.out::println);
		System.out.println("VERTICAL");
		System.out.println("______________");
		verticalCLues.forEach(System.out::println);

	}

	/**
	 * Read Random words from a file and create a list of those word and their
	 * meaning
	 **/
	private void readRandomWords() {
		int position;
		InputStream is = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			wordCounts = CrossWordUtils.countLinesNew(is);
			System.out.println("Total words found " + wordCounts);
			while (wordPositionAlreadyCovered.size() <= 40) {
				position = ThreadLocalRandom.current().nextInt(1, wordCounts);
				// check whether the word is already taken
				while (wordPositionAlreadyCovered.indexOf(position) < 0) {
					position = ThreadLocalRandom.current().nextInt(1, wordCounts);
					wordPositionAlreadyCovered.add(position);
				}
				System.out.println("***[INFO] Word to be found at line " + position);
			}
			// Sorting the positions of the word
			Collections.sort(wordPositionAlreadyCovered);

			String line = "";
			int lineNum = 0;
			int iter = 0;
			int positionToBeFoundNext = wordPositionAlreadyCovered.get(iter++);
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null && iter <= 40) {
				if (lineNum == positionToBeFoundNext) {
					Word wordAtCeratainLineNumber = new Word();
					String[] wordArray = line.split("=");
					if (wordArray[0].length() > 16)
						continue;
					wordAtCeratainLineNumber.setValue(wordArray[0].trim().toUpperCase());
					wordAtCeratainLineNumber.setMeaning(wordArray[1]);
					wordList.add(wordAtCeratainLineNumber);
					positionToBeFoundNext = wordPositionAlreadyCovered.get(iter++);
					System.out.println(positionToBeFoundNext + "<----------");
				}
				lineNum++;
			}
			System.out.println("*** [INFO]: Word Reading Complete");
		} catch (Exception e) {
			System.out.println("****[ERROR] While Reading Dictionary to Fetch File, " + e.getMessage());
		}
	}

	/** Generate The Crossword by filling the template **/
	private void generate() {
		int wordCounter = 0;
		for (Word word : wordList) {
			if (wordCounter == 0)
				fillFirstWord(word);
			else {
//				System.out.println("***[INFO]: Now Filling Table For " + word.getValue());
				createWordCrossMapAndFillTable(word, wordCounter);
			}
			wordCounter++;
			if (totalWordEntered == 19)
				break;
		}
	}

	/**
	 * Create a map of the position where a already entered word and the given word
	 * crosses
	 **/
	private void createWordCrossMapAndFillTable(Word wordToEnter, int alreadyInsertedWordCount) {
		List<WordMapWrapper> wordMapWrapperList = new ArrayList<>();
		for (int itr = 0; itr < filledWordList.size(); itr++) {
			// System.out.println("***[INFO]: Iterating through Existing Word : " +
			// wordList.get(itr).getValue());
			/*-<  Crossing word by making it perpendicular to the word to be crossed with >- */
			Word existingWord = filledWordList.get(itr);
			Word wordToBeEntered = new Word(wordToEnter);
			if (existingWord.isOrientationHorizontal())
				wordToBeEntered.setOrientationHorizontal(false);
			else
				wordToBeEntered.setOrientationHorizontal(true);
			if (!isAnyCommonCharacter(existingWord, wordToBeEntered)) {
				System.out.println("***[INFO]: No Common character found");
				// Randomly deciding if this existing word will be cosidered for
				// entering the word
				if (ThreadLocalRandom.current().nextInt() % 2 == 0)
					continue;
				int randomPositon = ThreadLocalRandom.current().nextInt(0, 15);
				int count = 0;
				while (!checkInsertable(wordToBeEntered, randomPositon, 0)) {
					// Loop Through the position Until Successful
					randomPositon = ThreadLocalRandom.current().nextInt(0, 15);
					if (++count > 30)
						return;
				}

				wordToBeEntered.setCellPosition(0);
				fillWords(wordToBeEntered, randomPositon, 0);
				System.out.println("***[INFO] Success Entering Randomly at " + randomPositon);
				return;
			}

			/**
			 * >>>>>>>>> !!! CROSS FOUND <<<<<<<<<< <br/>
			 * New Word To Be Entered Is happened to be crossing the existing word, Hence a
			 * list of all possible crossing needs to be calculated and randomly one will be
			 * selected from the obtained list
			 */

			// System.out.println("***[INFO]: There are intersections, create cross map");
			for (int itr_word = 0; itr_word < existingWord.getValue().length(); itr_word++) {
				// Create a map for each first character of the existing word and where it
				// matches (with character) with the word to be inserted
				List<WordMapWrapper> tempWordMapWrapperList = new ArrayList<>();
				tempWordMapWrapperList = getCrossMapForTheCertaincharacter(itr_word + existingWord.getCellPosition(),
						wordToBeEntered, existingWord.getValue().charAt(itr_word), existingWord.getStartingPosition());
				if (!tempWordMapWrapperList.isEmpty())
					wordMapWrapperList.addAll(tempWordMapWrapperList);
			}
		}
		if (wordMapWrapperList.size() == 0) {
			return;
		}
		int randomInd = ThreadLocalRandom.current().nextInt(0, wordMapWrapperList.size());
		WordMapWrapper wrapper = wordMapWrapperList.get(randomInd);
		int key = (int) wrapper.getPostionMap().keySet().toArray()[0];
		int val = wrapper.getPostionMap().get(key);
		fillWords(wrapper.getWord(), key, val);

	}

	private boolean isAnyCommonCharacter(Word existingWord, Word wordToBeEntered) {
		char[] existingWordCharArray = existingWord.getValue().toCharArray();
		for (int charItr = 0; charItr < existingWord.getValue().length(); charItr++) {
			if (wordToBeEntered.getValue().indexOf(existingWordCharArray[charItr]) >= 0)
				return true;
		}
		return false;
	}

	/**
	 * Create the list of map( MAP<position of the given character of the existing
	 * word for which crossing to be found, position of the character in the word
	 * (that matched with it) needs to be entered
	 * 
	 * @param i
	 * 
	 * @returns <b>Map< </b> <i>index of character in existing word in table
	 *          <b>,</b> index(ices) of that character in new word</i> <b>></b>
	 */
	private List<WordMapWrapper> getCrossMapForTheCertaincharacter(int itr_word, Word word,
			char characterThatNeedsMatching, int startingPsitionOfExisitingWord) {
		List<WordMapWrapper> wordMapWrapperList = new ArrayList<>();
		String wordVal = word.getValue();
		int index = wordVal.indexOf(characterThatNeedsMatching);
		while (index >= 0) {

			// We will first check if this word can be crossed and entered here
			int startingPosition = startingPsitionOfExisitingWord - index;
			if (startingPosition < 0) {
				index = wordVal.indexOf(characterThatNeedsMatching, index + 1);
				continue;
			}
			if (!checkInsertable(word, itr_word, startingPosition)) {
				index = wordVal.indexOf(characterThatNeedsMatching, index + 1);
				continue;
			}
			WordMapWrapper wrapperObject = new WordMapWrapper();
			wrapperObject.setWord(word);
			wrapperObject.setPostionMap(itr_word, startingPosition);
			wordMapWrapperList.add(wrapperObject);
			index = wordVal.indexOf(characterThatNeedsMatching, index + 1);
		}
		return wordMapWrapperList;

	}

//NOTE:  position 0 indexed
	private boolean checkInsertable(Word word, int pos, int startingCellPosition) {
		if (startingCellPosition + word.getValue().length() > 16)
			return false;
		String wordVal = word.getValue();
		if (word.isOrientationHorizontal()) {
			for (int iterator = startingCellPosition, itr = 0; itr < word.getValue().length(); iterator++, itr++) {
				if (solutionBoard[pos][iterator][1] != '.' && solutionBoard[pos][iterator][1] != wordVal.charAt(itr))
					return false;
			}
		} else {
			for (int iterator = startingCellPosition, itr = 0; itr < word.getValue().length(); iterator++, itr++) {
				if (solutionBoard[iterator][pos][1] != '.' && solutionBoard[iterator][pos][1] != wordVal.charAt(itr))
					return false;
			}
		}
		return true;
	}

	private void fillFirstWord(Word word) {
		if (ThreadLocalRandom.current().nextInt() % 2 == 0)
			word.setOrientationHorizontal(true);
		else
			word.setOrientationHorizontal(false);
		int pos = ThreadLocalRandom.current().nextInt(1, 16);
		fillWords(word, pos, 1);
		// System.out.println("***[INFO] First Word Filled Successfully " +
		// word.getValue());
	}

	private void fillWords(Word word, int pos, int cellPosition) {
		String wordVal = word.getValue();
		if (word.isOrientationHorizontal())
			horizontalCounter++;
		else
			verticalCounter++;
		if (cellPosition == 0)
			return;
		if (cellPosition + wordVal.length() > 16)
			System.out.println(
					"This word ladies and gentlemen violated the condition and don\'t know how this sneaked in "
							+ wordVal);
		if (word.isOrientationHorizontal()) {
			for (int cellIndex = cellPosition, iterator = 0; iterator < wordVal.length(); cellIndex++) {
				defaultTemplate[pos][cellIndex][1] = ' ';
				solutionBoard[pos][cellIndex][1] = wordVal.charAt(iterator++);
			}
		//	defaultTemplate[pos][1][1] = Integer.toString(horizontalCounter).toCharArray()[0];
		} else {
			for (int cellIndex = cellPosition, iterator = 0; iterator < wordVal.length(); cellIndex++) {
				defaultTemplate[cellIndex][pos][1] = ' ';
				solutionBoard[cellIndex][pos][1] = wordVal.charAt(iterator++);
			}
			//defaultTemplate[1][pos][1] = Integer.toString(verticalCounter).toCharArray()[0];
		}
		word.setCellPosition(cellPosition);
		word.setStartingPosition(pos);
		word.setEntered(true);
		filledWordList.add(word);
		totalWordEntered++;
		// printSolution();

	}

	private void printCrossWord() {
		System.out.printf("\n\n\n\n\n");
		for (int row = 0; row <= 16; row++) {
			for (int col = 0; col <= 16; col++)
				System.out.print(defaultTemplate[row][col]);
			System.out.println();
		}
		System.out.printf("\n\n\n\n");
	}

	private void printSolution() {
		System.out.printf("\n\n\n\n\n");
		for (int row = 0; row <= 16; row++) {
			for (int col = 0; col <= 16; col++)
				System.out.print(solutionBoard[row][col]);
			System.out.println();
		}
		System.out.println("\n\n---------------TOTAL WORD COUNTS " + totalWordEntered + "  ----------------------");
		System.out.printf("\n\n\n\n\n");
	}

}
