package animalhospital.config;

import animalhospital.service.MessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class WebSellMsgSocketHandler extends TextWebSocketHandler {

    private Map<WebSocketSession, String> list = new HashMap<>();

    @Autowired
    private MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("서버에 접속했습니다." + session);
        String path = session.getUri().getPath();
        String mid = path.substring(path.lastIndexOf("/")+1);
        list.put(session, mid);
//        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String json = message.getPayload();
            JSONObject object = new JSONObject(json);
            messageService.messagesend(object);
            for (WebSocketSession socketSession : list.keySet()) {
                if (list.get(socketSession).equals(object.get("to"))) {
                    socketSession.sendMessage(message);
                }
            }
        }catch (Exception e){e.printStackTrace();}
//        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("서버에서 나갔습니다."+session);
        list.remove(session);
//        super.afterConnectionClosed(session, status);
    }
}