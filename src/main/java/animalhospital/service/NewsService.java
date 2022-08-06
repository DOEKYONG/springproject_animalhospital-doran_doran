package animalhospital.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NewsService {

    public  JSONArray getnews(){
        String url = "https://search.naver.com/search.naver?where=news&sm=tab_jum&query=%EB%B0%98%EB%A0%A4%EB%8F%99%EB%AC%BC"; // 1.
        Connection connect =  Jsoup.connect(url); // 2.
        try {
            Document document = connect.get();
            Elements elements = document.getElementsByClass("news_tit").select("a");
            Elements elements2 = document.getElementsByClass("dsc_wrap").select("a");
            Elements elements3 = document.getElementsByClass("dsc_thumb ");

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < 8; i++ ) {
                JSONObject object = new JSONObject();
                String title = elements.get(i).text();
                String content = elements2.get(i).text();
                String nlink = elements3.get(i).attr("href");
                String imgurl = elements3.get(i).select("img").attr("src");
                object.put("title",title);
                object.put("content",content);
                object.put("nlink",nlink);
                object.put("imgurl",imgurl);
                jsonArray.put(object);
            }

//            System.out.println(imgurl);
//            System.out.println(nlink);
            return jsonArray;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}