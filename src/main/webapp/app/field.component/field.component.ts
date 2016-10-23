import {Component} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";
import {Creature} from "../model/creature";

@Component({
    templateUrl: "/app/field.component/field.component.html"

})


export class FieldComponent {
    matrix: Cell[][] = new Array();
    creatures: Creature[];

    constructor(private gameServer: GameService) {
        this.gameServer.getCreatures().then(creatures => this.creatures = creatures.map(stack=>stack.creature));
    }

    onClick(message: Cell): void {
    	this.gameServer.makeAMove(message).then();
    }

    getCreatures(): void {
        this.gameServer.getCreatures().then(creatures => this.creatures = creatures.map(stack=>stack.creature));
    }
}