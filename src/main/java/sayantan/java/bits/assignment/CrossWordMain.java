package sayantan.java.bits.assignment;

import java.util.Scanner;

import sayantan.java.bits.assignment.generator.CrossWordGenerator;
import sayantan.java.bits.assignment.graphics.DisplayMessages;

public class CrossWordMain {

	public static void main(String[] args) {

		// printWelcome();
		String filePath = "Dictionary.txt";
		CrossWordGenerator crossWordGen = new CrossWordGenerator();
		DisplayMessages message = new DisplayMessages();
		int choiceNumber = 0;
		Scanner sc = new Scanner(System.in);
		try {
			while (true) {

				message.printWelcome();
				message.printMenu();
				while (true) {
					try {
						choiceNumber = Integer.parseInt(sc.next());
						//sc.close();
						if (choiceNumber == 1 || choiceNumber == 2)
							break;
						System.out.println(" [ALERT] : Please Enter a Valid Number");
					} catch (NumberFormatException e) {
						System.out.println(" [ALERT] : Enter Numeric Value Only");
					}
				}
				if (choiceNumber == 2)
					break;
				crossWordGen.createNew();
				message.askConfirmationToShowAnswer();
				choiceNumber = sc.nextInt();
				while (true) {
					if (choiceNumber == 5) {
						crossWordGen.printSolution();
						break;
					} else if (choiceNumber == 2)
						return;
					System.out.println("   [ALERT] : Enter a valid option");
				}
			}
		} catch (

		Exception e) {
			System.out.println("*** [ERROR] : " + e.getLocalizedMessage());
			CrossWordGenerator.wordCounts = 30000;
		}

	}

}