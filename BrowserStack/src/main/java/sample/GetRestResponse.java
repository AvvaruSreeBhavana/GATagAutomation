package sample;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class GetRestResponse {
	
	public  String GetResponse(String testurl){
		
		
	  String USERNAME = "ibm6";
	  String AUTOMATE_KEY = "TE1aonBL13mDNQbJutCX";
	  String response="";
	   
	   HttpURLConnection connection=null;
    
   
    try {
       
    	URL url = new URL (testurl);
    	String encoding = Base64.getEncoder().encodeToString((USERNAME+":"+AUTOMATE_KEY).getBytes("UTF-8"));
         connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setRequestProperty  ("Authorization", "Basic " + encoding);
        System.out.println("connec::::"+connection);
        InputStream content = (InputStream)connection.getInputStream();
        BufferedReader in   = 
            new BufferedReader (new InputStreamReader (content));
        String line;
        while ((line = in.readLine()) != null) {
        	response+=line+"\n";
        	
        	
        }
            //System.out.println("response::::"+response);
            
            connection.disconnect();
            return  response;
             
        }
    catch(Exception e) {
        e.printStackTrace();
        connection.disconnect();
        return  response;
    }
	
   
	
	
	}

	/*public static void main(String[] args) throws Exception {
		GetResponse("https://api.browserstack.com/automate/sessions/306da1d445ceddfe238e59bf760c19f2c9dde578/networklogs");
}*/

}