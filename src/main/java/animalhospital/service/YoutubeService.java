package animalhospital.service;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import org.json.JSONArray;
import org.springframework.stereotype.Service;
@Service
public class YoutubeService {


    public String videosearch(String search) {
        try{
        String apiurl = "https://www.googleapis.com/youtube/v3/search";
        apiurl += "?key=AIzaSyD2KglODGU99HtA5co0gBPMo5iMDxnIA0Q";
        apiurl += "&part=snippet&type=video&maxResults=6&videoEmbeddable=true";
        apiurl += "&q="+ URLEncoder.encode(search,"UTF-8");

        URL url = new URL(apiurl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();

        return response.toString();
        }catch(Exception e){e.printStackTrace();}
        return null;
    }


}

