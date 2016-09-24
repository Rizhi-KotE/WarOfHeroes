package rk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rk.dto.Greeting;
import rk.dto.HelloMessage;

@Controller
public class GreetingController {

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/hello")

    public void greeting(HelloMessage message) throws Exception {
        answer("Hello, " + message.getName() + "!");
    }

    public void answer(String greeting) throws Exception {
        Thread.sleep(3000);// simulated delay
        template.convertAndSend("/topic/greetings", new Greeting(greeting));
    }
}
