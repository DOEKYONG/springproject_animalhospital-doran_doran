package animalhospital.service;


import animalhospital.domain.member.MemberEntity;
import animalhospital.domain.member.MemberRepository;
import animalhospital.domain.message.MessageEntity;
import animalhospital.domain.message.MessageRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Transactional // 메시지 전송 메소드
    public  boolean messagesend(JSONObject object) {

        // JSON 정보 호출
        String from = (String) object.get("from");
        String to = (String) object.get("to");
        String msg = (String) object.get("msg");
        // 각 회원들의 엔티티 찾기
        MemberEntity fromentity = null;
        Optional<MemberEntity> fromoption = memberRepository.findBymid(from);
        if(fromoption.isPresent()) {
            fromentity = fromoption.get();
        } else {return false; }
        MemberEntity toentity = null;
        Optional<MemberEntity> tooption = memberRepository.findBymid(to);
        if(tooption.isPresent()) {
            toentity = tooption.get();
        } else {return false;}

        MessageEntity message = MessageEntity.builder()
                .msg(msg)
                .fromentity(fromentity)
                .toentity(toentity)
                .msgtype(1)
                .build();
        messageRepository.save(message);

        fromentity.getFromentitylist().add(message); // 보낸사람 엔티티의 보낸메시지 리스트에 메시지 저장
        toentity.getToentitylist().add(message); // 받는사람 엔티티의 받은메시지 리스트에 메시지 저장
        return true;
    }

    // 안 읽은 메시지 개수 메소드
    public Integer getisread() {
        String mid = memberService.authenticationget();
        if(mid == null) {return -1;}
        int count = 0;
//        for(MessageEntity temp : memberRepository.findBymid(mid).get().getToentitylist()) {
//
//        }
        int mno = memberRepository.findBymid(mid).get().getMno();
        count = messageRepository.getisread(mno);

        return count;
    }

    // 본인이 보낸 메시지
    public JSONArray getfrommsglist() {
        String mid = memberService.authenticationget();
        if(mid == null) {return null;}
        List<MessageEntity> fromlist = memberRepository.findBymid(mid).get().getFromentitylist();
        JSONArray js = new JSONArray();
        for(MessageEntity temp : fromlist) {
            JSONObject jo = new JSONObject();
            jo.put("msgno", temp.getMsgno());
            jo.put("msg",temp.getMsg());
            jo.put("to" , temp.getToentity().getMid() );
            jo.put("date",temp.getCreatedate());
            jo.put("isread",temp.isIsread());
            js.put(jo);
        }
        return js;
    }
    // 본인이 받은 메시지
    public JSONArray gettomsglist() {
        String mid = memberService.authenticationget();
        if(mid == null) {return null;}
        List<MessageEntity> tolist = memberRepository.findBymid(mid).get().getToentitylist();
        JSONArray js = new JSONArray();
        for(MessageEntity temp : tolist) {
            JSONObject jo = new JSONObject();
            jo.put("msgno", temp.getMsgno());
            jo.put("msg",temp.getMsg());
            jo.put("from" , temp.getToentity().getMid() );
            jo.put("date",temp.getCreatedate());
            jo.put("isread",temp.isIsread());
            js.put(jo);
        }
        return js;
    }

    @Transactional
    public boolean isread(int msgno) {
        messageRepository.findById(msgno).get().setIsread(true);
        return true;
    }

    @Transactional
    public boolean msgdelete( List<Integer> deletelist ){
        // 1. 반복문 이용한 모든 엔티티 호출
        System.out.println(deletelist.toString() );
        for( int msgno :  deletelist ){

            MessageEntity entity
                    = messageRepository.findById( msgno).get();

            messageRepository.delete( entity );

        }
        return true;
    }

}