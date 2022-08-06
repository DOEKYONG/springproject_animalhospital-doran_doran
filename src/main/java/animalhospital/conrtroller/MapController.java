package animalhospital.conrtroller;

import animalhospital.dto.CrawlDto;
import animalhospital.dto.ReviewDto;
import animalhospital.service.BoardService;
import animalhospital.service.MapService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/map")
public class MapController {

    @Autowired
    private HttpServletRequest request;     // 1. 세션 호출을 위한 request 객체 생성

    @Autowired
    MapService mapService;

    @Autowired
    BoardService boardService;


    @GetMapping("/infopage")
    public String list(){ return "hospitalinfo";}


    @GetMapping("/view")
    @ResponseBody
    public String view(HttpServletResponse response, @RequestParam("hname") String hname, @RequestParam("hdate") String hdate,@RequestParam("hcity") String hcity
            ,@RequestParam("haddress") String haddress, @RequestParam("htel") String htel ,@RequestParam("lat") String lat ,@RequestParam("logt") String logt)
    {
        request.getSession().setAttribute("hname", hname);
        request.getSession().setAttribute("hdate", hdate);
        request.getSession().setAttribute("hcity",hcity);
        request.getSession().setAttribute("haddress",haddress);
        request.getSession().setAttribute("htel",htel);
        request.getSession().setAttribute("lat",lat);
        request.getSession().setAttribute("logt",logt);
        return hname;
    }

    @PostMapping("/info")
    @ResponseBody
    public void info(HttpServletResponse response ){
        String hname =  (String) request.getSession().getAttribute("hname");
        String hdate =  (String) request.getSession().getAttribute("hdate");
        String hcity = (String) request.getSession().getAttribute("hcity");
        String htel = (String) request.getSession().getAttribute("htel");
        String haddress = (String) request.getSession().getAttribute("haddress");
        String lat = (String) request.getSession().getAttribute("lat");
        String logt = (String) request.getSession().getAttribute("logt");

        System.out.println("크롤링에서뽑아온이름"+boardService.crawling(hcity,hname));
        CrawlDto crawlDto = boardService.crawling(hcity,hname);
        //String score = boardService.crawling(hcity,hname);
        System.out.println(hname);
        JSONObject object = new JSONObject();
        object.put("hname",hname);
        object.put("hdate",hdate);
        object.put("hcity",hcity);
        object.put("haddress",haddress);
        object.put("htel",htel);
        object.put("lat",lat);
        object.put("logt",logt);
        if(crawlDto.getScroe()==null) {
            object.put("score","다음 리뷰 없음");
            object.put("link","#");
        }else {
            object.put("score", crawlDto.getScroe());
            object.put("link", crawlDto.getLink());
        }
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
        }catch( Exception e ){
            System.out.println(  e   );
        }
    }


        @GetMapping("/search")
    public void search(HttpServletResponse response, @RequestParam("keyword") String keyword){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application.json");
            response.getWriter().println(mapService.search(keyword));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("/addreview" )
    @ResponseBody
    public boolean addreview(HttpServletResponse response, ReviewDto reviewDto){
        String hname =  (String) request.getSession().getAttribute("hname");
        String hdate =  (String) request.getSession().getAttribute("hdate");
        reviewDto.setHname(hname);
        reviewDto.setHdate(hdate);
        System.out.println("등록"+reviewDto);
        boolean result = mapService.addreview(reviewDto);
        return result;
    }

    @PostMapping("/getreviewlist")
    @ResponseBody
    public void getreviewlist(HttpServletResponse response, @RequestParam("hname") String hname , @RequestParam("hdate") String hdate  , @RequestParam("page") int page  ){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(mapService.getreviewlist(hname,hdate, page ));
        }catch( Exception e ){ System.out.println( e ); }
    }
    @PostMapping("/getreviewstarlist")
    @ResponseBody
    public void getreviewstarlist(HttpServletResponse response, @RequestParam("hname") String hname , @RequestParam("hdate") String hdate ){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(mapService.getreviewstarlist(hname,hdate ));
        }catch( Exception e ){ System.out.println( e ); }
    }

    @GetMapping("/getreview")
    public void getreview(HttpServletResponse response , @RequestParam("rno") int rno){
        System.out.println(rno);
        try{
            JSONObject object =  mapService.getreview( rno );
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print( object );
        }catch( Exception e ){ System.out.println( e ); }
    }


    @DeleteMapping("/rdelete")
    @ResponseBody
    public boolean rdelete(@RequestParam("rno") int rno ){return mapService.rdelete( rno );}

    @PostMapping("/updatereview" )
    @ResponseBody
    public boolean updatereview(HttpServletResponse response, ReviewDto reviewDto){boolean result = mapService.updatereview(reviewDto);return result;}
}