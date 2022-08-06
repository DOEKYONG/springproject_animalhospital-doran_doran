package animalhospital.conrtroller;

import animalhospital.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class NewsController {
    @Autowired
    NewsService newsservice;
    @GetMapping("/news")
    public String newsview() {

        return "news/newsview";
    }
    @GetMapping("/getnews")
    public void getnews(HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(   newsservice.getnews()  );
        }catch( Exception e){}
    }


}
