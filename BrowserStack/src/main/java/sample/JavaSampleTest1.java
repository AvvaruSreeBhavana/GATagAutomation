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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JavaSampleTest1 {

	public static final String USERNAME = "ibm6";
	public static final String AUTOMATE_KEY = "TE1aonBL13mDNQbJutCX";
	public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

	public static void main(String[] args) throws Exception {

		DesiredCapabilities caps = new DesiredCapabilities();

		caps.setCapability("browser", "Chrome");
		caps.setCapability("browser_version", "74.0");
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
		//String urltoTest = "https://www.ibm.com/in-en";
		//String urltoTest = "https://googleanalytics2019.mybluemix.net";
		String urltoTest = "https://googleanalyticspoc-zany-reedbuck.eu-gb.mybluemix.net";
		driver.get(urltoTest);
		String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
		((RemoteWebDriver) driver).executeScript("console.log('Test')");
		System.out.println(sessionId);
	    Thread.sleep(5000);

		driver.quit();
		System.out.println("Waiting for 2 secs to generate logs ");
	    Thread.sleep(10000);

		String urlconsolelogs = "https://api.browserstack.com/automate/sessions/" + sessionId + "/consolelogs";
		String urlnetworklogs = "https://api.browserstack.com/automate/sessions/" + sessionId + "/networklogs";

		String resObj = responseObj.GetResponse(urlnetworklogs);
		//System.out.println(resObj);
		
		JsonObject obj = new JsonParser().parse(resObj).getAsJsonObject();
		
		//System.out.println("gson obj::::"+obj);
		
		/*JsonObject jobj = obj.getAsJsonObject("log");
		JsonArray jarr = jobj.getAsJsonArray("entries");
		JsonObject reqObj = null;
		JsonObject repObj = null;
		
		
		System.out.println("json obj:::"+jobj);*/
		
		JSONObject output =  new JSONObject(resObj);
		JSONObject logObj = output.getJSONObject("log");
        JSONArray docs = logObj.getJSONArray("entries");
        
        
        //System.out.println("docs:::"+docs);
        JSONObject reqObj1 = null;
        JSONObject repObj1= null;
		try{
      	  File file=new File("/tmp2/fromJSON2.csv");
            String csv = CDL.toString(docs);
            FileUtils.writeStringToFile(file, csv);
        }
		 catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }  
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("arr1", docs);
		System.out.println("jsonarray:::"+jsonobj);
		System.out.println("reqqqq:::"+jsonobj.get("arr1"));
		for(int n = 0; n < docs.length(); n++)
		{
		    JSONObject object = (JSONObject) docs.getJSONObject(n).get("request");
		    
		    //System.out.println("objectlllll::::"+object.get("url").toString().indexOf("https://www.google-analytics.com/"));
		    if(object.get("url").toString().indexOf("https://www.google-analytics.com/") != -1){
		    	System.out.println("qs::::"+object.get("queryString"));
		    }
		}
		/*for (JsonElement ja : jarr) {
			
			JsonObject jsobj = ja.getAsJsonObject();
			reqObj = jsobj.get("request").getAsJsonObject();

			repObj = jsobj.get("response").getAsJsonObject();
			if (reqObj.get("url").getAsString().indexOf("https://www.google-analytics.com/") != -1) {
				System.out.println("queryString:::" + reqObj.get("queryString"));
              
			}*/

		}

	}
