import {Component} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";

@Component({
    templateUrl: "/app/field.component/field.component.html"

})


export class FieldComponent {
    matrix: Cell[][] = new Array();

    constructor(private gameServer: GameService) {
        this.gameServer.getCreatures().then(matrix => this.matrix = matrix);
    }

    onClick(message: Cell): void {
    	this.gameServer.makeAMove(message).then();
    }
}