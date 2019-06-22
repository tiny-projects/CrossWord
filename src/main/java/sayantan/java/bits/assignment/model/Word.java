package sayantan.java.bits.assignment.model;

public class Word {

	private String value;
	private boolean horizontalOrientation;
	private String meaning;
	private int startingPosition; // like which column number or which row it is inserted
	private int cellPosition; //
	boolean isEntered;

	public Word(Word wordToBeEnteredHere) {
		this.value = wordToBeEnteredHere.value;
		this.meaning = wordToBeEnteredHere.meaning;
		this.startingPosition = wordToBeEnteredHere.startingPosition;
		this.cellPosition = wordToBeEnteredHere.cellPosition;
		this.isEntered = wordToBeEnteredHere.isEntered;
	}

	public Word() {
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isOrientationHorizontal() {
		return horizontalOrientation;
	}

	public void setOrientationHorizontal(boolean horizontalOrientation) {
		this.horizontalOrientation = horizontalOrientation;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public int getStartingPosition() {
		return startingPosition;
	}

	public void setStartingPosition(int startingPosition) {
		this.startingPosition = startingPosition;
	}

	public int getCellPosition() {
		return cellPosition;
	}

	public void setCellPosition(int cellPosition) {
		this.cellPosition = cellPosition;
	}

	public boolean isEntered() {
		return isEntered;
	}

	public void setEntered(boolean isEntered) {
		this.isEntered = isEntered;
	}

}
