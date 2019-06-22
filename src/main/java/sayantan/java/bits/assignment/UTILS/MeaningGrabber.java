package sayantan.java.bits.assignment.UTILS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MeaningGrabber {

	public static String path = "";
	public static String wordToFind = "";

	public static void main(String[] args) {

		findMeaning();
	}

	private static void findMeaning() {

		 wordToFind = "";
		String meaning = "";
		File wordListFile = new File("C:\\Users\\I354650\\Desktop\\usa(3).txt");
		File wordWithMeaningFile = new File("C:\\Users\\I354650\\Desktop\\Dictionary(4).txt");
		Scanner sc = null;
		OutputStream out = null;
		try {
			out = new FileOutputStream(wordWithMeaningFile);
			sc = new Scanner(wordListFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (sc.hasNextLine()) {
			wordToFind = sc.nextLine();
			try {
				meaning = getMeaning(wordToFind);
				if (meaning == "")
					continue;
				out.write(wordToFind.getBytes());
				out.write(" = ".getBytes());
				out.write(meaning.getBytes());
				out.write(".".getBytes());
				out.write("\n".getBytes());
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private static String getMeaning(String wordToFind) {
		String meaning = "";
		String responseJsonString = getRequest(
				"https://googledictionaryapi.eu-gb.mybluemix.net/?define=" + wordToFind + "&lang=en");
		if (responseJsonString == "")
			return "";
		try {
			JSONArray jsonArray = new JSONArray(responseJsonString);
			meaning = findDefinition(jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return meaning;
	}

	/*
	 * private static String findDefinition(JSONObject jsonArray) { String str = "";
	 * try { str = jsonArray.getJSONArray("results").getJSONObject(0).getJSONArray(
	 * "lexicalEntries").getJSONObject(0)
	 * .getJSONArray("entries").getJSONObject(0).getJSONArray("senses").
	 * getJSONObject(0) .getJSONArray("definitions").getString(0).toString(); }
	 * catch (JSONException e) { e.getCause().toString(); } return str;
	 * 
	 * }
	 */

	private static String findDefinition(JSONArray jsonArray) {
		String meaning = "";
		try {
			JSONObject objt = jsonArray.getJSONObject(0).getJSONObject("meaning");
			Iterator<String> keys = objt.keys();

			while (keys.hasNext()) {
				String key = keys.next();
				meaning += "(" + key + ") "+ objt.getJSONArray(key).getJSONObject(0).getString("definition").toString();
				if (keys.hasNext())
					meaning += ";";
			}
		} catch (Exception e) {
			System.out.println(wordToFind.toUpperCase()+" : "+e.getMessage());
		}
		return meaning;
	}

	private static String getRequest(String link) {
		try {
			URL url = new URL(link);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			BufferedReader reader = null;

			if (urlConnection.getResponseCode() == 404 || urlConnection.getResponseCode() == 403) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>Meaning Not Found For : "
						+ link.substring(link.lastIndexOf("/"), link.length() - 1).toUpperCase() + "("
						+ urlConnection.getResponseCode() + ") <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				return "";
			}

			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

}
