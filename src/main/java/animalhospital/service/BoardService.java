package animalhospital.service;

import animalhospital.domain.board.*;
import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.dto.BoardDto;
import animalhospital.dto.CrawlDto;
import animalhospital.dto.OauthDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BoardService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardimgRespository boardimgRespository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private MemberService memberService;

    @Transactional
    public boolean save(BoardDto boardDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String mid = null;
        if( principal instanceof UserDetails){
            mid = ((UserDetails) principal).getUsername();
        }else if( principal instanceof DefaultOAuth2User){
            Map<String , Object>  map =  ((DefaultOAuth2User) principal).getAttributes();
            if( map.get("response") != null ){
                Map< String , Object> map2  = (Map<String, Object>) map.get("response"); // 네이버
                mid = map2.get("email").toString().split("@")[0];
            }else if(map.get("kakao_account") != null){
                Map< String , Object> map2  = (Map<String, Object>) map.get("kakao_account"); // 카카오
                mid = map2.get("email").toString().split("@")[0];
            }else if(map.get("kakao_account") == null && map.get("response") == null ) { // 구글, 깃허브
                mid = map.get("email").toString().split("@")[0];
            }
        }else{
            return false;
        }
        if( mid != null  ) {
            Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
            if (optionalMember.isPresent()) { // null 아니면
                BoardEntity boardEntity = boardDto.toentity();
                boardEntity.setMemberEntity( optionalMember.get() );
                boardRepository.save(boardEntity);
                String uuidfile = null;
                if (boardDto.getBimg().size() != 0) {
                    for (MultipartFile file : boardDto.getBimg()) {
                        UUID uuid = UUID.randomUUID();

                        uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                        String dir = "/home/ec2-user/app/springproject_animalhospital/build/resources/main/static/upload/";
                     //   String dir = "C:\\Users\\504\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";

                        String filepath = dir + uuidfile;

                        try {
                            file.transferTo(new File(filepath));

                            BoardimgEntity boardimgEntity = BoardimgEntity.builder()
                                    .bimg(uuidfile)
                                    .boardEntity(boardEntity)
                                    .build();

                            boardimgRespository.save(boardimgEntity);

                            boardEntity.getBoardimgEntities().add(boardimgEntity);

                        } catch (Exception e) {
                            System.out.println("파일저장실패 : " + e);
                        }
                    }

                }

                return true;

            } else { // 로그인이 안되어 있는경우
                return false;
            }

        }
        return false;
    }

    @Transactional
    public boolean noticesave(String btitle, String bcontent) {
        MemberEntity memberEntity = memberRepository.findBymid("admin").get();
        BoardEntity boardEntity = BoardEntity.builder()
                .cno(1)
                .bcontent(bcontent)
                .btitle(btitle)
                .memberEntity(memberEntity)
                .build();
        boardRepository.save(boardEntity);
        return true;
    }

    @Transactional
    public boolean noticeupdate(int bno, String btitle, String bcontent) {
        MemberEntity memberEntity = memberRepository.findBymid("admin").get();
        Optional<BoardEntity> optional = boardRepository.findById(bno);
        if(optional.isPresent()) {
            BoardEntity boardEntity = optional.get();
            boardEntity.setBtitle(btitle);
            boardEntity.setBcontent(bcontent);
            return true;
        }
        return false;
    }


    @Transactional
    public boolean noticedelete(int bno) {
        Optional<BoardEntity> optional = boardRepository.findById(bno);
        if(optional.isPresent()) {
            BoardEntity boardEntity = optional.get(); boardRepository.delete(boardEntity); return true;
        }
        return false;
    }

    public Map< String , List<Map<String , String >>> boardlist(int page ) // 인수
    {



        Page<BoardEntity> boardEntitylist = null ;
        Pageable pageable = PageRequest.of( page , 12 , Sort.by( Sort.Direction.DESC , "bno")    );

        int cno=2;
        List<  Map<String , String >  > Maplist = new ArrayList<>();

        boardEntitylist =boardRepository.findByblist(cno, pageable);
        System.out.println( boardEntitylist.toString() );

        int btncount = 5;
        int startbtn  = ( page / btncount ) * btncount + 1;
        int endhtn = startbtn + btncount -1;
        if( endhtn > boardEntitylist.getTotalPages() ) endhtn = boardEntitylist.getTotalPages();


        for( BoardEntity entity : boardEntitylist ){
            // 3. map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("bno", entity.getBno()+"" );
            map.put("btitle", entity.getBtitle());
            map.put("bimg", entity.getBoardimgEntities().get(0).getBimg());
            map.put( "startbtn" , startbtn+"" );
            map.put("mid", entity.getMemberEntity().getMid());
            map.put("bdate",  entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put( "endhtn" , endhtn+"" );
            map.put( "totalpages" , boardEntitylist.getTotalPages()+"" );
            // 4. 리스트 넣기
            Maplist.add(map);
        }
        Map< String , List<  Map<String , String >  > > object = new HashMap<>();

        object.put( "blists" , Maplist );

        return  object;
    }

    public Map< String , List<Map<String , String >>> boartiplist(int page ) // 인수
    {

        System.out.println( "페이지 :"+ page );

        Page<BoardEntity> boardEntitylist = null ;
        Pageable pageable = PageRequest.of( page , 5 , Sort.by( Sort.Direction.DESC , "bno")    );

        int cno=3;
        List<  Map<String , String >  > Maplist = new ArrayList<>();

        boardEntitylist =boardRepository.findByblist(cno, pageable);
        System.out.println( boardEntitylist.toString() );

        int btncount = 5;
        int startbtn  = ( page / btncount ) * btncount + 1;
        int endhtn = startbtn + btncount -1;
        if( endhtn > boardEntitylist.getTotalPages() ) endhtn = boardEntitylist.getTotalPages();


        for( BoardEntity entity : boardEntitylist ){
            // 3. map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("bno", entity.getBno()+"" );
            map.put("btitle", entity.getBtitle());
            map.put("bimg", entity.getBoardimgEntities().get(0).getBimg());
            map.put( "startbtn" , startbtn+"" );
            map.put("mid", entity.getMemberEntity().getMid());
            map.put("bdate",  entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put( "endhtn" , endhtn+"" );
            map.put( "totalpages" , boardEntitylist.getTotalPages()+"" );
            // 4. 리스트 넣기
            Maplist.add(map);
        }
        Map< String , List<  Map<String , String >  > > object = new HashMap<>();

        object.put( "blists" , Maplist );

        return  object;
    }

    public JSONObject getnoticelist(int page) {
        JSONObject jo = new JSONObject();
        // Pageable : 페이지처리 관련 인터페이스
        // PageRequest : 페이징처리 관련 클래스
        // PageRequest.of(page, size) : 페이징처리 설정
        // page = "현재 페이지" [0부터 시작 ]
        // size = "현재 페이지에 보여줄 게시물 수"
        // sort = "정렬기준" [Sort.Direction.DESC]
        // sort 문제점 : 정렬 필드명에 _인식 불가능 ----> SQL 처리
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "cno")  );
        int cno = 1;
        Page<BoardEntity> boardEntities = boardRepository.findByblist(cno, pageable);
        JSONArray jsonArray = new JSONArray();

        for (BoardEntity entity : boardEntities ) {
            JSONObject object = new JSONObject();
            object.put("bno", entity.getBno());
            object.put("btitle", entity.getBtitle());
            object.put("bcontent", entity.getBtitle());
            object.put("bindate", entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
            object.put("mid", entity.getMemberEntity().getMid());
            jsonArray.put(object);
        }

        // 페이지에 표시할 총 버튼 개수
        int btncount = 5;
        // 시작 번호의 번호 [ 현재 페이지 / 표시할 버튼 수 ) * 표시할 버튼 수 + 1
        int startbtn = (page / btncount) * btncount + 1;
        // 끝 번호 버튼의 번호 [ 시작버튼 + 표시버튼수 - 1 ]
        int endbtn = startbtn + btncount - 1;
        // 만약에 끝번호가 마지막페이지보다 크면 끝번호는 마지막페이지 번호로 사용
        if(endbtn > boardEntities.getTotalPages()) endbtn = boardEntities.getTotalPages();
        jo.put("startbtn", startbtn);
        jo.put("endbtn", endbtn);
        jo.put("data", jsonArray);
        jo.put("totalpage", boardEntities.getTotalPages()); // 전체 페이지 수
        return jo;
    }

    public JSONObject getboard( int bno ){

        OauthDto loginDto = (OauthDto) request.getSession().getAttribute("login");
        Optional<BoardEntity> optionalRoomEntity =  boardRepository.findById(bno );
        BoardEntity boardEntity =  optionalRoomEntity.get();
        String same = null;
        if(loginDto == null){
            same = "false";
        }else if(boardEntity.getMemberEntity().getMid().equals(loginDto.getMid())){
            same =  "true";
        }else{
            same =  "false";
        }
        // 2.  해당 엔티티 -> json 객체 변환
        JSONObject object = new JSONObject();
        // 1. json에 엔티티 필드 값 넣기
        object.put("bno" ,boardEntity.getBno());
        object.put("btitle" , boardEntity.getBtitle());
        object.put("bcontent" , boardEntity.getBcontent());
        object.put("modifiedate" , boardEntity.getModifiedate());
        object.put("mid" , boardEntity.getMemberEntity().getMid());
        object.put("same" , same);

        JSONArray jsonArray = new JSONArray();
        for(  BoardimgEntity boardimgEntity : boardEntity.getBoardimgEntities() ) { //  룸별로 이미지 여러개
            jsonArray.put( boardimgEntity.getBimg());
        }
        // 3. jsonarray를 json객체 포함
        object.put("bimglist" , jsonArray) ;
        // 3. 반한
        return object;
    }

    @Transactional
    public boolean delete( int bno ){
        BoardEntity boardEntity =  boardRepository.findById( bno ).get();
        if( boardEntity != null ){
            // 해당 엔티티를 삭제
            boardRepository.delete( boardEntity );
            return true;
        }else{
            return false;
        }
    }

    public CrawlDto crawling(String city, String name) {
        String code = city+name;
        String inflearnUrl = "https://search.daum.net/search?nil_suggest=btn&w=tot&DA=SBC&q="+code;
        Connection conn = Jsoup.connect(inflearnUrl);
        try {
            CrawlDto crawlDto = new CrawlDto();
            Document document = conn.get();
            // Elements title = document.getElementsByClass("inner_tit").first().select("b");
            try{ // try catch 두번 쓴 이유 크롤링 null값 예외처리 위해서
                Elements score = document.getElementsByClass("txt_info ").first().getElementsByClass("f_eb");
//            String title2 = title.text().replaceAll(" ","");
                String  score2 = score.first().text();
                String  link = score.attr("href");
                crawlDto.setScroe(score2);
                crawlDto.setLink(link);
            }catch (NullPointerException e){}
            return  crawlDto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }


    @Transactional
    public boolean replysave(int bno, String reply) {

        String mid = memberService.authenticationget();

        if( mid != null  ) {
            Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
            if (optionalMember.isPresent()) { // null 아니면
                MemberEntity memberEntity = memberRepository.findBymid(mid).get();
                BoardEntity boardEntity = boardRepository.findBybno(bno);
                ReplyEntity replyEntity = ReplyEntity.builder()
                        .rindex(0)
                        .rcontent(reply)
                        .boardEntity(boardEntity)
                        .memberEntity(memberEntity)
                        .build();
                replyRepository.save(replyEntity);
                return true;
            } else { // 로그인이 안되어 있는경우
                return false;
            }
        }
        return false;
    }

    public JSONArray getreply(int bno){
        System.out.println("getreply");
//        System.out.println("login : " + request.getSession().getAttribute("login"));
        OauthDto oauthDto= (OauthDto) request.getSession().getAttribute("login");
        System.out.println(oauthDto);
        boolean same;
        JSONArray jsonArray = new JSONArray();
        List<ReplyEntity> replyEntities = replyRepository.findreply(bno);
        for(ReplyEntity replyEntity : replyEntities){
            JSONObject object = new JSONObject();
            object.put("rno",replyEntity.getRno());
            object.put("rindex",replyEntity.getRindex());
            object.put("mid",replyEntity.getMemberEntity().getMid());
            object.put("rcontent",replyEntity.getRcontent());
            object.put("createdate",replyEntity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            if(oauthDto == null){
                same = false;
            }else if(replyEntity.getMemberEntity().getMid().equals(oauthDto.getMid())){
                same =  true;
            }else{
                same =  false;
            }
            object.put("same",same);
            jsonArray.put(object);
        }
        return jsonArray;
    }
    @Transactional
    public boolean replydelete(int rno) {
        ReplyEntity replyEntity = replyRepository.findByrno(rno);
        replyRepository.delete(replyEntity);
        return true;

    }

    @Transactional
    public JSONObject replyupdate(int rno) {
        ReplyEntity replyEntity = replyRepository.findByrno(rno);
        JSONObject object = new JSONObject();
        object.put("rno", replyEntity.getRno());
        object.put("rcontent", replyEntity.getRcontent());
        object.put("createdate", replyEntity.getModifiedate());
        object.put("member", replyEntity.getMemberEntity().getMid());
        object.put("board", replyEntity.getBoardEntity());
        return object;
    }
    @Transactional
    public boolean reupdate(int rno, String reply) {
        ReplyEntity replyEntity = replyRepository.findByrno(rno);
        replyEntity.setRcontent(reply);
        return true;
    }

    public boolean rereplysave(int bno, int rindex, String reply) {
        System.out.println(rindex);
      String mid = memberService.authenticationget();
        if( mid != null  ) {
            Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
            if (optionalMember.isPresent()) { // null 아니면
                MemberEntity memberEntity = memberRepository.findBymid(mid).get();
                BoardEntity boardEntity = boardRepository.findBybno(bno);
                ReplyEntity replyEntity = ReplyEntity.builder()
                        .rindex(rindex)
                        .rcontent(reply)
                        .boardEntity(boardEntity)
                        .memberEntity(memberEntity)
                        .build();
                replyRepository.save(replyEntity);
                return true;
            } else { // 로그인이 안되어 있는경우
                return false;
            }
        }
        return false;
    }

    public JSONArray getrereply(int bno, int rindex) {

        OauthDto oauthDto= (OauthDto) request.getSession().getAttribute("login");
        boolean same;

        JSONArray jsonArray = new JSONArray();
        List<ReplyEntity> replyEntities = replyRepository.findrereply(bno, rindex);
        for(ReplyEntity replyEntity : replyEntities){
            JSONObject object = new JSONObject();
            object.put("rno",replyEntity.getRno());
            object.put("rindex",replyEntity.getRindex());
            object.put("mid",replyEntity.getMemberEntity().getMid());
            object.put("rcontent",replyEntity.getRcontent());
            object.put("createdate",replyEntity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if(oauthDto == null){
                same = false;
            }else if(replyEntity.getMemberEntity().getMid().equals(oauthDto.getMid())){
                same =  true;
            }else{
                same =  false;
            }

            object.put("same",same);
            jsonArray.put(object);
        }
        return jsonArray;

    }

   /* 조회수 증가
   @Transactional
    public JSONObject getboard(int bno) { // 개별조회
        // 조회수 증가처리
        String ip = request.getRemoteAddr(); // 사용자의 ip 가져오기
        Optional<BoardEntity> Optional = boardRepository.findById(bno);
        BoardEntity entitiy = Optional.get();
        // ip와 bno를 합쳐서 세션(서버내 저장소) 부여
        Object com = request.getSession().getAttribute(ip+bno);
        if(com == null) {
            request.getSession().setAttribute(ip+bno, 1);
            request.getSession().setMaxInactiveInterval(60*60*24); // 세션 허용시간 [ 초단위 ]
            // 조회수 증가
            entitiy.setBview(entitiy.getBview()+1);
        }
        JSONObject jo = new JSONObject();
        jo.put("bno", entitiy.getBno());
        jo.put("btitle", entitiy.getBtitle() );
        jo.put("bcontent", entitiy.getBcontent());
        jo.put("bview", entitiy.getBview());
        jo.put("blike", entitiy.getBlike());
        jo.put("bindate" , entitiy.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm") ) );
        jo.put("bmodate" , entitiy.getUpdateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm") ) );
        jo.put("mid", entitiy.getMemberEntity().getMid());
        return jo;
    }
    */

    @Transactional
    public boolean bupdate(BoardDto boardDto) {

        Optional<BoardEntity> optional
                =  boardRepository.findById( boardDto.getBno() );
        BoardEntity boardEntity =  optional.get();
        boardEntity.setCreatedate(boardEntity.getCreatedate());
        boardEntity.setBtitle(boardDto.getBtitle());
        boardEntity.setBcontent(boardDto.getBcontent());
        if( boardDto.getBimg()!=null){
            List<BoardimgEntity> boardimgEntityList = boardimgRespository.getboardimgEntities(boardDto.getBno());
            for(BoardimgEntity boardimgEntity: boardimgEntityList){
                boardimgRespository.delete(boardimgEntity);
            }

            String uuidfile = null;
            if (boardDto.getBimg().size() != 0) {
                for (MultipartFile file : boardDto.getBimg()) {
                    UUID uuid = UUID.randomUUID();

                    uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                  //  String dir = "C:\\Users\\504\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";
                    String dir = "/home/ec2-user/app/springproject_animalhospital/build/resources/main/static/upload/";
                    String filepath = dir + uuidfile;

                    try {
                        file.transferTo(new File(filepath));

                        BoardimgEntity boardimgEntity = BoardimgEntity.builder()
                                .bimg(uuidfile)
                                .boardEntity(boardEntity)
                                .build();

                        boardimgRespository.save(boardimgEntity);

                        boardEntity.getBoardimgEntities().add(boardimgEntity);

                    } catch (Exception e) {
                        System.out.println("파일저장실패 : " + e);
                    }
                }

            }
        }

        return true;
    }


}
