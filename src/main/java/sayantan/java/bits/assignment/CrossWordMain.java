package sayantan.java.bits.assignment;

import sayantan.java.bits.assignment.generator.CrossWordGenerator;

public class CrossWordMain {

	public static void main(String[] args) {

		// printWelcome();
		String filePath = "Dictionary.txt";
		CrossWordGenerator crossWordGen = new CrossWordGenerator();
		try {

			crossWordGen.createNew();
		} catch (Exception e) {
			System.out.println("*** [ERROR] : " + e.getLocalizedMessage());
			CrossWordGenerator.wordCounts = 30000;
		}

	}

}