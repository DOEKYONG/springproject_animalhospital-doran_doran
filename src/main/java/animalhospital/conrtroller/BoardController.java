package animalhospital.conrtroller;

import animalhospital.dto.BoardDto;
import animalhospital.service.BoardService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private HttpServletRequest request;


    @GetMapping("/list")
    public String list(){ return "board/mypetlist";}

    @GetMapping("/tiplist")
    public String tiplist(){ return "board/tiplist";}

    @GetMapping("/tipview/{bno}")
    public String tipview( @PathVariable("bno") int bno){
        request.getSession().setAttribute("bno", bno);
        return "board/tipview";
    }


    @PostMapping("/write")
    @ResponseBody
    public boolean write_save( BoardDto boardDto ){
        boardDto.setCno(2);
        boardDto.setBcontent(boardDto.getBcontent().replace("\r\n", "<br>"));
        boolean result = boardService.save( boardDto );
        return result;
    }
    @PostMapping("/tipwrite")
    @ResponseBody
    public boolean tipwrite_save( BoardDto boardDto ){
        boardDto.setCno(3);
        boardDto.setBcontent(boardDto.getBcontent().replace("\r\n", "<br>"));
        boolean result = boardService.save( boardDto );

        return result;
    }

    @PutMapping("/tipupdate")
    @ResponseBody
    public boolean tipwrite_update( BoardDto boardDto ){
        boardDto.setCno(3);
        boardDto.setBcontent(boardDto.getBcontent().replace("\r\n", "<br>"));
        int bno =  (Integer) request.getSession().getAttribute("bno");
        boardDto.setBno(bno);
        boolean result = boardService.bupdate( boardDto );

        return result;
    }

    @PutMapping("/mypetupdate")
    @ResponseBody
    public boolean mypetupdate( BoardDto boardDto ){
        boardDto.setCno(2);
        boardDto.setBcontent(boardDto.getBcontent().replace("\r\n", "<br>"));
        int bno =  (Integer) request.getSession().getAttribute("bno");
        boardDto.setBno(bno);
        boolean result = boardService.bupdate( boardDto );

        return result;
    }


    @PostMapping("/blist")
    @ResponseBody
    public Map< String , List<Map<String , String >>>
    blist(HttpServletResponse response, @RequestParam("page") int page ){
        return boardService.boardlist(page);
    }

    @PostMapping("/btiplist")
    @ResponseBody
    public Map< String , List<Map<String , String >>>
    btiplist(HttpServletResponse response, @RequestParam("page") int page ){
        return boardService.boartiplist(page);
    }

    @GetMapping("/getboard")
    public void getboard( @RequestParam("bno") int bno ,
                          HttpServletResponse response  ){
        request.getSession().setAttribute("bno", bno);
        try{
            JSONObject object =  boardService.getboard( bno );
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print( object );
        }catch( Exception e ){ System.out.println( e ); }

    }

    @DeleteMapping("/bdelete")
    @ResponseBody
    public boolean delete(@RequestParam("bno") int bno ){
        System.out.println(bno);
        return boardService.delete( bno );
    }

    @PostMapping("/search")
    @ResponseBody
    public void videosearch(@RequestParam("search") String search
            , HttpServletResponse response )
    {
        try{


        }catch( Exception e ){ System.out.println( e ); }
    }

    @PostMapping("/replysave")
    @ResponseBody
    public boolean replysave(@RequestParam("bno") int bno, @RequestParam("reply") String reply){
        return boardService.replysave( bno, reply );
    }

    @GetMapping("/getreply")
    @ResponseBody
    public void getreply(@RequestParam("bno")int bno, HttpServletResponse response){
        try {
            System.out.println("getreplycc");
            JSONArray jsonArray = boardService.getreply(bno);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        } catch (IOException e) {
            System.out.println("getreply error : "+e);
        }
    }

    @GetMapping("/replydelete")
    @ResponseBody
    public boolean replydelete(@RequestParam("rno") int rno){
        System.out.println("replydelete : " + rno);
        return boardService.replydelete(rno);
    }
    @GetMapping("/replyupdate")
    @ResponseBody
    public void replyupdate(@RequestParam("rno") int rno,HttpServletResponse response){
        try {
            JSONObject object = boardService.replyupdate(rno);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
//            System.out.println("replyupdate : " + object);
        } catch (IOException e) {
            System.out.println("replyupdate error : "+e);
        }
    }
    @PostMapping("/reupdate")
    @ResponseBody
    public boolean reupdate(@RequestParam("rno") int rno, @RequestParam("reply") String reply){
        return boardService.reupdate( rno, reply );
    }

    @PostMapping("/rereply")
    @ResponseBody
    public boolean rereply(@RequestParam("bno") int bno, @RequestParam("rindex") int rindex, @RequestParam("reply") String reply){
        return boardService.rereplysave(bno, rindex, reply );
    }
    @GetMapping("/getrereply")
    @ResponseBody
    public void getrereply(@RequestParam("bno")int bno,@RequestParam("rindex") int rindex, HttpServletResponse response){
        try {
            JSONArray jsonArray = boardService.getrereply(bno,rindex);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(jsonArray);
        } catch (IOException e) {
            System.out.println("getrereply error : "+e);
        }
    }
    @GetMapping("/gettipboard")
    public void gettipboard( HttpServletResponse response){
        int bno =  (Integer) request.getSession().getAttribute("bno");

        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(boardService.getboard(  bno   ));
        }catch( Exception e ){
            System.out.println( e );
        }
    }
}