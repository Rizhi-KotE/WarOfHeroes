import {Component, OnInit} from "@angular/core";
import {WebSocketService} from "./websocket.service";
import {StompService} from "./stomp.service";

@Component({
    selector: "my-app",
    template: `<h1>War of Heroes</h1>
<button (click)="click()"></button>`
})


export class AppComponent {
    stompClient: any;
    click(): void {
        this.stompClient.send("/user/queue/game.start");
        this.stompClient.subscribe("/user/queue/game*", result => console.log(result));
    }
    constructor(private stompService: StompService) {
        this.stompClient = this.stompService.connect()
    }
}