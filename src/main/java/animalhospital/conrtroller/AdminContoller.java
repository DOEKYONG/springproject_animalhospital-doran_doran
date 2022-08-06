package animalhospital.conrtroller;

import animalhospital.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminContoller {

    @Autowired
    MemberService memberService;

    @GetMapping("/binlist")
    public String binlist() {
        return "admin/binlist";
    }

    @GetMapping("/getbinlist")
    public void getbinlist(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            response.getWriter().print(memberService.getbinlist());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/setrole")
    public void setrole(HttpServletResponse response, @RequestParam("mno") int mno,@RequestParam("hospital") String hospital,@RequestParam("bin") String bin) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(memberService.setrole(mno, hospital, bin));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/getchart")
    public String  getchart(){return "admin/chart";}

    @GetMapping("/getchartview")
    @ResponseBody
    public void getchartview(HttpServletResponse response) {
        try{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(memberService.todaycount());
        }catch (Exception e) {e.printStackTrace();}
    }
}