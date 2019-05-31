package sample;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JavaSampleTest {

	// public static final String USERNAME = "kashyapdas1";
	// public static final String AUTOMATE_KEY = "y4NbhjQgNxt9ZdprsHpF";

	public static final String USERNAME = "sreebhavana1";
	public static final String AUTOMATE_KEY = "1sfzs1XRYTypbW813pUz";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	public static void main(String[] args) throws Exception {

		DesiredCapabilities caps = new DesiredCapabilities();
		/*
		 * caps.setCapability("browserName", "iPhone");
		 * caps.setCapability("device", "iPhone 8 Plus");
		 * caps.setCapability("realMobile", "true");
		 * caps.setCapability("os_version", "11");
		 */

		caps.setCapability("browser", "Chrome");
		caps.setCapability("browser_version", "73.0");
		caps.setCapability("os", "Windows");
		caps.setCapability("os_version", "10");
		caps.setCapability("resolution", "1920x1080");
		caps.setCapability("browserstack.debug", "true");
		caps.setCapability("browserstack.networkLogs", "true");
		caps.setCapability("browserstack.video", "true");
		caps.setCapability("browserstack.geoLocation", "US");
		caps.setCapability("browserstack.console", "verbose");

		GetRestResponse responseObj = new GetRestResponse();

		WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
		String urltoTest = "https://www.ibm.com/in-en";
		driver.get(urltoTest);
		String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
		((RemoteWebDriver) driver).executeScript("console.log('Test')");
		System.out.println(sessionId);
		driver.quit();
		System.out.println("Waiting for 2 secs to generate logs ");
		Thread.sleep(5000);
		String urlconsolelogs = "https://api.browserstack.com/automate/sessions/" + sessionId + "/consolelogs";
		String urlnetworklogs = "https://api.browserstack.com/automate/sessions/" + sessionId + "/networklogs";

		// System.out.println(responseObj.GetResponse(urlconsolelogs));
		// System.out.println(responseObj.GetResponse(urlnetworklogs ));

		String resObj = responseObj.GetResponse(urlnetworklogs);
		// System.out.println(resObj);
		JsonObject obj = new JsonParser().parse(resObj).getAsJsonObject();
		// System.out.println("jsonObject::: obj"+obj);
		// System.out.println("log: " + obj.getAsJsonObject("log"));
		JsonObject jobj = obj.getAsJsonObject("log");
		JsonArray jarr = jobj.getAsJsonArray("entries");
		// System.out.println("jarr::::"+jarr + "resobj:::" + resObj);
		JsonObject reqObj = null;
		JsonObject repObj = null;
		// JsonArray ujsArr = new JsonArray();

		for (JsonElement ja : jarr) {
			JsonObject jsobj = ja.getAsJsonObject();
			// System.out.println("reqobj:::"+jsobj.get("request").getAsJsonObject()
			// + "jarrr::"+jarr);
			reqObj = jsobj.get("request").getAsJsonObject();
			repObj = jsobj.get("response").getAsJsonObject();
			// System.out.println("url::"+reqObj.get("url"));
			// ujsArr.add(reqObj.get("url"));
			// System.out.println(reqObj.get("url").getAsString().indexOf("eluminate.js"));
			if (reqObj.get("url").getAsString().indexOf("https://data.coremetrics.com/cm?") != -1) {
				// System.out.println("responseObj:::"+repObj);
				System.out.println("queryString:::" + reqObj.get("queryString"));
				// System.out.println("reqobj:::"+jsobj.get("request").getAsJsonObject()
				// + "jarrr::"+jarr);

			}

			// System.out.println("repObj:::"+repObj + "jarr:::"+jarr);

		}

	}
}
