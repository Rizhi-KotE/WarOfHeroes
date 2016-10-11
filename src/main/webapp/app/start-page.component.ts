import {Component} from "@angular/core";
import {StompService} from "./stomp.service";

@Component({
    selector: "my-app",
    template: `<button (click)="click()">Start Game</button>`
})


export class StartPageComponent {
    click(): void {
        this.stompService.send("/user/queue/game.start");
        this.stompService.subscribe("/user/queue/game*", result => console.log(result));
    }
    constructor(private stompService: StompService) {    }
}