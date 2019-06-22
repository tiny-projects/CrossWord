package sayantan.java.bits.assignment.UTILS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

public class CouponParser {

	public static void main(String[] args) {

		findCoupon1();
	}

	private static void findCoupon1() {
		String coupn = "";
		String outPutHtml;
		String couponCodeNavId = "data-gtm-voucher-id";
		int lengthOfID = couponCodeNavId.length() + 2;
		File inputFile = new File("C:\\Users\\I354650\\Desktop\\CouponSiteList.txt");
		File outputFile = new File("C:\\Users\\I354650\\Desktop\\Coupons.txt");
		Scanner sc = null;
		OutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
			sc = new Scanner(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<String> codeList = new ArrayList<String>();
		while (sc.hasNextLine()) {
			coupn = sc.nextLine();
			codeList.clear();
			try {
				outPutHtml = findListOfCoupon(coupn);
				// List<String> couponIdList = new ArrayList<String>();
				int index = 0;
				String heading = coupn + ":\n";
				out.write(heading.getBytes());
				int id = 0;
				while ((index = outPutHtml.indexOf(couponCodeNavId, index)) > 0) {
					int indexOfId = index + lengthOfID;
					index = indexOfId;
					String idAsString = "";
					idAsString = outPutHtml.substring(indexOfId, indexOfId + 6);
					try {
						if (Integer.parseInt(idAsString) != id)
							id = Integer.parseInt(idAsString);
						JSONObject response = null;
						response = getCouponDetails(id);
						String code = response.getJSONObject("voucher").getString("code");
						System.out.println(">>>>>>>>>>[code] " + code);
						if (code == "" || code == null || codeList.indexOf(code) != -1 || code == "OFFER ACTIVATED")
							continue;
						codeList.add(code);
						String retailer = response.getJSONObject("voucher").getString("retailer");
						String title = response.getJSONObject("voucher").getString("title");
						out.write("\t\t\t\t[".getBytes());
						out.write(code.getBytes());
						out.write("] - ".getBytes());
//						out.write("retailer \"".getBytes());
//						out.write(retailer.getBytes());
//						out.write("\" , ".getBytes());
//						out.write("voucher \"".getBytes());
						out.write(title.getBytes());
						out.write("\"\n".getBytes());
						out.flush();
					} catch (NumberFormatException e) {
						System.out.println("*******> " + idAsString);
						continue;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private static JSONObject getCouponDetails(int id) {
		try {
			String link = "https://coupons.oneindia.com/ajax/voucherpopup?id=" + id;
			System.out.println("Searching now for - " + link);
			URL url = new URL(link);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			BufferedReader reader = null;

			if (urlConnection.getResponseCode() == 404 || urlConnection.getResponseCode() == 403) {
				System.out.println("--------------------->Coupon Voucher Code Not Found For : "
						+ link.substring(link.lastIndexOf("/"), link.length() - 1).toUpperCase() + "("
						+ urlConnection.getResponseCode() + ") <------------//");
				System.out.println("OOPS " + id);
				return null;
			}

			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			String responseJson = stringBuilder.toString();
			JSONObject responseObject = new JSONObject(responseJson);
			return responseObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String findListOfCoupon(String coupn) {
		try {
			String link = "https://coupons.oneindia.com/" + coupn + "-coupons";
			URL url = new URL(link);
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			BufferedReader reader = null;

			if (urlConnection.getResponseCode() == 404 || urlConnection.getResponseCode() == 403) {
				System.out.println("--------------------->Coupon Not Found For : "
						+ link.substring(link.lastIndexOf("/"), link.length() - 1).toUpperCase() + "("
						+ urlConnection.getResponseCode() + ") <------------//");
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
