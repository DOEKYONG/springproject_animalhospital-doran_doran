package animalhospital.conrtroller;

import animalhospital.service.YoutubeService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
public class YouTubeController {

    @Autowired
    YoutubeService youtubeService;



    @GetMapping("/videosearch")
    public void videosearch(@RequestParam("search") String text, HttpServletResponse response){

        try{
            JSONObject jo = new JSONObject(youtubeService.videosearch(text));
           response.setCharacterEncoding("UTF-8");
           response.setContentType("application/json");
           response.getWriter().print(jo);
        }catch(Exception e){e.printStackTrace();}

    }
}
