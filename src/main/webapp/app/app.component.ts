import {Component, OnInit} from "@angular/core";
import {WebSocketService} from "./websocket.service";
import {Stomp} from "stompjs";
import SockJS from "sockjs-client";

@Component({
    selector: "my-app",
    template: `<h1>War of Heroes</h1>`
})


export class AppComponent implements OnInit {
    ngOnInit(): void {
        var ws = new SockJS("/battle")
        var stompClient = Stomp.over(ws);
        stompClient.connect({}, frame => {
            console.log(frame);
        })
    }

    constructor(private webSocketService: WebSocketService) {
    }
}