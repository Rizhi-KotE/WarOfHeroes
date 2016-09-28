package rk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ConnectController {
    @MessageMapping(value = "/connect")
    public Map<String, String> connect(Principal principal) {
        Map<String, String> out = new HashMap<>();
        out.put("user", principal.getName());
        return out;
    }

    @Autowired
    SendMessageService sendMessageService;

    @SubscribeMapping(value = "/queue/play")
    public void subscribe(Principal p, SimpMessageHeaderAccessor accessor) {
        sendMessageService.addUser(p.getName());
    }
}
