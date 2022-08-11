package animalhospital.conrtroller;

import animalhospital.dto.HospitalDto;
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


    HospitalDto hospitalDto = new HospitalDto();


    @GetMapping("/infopage")
    public String list(){ return "hospitalinfo";}
    @GetMapping("/view")
    @ResponseBody
    public void view(HttpServletResponse response, @RequestParam("hname") String hname, @RequestParam("hdate") String hdate,@RequestParam("hcity") String hcity
            ,@RequestParam("haddress") String haddress, @RequestParam("htel") String htel ,@RequestParam("lat") String lat ,@RequestParam("logt") String logt)
    {
        hospitalDto.setHname(hname);
        hospitalDto.setHdate(hdate);
        hospitalDto.setHcity(hcity);
        hospitalDto.setHaddress(haddress);
        hospitalDto.setHtel(htel);
        hospitalDto.setLat(lat);
        hospitalDto.setLogt(logt);
    }

    @PostMapping("/info")
    @ResponseBody
    public void info(HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(boardService.crawling(hospitalDto));
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