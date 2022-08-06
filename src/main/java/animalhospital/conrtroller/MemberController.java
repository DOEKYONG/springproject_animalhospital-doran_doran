package animalhospital.conrtroller;

import animalhospital.dto.RequestDto;
import animalhospital.dto.ShopDto;
import animalhospital.service.ItemService;
import animalhospital.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RequestMapping("/member")
@Controller
public class MemberController {

    @Autowired
    MemberService memberService;

    @Autowired
    ItemService itemService;

    @GetMapping("/login")
    public String login() {return "member/login";}

   @DeleteMapping("/delete")
   @ResponseBody
    public boolean delete() {
       return memberService.delete();
    }

    @GetMapping("/shop")
    public String shop() {return "member/shop";}

    @GetMapping("/memberinfo")
    public String memberinfo(){return "member/memberinfo";}

    @GetMapping("/request")
    public String request() {return "member/request";}

    @GetMapping("/getitem")
    @ResponseBody
    public void getitem(HttpServletResponse response,  @RequestParam("sno") int sno) {
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(itemService.getitem(sno));
        }catch(Exception e){e.printStackTrace();}
    }

    @GetMapping("/getitemlist")
    @ResponseBody
    public Map<String, List<Map<String, String>>> getitemlist(@RequestParam("page") int page) {
             return itemService.itemlist(page);
    }

    @PostMapping("/itemsave")
    @ResponseBody
    public boolean itemsave(ShopDto shopDto) {return itemService.itemsave(shopDto);}

    @PutMapping("/itemupdate")
    @ResponseBody
    public boolean itemupdate( ShopDto shopDto)
    {
        return itemService.itemupdate(shopDto);
    }

    @DeleteMapping("/deleteitem")
    @ResponseBody
    public boolean deleteitem(@RequestParam("sno") int sno) {
        return itemService.itemdelete(sno);
    }

    @GetMapping("/itemview{sno}")
    public String itemview(@PathVariable("sno") String sno) {return "member/itemview";}

    @GetMapping("/idcheck")
    @ResponseBody
    public int idcheck(@RequestParam("sno") int sno) {
        return itemService.idcheck(sno);
    }

    @GetMapping("/likesave")
    @ResponseBody
    public int likesave(@RequestParam("sno") int sno) {return itemService.likesave(sno);}

    @GetMapping("/likecheck")
    @ResponseBody
    public int likecheck(@RequestParam("sno") int sno) {return itemService.likecheck(sno);}

    @PostMapping("/requestsave")
    @ResponseBody
    public int requestsave (RequestDto requestDto, HttpServletResponse response) {
        int result = memberService.requestsave(requestDto);
        return result;
    }

    @GetMapping("/getinfo")
    @ResponseBody
    public void getinfo(HttpServletResponse response){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(memberService.getinfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/findhospital")
    @ResponseBody
    public void findhospital(HttpServletResponse response,@RequestParam("hospital") String hospital){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(memberService.findhospital(hospital));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/message")
    public String message(){ return "member/message";}

    @GetMapping("/gettomsglist")
    @ResponseBody
    public void gettomsglist(HttpServletResponse response, @RequestParam("type") int type){
        try {
            System.out.println("컨트롤러"+ type);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(memberService.gettomsglist(type));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @GetMapping("/getfrommsglist")
    public void getfrommsglist(HttpServletResponse response, @RequestParam("type") int type){
        try {
            System.out.println("컨트롤러"+ type);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(memberService.getfrommsglist(type));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @GetMapping("/getmid")
    @ResponseBody
    public String getmid(){
       return memberService.authenticationget();
    }
    @PutMapping("/isread") //5.
    @ResponseBody
    public boolean isread(@RequestParam ("msgno") int msgno){
        return memberService.isread(msgno);
    }

    @DeleteMapping("/msgdelete")
    @ResponseBody
    public boolean msgdelete(@RequestBody List<Integer> deletelist){
        return memberService.msgdelete(deletelist);
    }
}
