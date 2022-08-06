package animalhospital.conrtroller;

import animalhospital.service.BoardService;
import animalhospital.service.MapService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@Controller
public class test { // 인덱스 컨트롤러 및 관리자 컨트롤러로 사용
    @Autowired
    BoardService boardService;

    @Autowired
    HttpServletResponse response;

    @Autowired
    MapService mapService;



    @GetMapping("/")
    public String main( Model model ){

        return "main";
    }

    @GetMapping("/board/notice")
    public String notice(){ return "admin/adminnotice";}

    @GetMapping("/special/specialanimal")
    public String special(){ return "special/specialanimal";}

    @GetMapping("/special/adminmemberlist")
    public String adminmemberlist() {return "special/adminmemberlist";}
    /* 관리자 로그인을 UserDetailService에서 처리 예정
    @PostMapping("/member/adminlogincontroller")
    @ResponseBody
    public ResponseEntity adminlogin(@RequestParam("mid") String mid, @RequestParam("mpassword") String mpassword) {
        boolean result = memberService.adminlogin(mid, mpassword);
        if(result == true) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
        else {
            return null;
        }

    }
    */
    @GetMapping("/special/videosearch")
    public String videosearch(){ return "special/videosearch";}
    @PostMapping("/board/getnotice")
    @ResponseBody
    public JSONObject getnoticelist(@RequestParam("page") int page) {

        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            JSONObject js = boardService.getnoticelist(page);
            response.getWriter().print(js);
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
    @PostMapping("/admin/noticesave")
    @ResponseBody
    public boolean noticesave(@RequestParam("btitle") String btitle, @RequestParam("bcontent") String bcontent ) {
        return boardService.noticesave(btitle, bcontent);
    }
    @DeleteMapping("/admin/deletenotice")
    @ResponseBody
    public boolean noticedelete(@RequestParam("bno") int bno) {return boardService.noticedelete(bno);}

    @PutMapping("/admin/updatenotice")
    @ResponseBody
    public boolean noticeupdate(@RequestParam("bno") int bno, @RequestParam("btitle") String btitle
            , @RequestParam("bcontent") String bcontent)
    {return boardService.noticeupdate(bno, btitle, bcontent);
    }
    @GetMapping("/getlist")
    @ResponseBody
    public void getlist(@RequestParam("codenamelist") JSONArray codenamelist){
        //  String result = code +" "+ name;
        System.out.println(  "json : " + codenamelist.getJSONObject(0).get("code"));


        //String codename[] = new String[count];
        //System.out.println(count);
        //  codename[0] = result;
        //  System.out.println(result);

    }
    @GetMapping("/map")
    @ResponseBody
    public void gethlist(HttpServletResponse response)  {
        JSONArray list =  mapService.map();

        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(list);
        } catch(Exception e) {}
    }

//    @GetMapping("/crwal")
//    @ResponseBody
//    public JSONArray getcrwal() {
//        JSONArray list = mapService.map();
//        // JSONObject o = new JSONObject();
//        //System.out.println(list);
//        String city;
//        String name;
//        for(int i =0; i<list.length(); i++) {
//            try {
//                city = list.getJSONObject(i).get("city").toString();
//                //System.out.println(city);
//                name = list.getJSONObject(i).get("name").toString();
//                // System.out.println(name);
//                boardService.crawling(city,name);
//
//
//            }catch(Exception e){System.out.println("dd"+ e);}
//
//        }
//
//        return null;
//    }


}
