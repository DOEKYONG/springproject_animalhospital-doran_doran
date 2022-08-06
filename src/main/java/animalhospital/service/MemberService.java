package animalhospital.service;

import animalhospital.domain.CountEntity;
import animalhospital.domain.CountingRepository;
import animalhospital.domain.member.*;
import animalhospital.domain.message.MessageEntity;
import animalhospital.domain.message.MessageRepository;
import animalhospital.dto.CountDto;
import animalhospital.dto.LoginDto;
import animalhospital.dto.OauthDto;
import animalhospital.dto.RequestDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MemberService implements OAuth2UserService<OAuth2UserRequest ,OAuth2User>, UserDetailsService {
    // UserDetailsService 인터페이스 [ 추상메소드 존재~~ ] : 일반 회원
    // -----> loadUserByUsername 메소드 구현
    // OAuth2UserService<OAuth2UserRequest , OAuth2User> : Oauth2 회원
    // ------> loadUser 메소드 구현

    //  *  oauth2 서비스 제공 메소드
    // OAuth2UserRequest : 인증 결과를 호출 클래스


    @Autowired
    HttpSession httpSession;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CountingRepository countingRepository;


    public String authenticationget() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String mid = null;
        if( principal instanceof UserDetails){
            mid = ((UserDetails) principal).getUsername(); return mid;
        }else if( principal instanceof DefaultOAuth2User){
            Map<String , Object> map =  ((DefaultOAuth2User) principal).getAttributes();
            if( map.get("response") != null ){
                Map< String , Object> map2  = (Map<String, Object>) map.get("response"); // 네이버
                mid = map2.get("email").toString().split("@")[0]; return mid;
            }else if(map.get("kakao_account") != null){
                Map< String , Object> map2  = (Map<String, Object>) map.get("kakao_account"); // 카카오
                mid = map2.get("email").toString().split("@")[0]; return mid;
            }else if(map.get("kakao_account") == null && map.get("response") == null ) { // 구글, 깃허브
                mid = map.get("email").toString().split("@")[0]; return mid;
            }
        }else{
            return 3+""; // 로그인이 안되어 있음
        }
        return null; // 프로그램 오류
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 인증[로그인] 결과 정보 요청
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser( userRequest );

        // 클라이언트 아이디 [ 네이버 vs 카카오 vs 구글 ] : oauth 구분용 으로 사용할 변수
        String registrationId = userRequest.getClientRegistration().getRegistrationId();


        // 회원정보 요청시 사용되는 JSON 키 이름 호출  : 회원정보 호출시 사용되는 키 이름
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 확인
        System.out.println(  " registrationId   " + registrationId   );
        System.out.println(  "userNameAttributeName key name :   " + userNameAttributeName   );
        System.out.println(  "login result  : " + oAuth2User.getAttributes() );

        // oauth2 정보 -> Dto -> entitiy -> db저장
        OauthDto oauthDto = OauthDto.of(  registrationId ,  userNameAttributeName  ,  oAuth2User.getAttributes()  );


        Optional<MemberEntity> optionalMemberEntity
                = memberRepository.findBymemail(oauthDto.getMemail());
        if(! optionalMemberEntity.isPresent()) {
            memberRepository.save(oauthDto.toentity());
        }

        httpSession.setAttribute("login", oauthDto);
        httpSession.setAttribute("date", LocalDate.now());
        LocalDate nowdate  = (LocalDate) httpSession.getAttribute("date");
        String date = nowdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        OauthDto csession = (OauthDto) httpSession.getAttribute("login");

        if(httpSession.getAttribute(csession.getMemail()+date)==null){
            CountDto cdto = CountDto.builder()
                    .cnum(csession.getMemail()+date)
                    .count(1)
                    .createdate(LocalDate.now())
                    .build();

            // 엔티티를 이용한 조회수 증가

            // 방문자수 중복방지 [ 세션 생성 ]
            httpSession.setAttribute(csession.getMemail()+date,true);
            System.out.println("중복방지"+httpSession.getAttribute(csession.getMemail()+date));
            httpSession.setMaxInactiveInterval(60*60*24);
            countingRepository.save(cdto.toentity());
        }

        // 반환타입 DefaultOAuth2User ( 권한(role)명 , 회원인증정보 , 회원정보 호출키 )
        // DefaultOAuth2User , UserDetails : 반환시 인증세션 자동 부여 [ SimpleGrantedAuthority : (권한) 필수~  ]
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                oAuth2User.getAttributes() ,
                userNameAttributeName
        );
    }

    public boolean delete() {
        System.out.println("삭제 시작");
        Optional<MemberEntity> optional = memberRepository.findBymid(authenticationget());
        if(optional.isPresent()) {
            System.out.println("삭제 엔티티 찾기 성공");
            MemberEntity memberEntity = optional.get();
            memberRepository.delete(memberEntity);
            return true;
        }
        return false;
    }

    /* 관리자 로그인을 userDetailService에서 처리
    public boolean adminlogin(String mid, String mpassword) {
        if (mid.equals("admin") && mpassword.equals("1234")) {
           MemberEntity memberEntity = MemberEntity.builder()
                           .mid(mid)
                   .mname("관리자")
                   .oauth("admin")
                   .mpassword(mpassword)
                   .memail("admin@gmail.com")
                                   .role(Role.ADMIN)
                                           .build();
            httpSession.setAttribute("admin", memberEntity);
            Optional<MemberEntity> optional = memberRepository.findBymid(mid);
            if(! optional.isPresent()) {
                memberRepository.save(memberEntity);
            }
            return  true;
        }
        return false;
    }*/


    // 1. 로그인 서비스 제공 메소드
    // 2. 패스워드 검증 X [ 시큐리티 제공 ]
    // 3. 아이디만 검증 처리
    @Override
    public UserDetails loadUserByUsername(String mid) throws UsernameNotFoundException {
            // 관리자 아이디는 미리 DB에 등록해 놓음
        // 1. 회원 아이디를 찾기

        Optional<MemberEntity> entitiy =  memberRepository.findBymid(mid); // 회원 아이디로 엔티티 찾기

        MemberEntity memberEntity = entitiy.orElse(null);
        // optional.isPresent() : null이 아니면
        // optional.orElse() : 만약에 optional 객체가 비어있으면 반환할 데이터

        // 2. 찾은 회원 엔티티의 권한[키]을 리스트에 담기
        List<GrantedAuthority> authorityList = new ArrayList<>(); // 부여된 인증들을 모아두기
        // GrantedAuthority : 부여된 인증의 클래스

        authorityList.add(new SimpleGrantedAuthority(memberEntity.getrolekey()));
        // 리스트에 인증된 엔티티의 키를 보관

        return new LoginDto(memberEntity, authorityList); // 회원엔티티, 인증된 리스트를 인증세션 부여
    }

    @Autowired
    RequestRepository requestRepository;

    @Transactional
    public int  requestsave(RequestDto requestDto) {
        String mid = authenticationget();
        Optional<MemberEntity> optionalMember = memberRepository.findBymid(mid);
        if( optionalMember.isPresent()) {
            int mno = optionalMember.get().getMno();
            Optional<RequestEntity> optionalRequest = requestRepository.findbymno(mno);
            if (!optionalRequest.isPresent()) {
                RequestEntity requestEntity = requestDto.toentity();
                requestEntity.setMno(mno);
                requestEntity.setHospital(requestEntity.getHospital());
                String uuidfile = null;
                UUID uuid = UUID.randomUUID();
                MultipartFile file = requestDto.getBinimg();
                uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
                String dir = "/home/ec2-user/app/springproject_animalhospital/build/resources/main/static/upload/";
//              String dir = "C:\\Users\\504\\Desktop\\springproject_animalhospital\\src\\main\\resources\\static\\upload\\";

                String filepath = dir + uuidfile;
                try {

                    file.transferTo(new File(filepath));
                    requestEntity.setBinimg(uuidfile);
                    requestRepository.save(requestEntity);
                    return 1;
                } catch (IOException e) {
                    System.out.println("requestsave error : " + e);
                }

            }else {
                return 2;
            }
        }
        return 3;
    }
    public JSONArray getbinlist() {
        JSONArray jsonArray = new JSONArray();
        List<RequestEntity> entities = requestRepository.findBybinlist();
        System.out.println(entities);
        for (RequestEntity entity : entities ){
            String mid = memberRepository.findbymno(entity.getMno());
            JSONObject object = new JSONObject();
            object.put("hno", entity.getHno());
            object.put("hospital", entity.getHospital());
            object.put("mno", entity.getMno());
            object.put("mid",mid);
            object.put("binimg", entity.getBinimg());
            jsonArray.put(object);
        }
        return jsonArray;
    }

    @Transactional
    public boolean setrole(int mno, String hospital, String bin) {
        if (bin != null) {
            MemberEntity memberEntity = memberRepository.findBymno(mno);
            memberEntity.setRole(Role.HOSPITAL);
            memberRepository.save(memberEntity);

            Optional<RequestEntity> requestEntity = requestRepository.findbyhospital(hospital);
            if (requestEntity.isPresent()) {
                RequestEntity requestEntity1 = requestEntity.get();
                requestEntity1.setBin(bin);
                requestEntity1.setActive(true);
                requestRepository.save(requestEntity1);
                return true;
            } else {
                return false; // 병원이 없는 경우
            }
        } else {
            return false; // 리스트에 없는 경우(=중복되는 경우?)
        }
    }
    //쪽지
    //메시지 전송
    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public boolean messagesend(JSONObject object){
        System.out.println("messagesend : " + object);
        String from = (String) object.get("from");
        String to = (String) object.get("to");
        String msg = (String) object.get("msg");
        int type = (int) object.get("type");

        MemberEntity fromentity = null;
        Optional<MemberEntity> optionalMember1 = memberRepository.findBymid(from);

        if(optionalMember1.isPresent()){
            fromentity = optionalMember1.get();
        }else { // 보내는 회원이 존재하지 않음
            return false;
        }
        int tomno = 0;
        String tomid = null;
        // 요청을 보내는 병원을 찾기 위해서
        Optional<RequestEntity> hospital = requestRepository.findbyhospital(to);
        if(hospital.isPresent()) {
            tomno = Integer.parseInt(requestRepository.findByhospital(to));
            tomid = String.valueOf(memberRepository.findbymno(tomno));
        }
        else {
            return false;
        }


        MemberEntity toentity = null;
        if(tomid != null) {
            Optional<MemberEntity> optionalMember2 = memberRepository.findBymid(tomid);
            if (optionalMember2.isPresent()) {
                toentity = optionalMember2.get();
            } else {
                return false; // 해당 되는 병원이 없음
            }
        }
        else if(tomid == null) {
            Optional<MemberEntity> optionalMember2 = memberRepository.findBymid(to);
            if(optionalMember2.isPresent()) {
                toentity = optionalMember2.get();
            } else {
                return false; // 해당 되는 회원이 없음
            }
        }

        MessageEntity messageEntity = MessageEntity.builder()
                .msg(msg)
                .fromentity(fromentity)
                .toentity(toentity)
                .msgtype(type)
                .build();

        messageRepository.save(messageEntity);

        fromentity.getFromentitylist().add(messageEntity);
        toentity.getToentitylist().add(messageEntity);
        return true;

    }


    public JSONArray gettomsglist(int type){
        String mid = authenticationget();
        Optional<MemberEntity> optional =  memberRepository.findBymid(mid);
        System.out.println("gettomsglsit optional : "+optional);
        int mno=0;
        if(optional.isPresent()){
            MemberEntity memberEntity = optional.get();
            System.out.println("gettomsglist member : " + memberEntity);
            mno = memberEntity.getMno();
        }
        List<MessageEntity>list = messageRepository.gettomsglist(mno, type);
        //JSON형 변환
        JSONArray jsonArray = new JSONArray();
        for(MessageEntity msg : list){
            JSONObject object = new JSONObject();
            object.put("msgno", msg.getMsgno());
            object.put("msg", msg.getMsg());
            object.put("from", msg.getFromentity().getMid());
            object.put("date", msg.getCreatedate());
            jsonArray.put(object);
        }
        System.out.println(jsonArray);
        return jsonArray;

    }

    public JSONArray getfrommsglist(int type){
        String mid = authenticationget();
        Optional<MemberEntity> optional =  memberRepository.findBymid(mid);
        System.out.println(optional);
        int mno=0;
        if(optional.isPresent()){
            MemberEntity memberEntity = optional.get();
            mno = memberEntity.getMno();
            System.out.println(mno);
        }
        System.out.println(type);
        List<MessageEntity>list = messageRepository.getfrommsglist(mno, type);

        //JSON형 변환
        JSONArray jsonArray = new JSONArray();
        for(MessageEntity msg : list){
            System.out.println(msg);
            JSONObject object = new JSONObject();
            object.put("msgno", msg.getMsgno());
            object.put("msg", msg.getMsg());
            object.put("to", msg.getToentity().getMid());
            object.put("from", msg.getFromentity().getMid());
            object.put("date", msg.getCreatedate());
            object.put("isread" , msg.isIsread() ); ///////// 읽었는지에 대한 여부
            jsonArray.put(object);
        }
        return jsonArray;
    }

    @Autowired
    private HttpServletRequest request;
    public JSONObject getinfo() {
        String mid = authenticationget();
        String hname =  (String) request.getSession().getAttribute("hname");
        String hdate =  (String) request.getSession().getAttribute("hdate");
        String hospital = hname+hdate;
        System.out.println(mid);
        System.out.println(hospital);
        JSONObject object = new JSONObject();
        object.put("mid", mid);
        object.put("hospital",hospital);
        return object;

    }


    public String getmid() {return authenticationget();}

    @Transactional
    public boolean messageanswer(JSONObject object){
        System.out.println(object);
        String from = (String) object.get("from");
        String to = (String) object.get("to");
        String msg = (String) object.get("msg");
        int type = (int) object.get("type");

        MemberEntity fromentity = null;
        Optional<MemberEntity> optionalMember1 = memberRepository.findBymid(from);
        if(optionalMember1.isPresent()){
            fromentity = optionalMember1.get();
        }else {
            return false;
        }
        System.out.println("2");
        MemberEntity toentity = null;
        Optional<MemberEntity> optionalMember2 = memberRepository.findBymid(to);
        if(optionalMember2.isPresent()){
            toentity =optionalMember2.get();
        }else {
            return false;
        }
        System.out.println("3");
        MessageEntity messageEntity = MessageEntity.builder()
                .msg(msg)
                .fromentity(fromentity)
                .toentity(toentity)
                .msgtype(type)
                .build();

        messageRepository.save(messageEntity);

        fromentity.getFromentitylist().add(messageEntity);
        toentity.getToentitylist().add(messageEntity);
        return true;

    }


    @Transactional
    public boolean isread(int msgno){
        messageRepository.findById(msgno).get().setIsread(true);
        return true;
    }

    public JSONArray todaycount(){
        JSONArray jsonArray = new JSONArray();
        JSONArray child =   new JSONArray();
        List<CountEntity> list = countingRepository.getcount();
        System.out.println(list.toString());
        JSONObject object2 = new JSONObject();
        object2.put("date","1");
        String od = object2.get("date").toString();
        for(CountEntity dto : list) {
            JSONObject object = new JSONObject();
            object.put("date",dto.getCreatedate());
            object.put("count",dto.getCount());
            if((object.get("date").toString().equals(od))) {
                child.put(object);

            } else {
                child = new JSONArray();
                child.put(object);
                jsonArray.put(child);
            }
            //jsonArray.put(object);
            od = object.get("date").toString();

        }
        //System.out.println("ddd"+ jsonArray);
        return jsonArray;
    }

    @Transactional
    public boolean msgdelete(List<Integer>deletelist){
        for(int msgno : deletelist){
            MessageEntity entity = messageRepository.findById(msgno).get();
            messageRepository.delete(entity);
        }
        return true;
    }

    public boolean findhospital(String hospital) {
        Optional<RequestEntity> requestEntity = requestRepository.findbyhospital(hospital);

        System.out.println(requestEntity.toString());
        if(requestEntity.isPresent()){
            return true;
        }else {
            return false;
        }
    }
}
