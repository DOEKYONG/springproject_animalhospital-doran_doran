package animalhospital.config;

import animalhospital.service.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class AnswerWebSocketHandler extends TextWebSocketHandler {

    private Map<WebSocketSession, String> list = new HashMap<>();

    @Autowired
    private MemberService memberService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        String mid = path.substring(path.lastIndexOf("/")+1);
        list.put(session, mid);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject object = new JSONObject(message.getPayload());
        memberService.messageanswer(object);
        for( WebSocketSession socketSession :list.keySet() ){
            if(list.get(socketSession).equals(object.get("to"))){
                socketSession.sendMessage(message);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        list.remove(session);
    }
}