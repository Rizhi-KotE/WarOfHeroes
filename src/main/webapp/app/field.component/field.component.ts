import {Component, OnInit} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";
import {Creature} from "../model/creature";

@Component({
    templateUrl: "/app/field.component/field.component.html"

})


export class FieldComponent implements OnInit{
    ngOnInit(): void {
        this.matrix = new Array<any>()
        for(var i = 0; i < 10 ; i++){
            var line = new Array<Cell>();
            for(var j = 0 ; j < 10; j++){
                line.push (new Cell());
            }
            this.matrix.push(line);
        }
    }

    matrix: Cell[][];
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