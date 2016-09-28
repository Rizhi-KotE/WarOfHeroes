package rk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SendMessageService {

    @Autowired
    private SimpMessagingTemplate template;

    List<String> users = new ArrayList<>();

    void addUser(String username) {
        users.add(username);
    }

    @PostConstruct
    public void run() {
        new Thread(() -> {
            while (true) {
                for (String username : users) {
                    template.convertAndSendToUser(username, "/queue/play", Arrays.asList(username));
                }
                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
