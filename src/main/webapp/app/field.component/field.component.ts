import {Component} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";

@Component({
    templateUrl: "/app/field.component/field.component.html"

})


export class FieldComponent {
    matrix: Cell[][] = new Array();

    constructor(private gameServer: GameService) {
        this.gameServer.subscribe("/user/queue/game*").subscribe(next => this.matrix = next);
    }

    onClick(message: Cell): void {
    	this.gameServer.send("/user/queue/game.step", {x: message.x; y: message.y})
    }
}