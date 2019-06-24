package sayantan.java.bits.assignment.graphics;

public class DisplayMessages {

	private static final String _30Spaces = "                              ";
	private static final String _14Spaces = "              ";

	public void printWelcome() {
		System.out.println(_30Spaces + "*******************************************************");
		System.out.println();
		System.out.println(_30Spaces + _14Spaces + "Welcome To  C R O S S W O R D S");
		System.out.println();
		System.out.println(_30Spaces + "*******************************************************");

	}

	public void printMenu() {

		System.out.println("---------------");
		System.out.println("| 1. New Game  |");
		System.out.println("|--------------|");
		System.out.println("| 2.  Exit     |");
		System.out.println("---------------");

	}
	
	public void askConfirmationToShowAnswer()
	{
		System.out.println("  PRESS 5 to Show Answer");
		System.out.println("  PRESS 2 To Exit");
	}
}
