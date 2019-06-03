package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class RequestNetworkLogs {

	public static final String USERNAME = "ibm6";
	public static final String AUTOMATE_KEY = "TE1aonBL13mDNQbJutCX";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	public static void main(String[] args) throws Exception {

		DesiredCapabilities caps = new DesiredCapabilities();

		caps.setCapability("browser", "Firefox");
		caps.setCapability("browser_version", "66.0.5");
		caps.setCapability("os", "Windows");
		caps.setCapability("os_version", "10");
		caps.setCapability("resolution", "1024x768");
		caps.setCapability("browserstack.debug", "true");
		caps.setCapability("browserstack.networkLogs", "true");
		caps.setCapability("browserstack.video", "true");
		caps.setCapability("browserstack.geoLocation", "IN");
		caps.setCapability("browserstack.console", "verbose");

		GetRestResponse responseObj = new GetRestResponse();

		WebDriver driver = new RemoteWebDriver(new URL(URL), caps);

		// String urltoTest = "https://www.ibm.com/in-en";
		// String urltoTest = "https://googleanalytics2019.mybluemix.net";
		String urltoTest = "https://googleanalyticspoc-zany-reedbuck.eu-gb.mybluemix.net";
		driver.get(urltoTest);
		String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
		((RemoteWebDriver) driver).executeScript("console.log('Test')");
		System.out.println(sessionId);
		Thread.sleep(10000);

		driver.quit();
		System.out.println("Waiting for 2 secs to generate logs ");
		Thread.sleep(10000);

		String urlconsolelogs = "https://api.browserstack.com/automate/sessions/" + sessionId + "/consolelogs";
		String urlnetworklogs = "https://api.browserstack.com/automate/sessions/" + sessionId + "/networklogs";

		String resObj = responseObj.GetResponse(urlnetworklogs);
		System.out.println(resObj);

		JSONObject output = new JSONObject(resObj);
		JSONObject logObj = output.getJSONObject("log");
		JSONArray docs = logObj.getJSONArray("entries");

		System.out.println("docs:::" + docs);

		JSONArray jsonArray = new JSONArray();

		JSONObject object = null;
		JSONArray jsArr = null;
		JSONObject qsob = null;

		for (int n = 0; n < docs.length(); n++) {
			object = (JSONObject) docs.getJSONObject(n).get("request");
			JSONObject resObj1 = (JSONObject) docs.getJSONObject(n).get("response");
			JSONObject object1 = new JSONObject();
			JSONObject object2 = new JSONObject();

			jsArr = (JSONArray) object.get("queryString");

			// System.out.println("objectlllll::::"+object.get("url").toString().indexOf("https://www.google-analytics.com/"));
			if (object.get("url").toString().indexOf("https://www.google-analytics.com/r/collect") != -1) {
				System.out.println("qs::::" + object.get("queryString").toString());
				System.out.println("timee::::::::" + docs.getJSONObject(n).get("time"));
				System.out.println("size:::::::::::::::" + resObj1.get("bodySize"));

				object1.put("Url", object.get("url"));
				// object1.put("QueryString",object.get("queryString"));
				object1.put("Timings", docs.getJSONObject(n).get("time"));
				object1.put("Size", resObj1.get("bodySize"));
				for (int n1 = 0; n1 < jsArr.length(); n1++) {
					qsob = jsArr.getJSONObject(n1);
					object1.put((String) qsob.get("name"), jsArr.getJSONObject(n1).get("value"));
					// System.out.println("qsObj name::"+
					// jsArr.getJSONObject(n1).get("name"));
					System.out.println("qsname:::::::::::" + qsob.get("name"));
					System.out.println("qsObj val::" + jsArr.getJSONObject(n1).get("value"));

				}
				jsonArray.put(object1);

			}
		}

		// System.out.println("jsonArray:::test:::"+jsonArray);
		try {
			File file = new File("/opal/POCReportFile.csv");
			System.out.println("jsonArray::::::" + jsonArray);
			String csv = CDL.toString(jsonArray);
			FileUtils.writeStringToFile(file, csv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
