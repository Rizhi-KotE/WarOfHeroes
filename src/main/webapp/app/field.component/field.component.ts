import {Component, OnInit} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";
import {CreatureStack} from "../model/creatureStack";

@Component({
    selector: "field",
    template: `
<table>
    <tr *ngFor="let line of matrix">
        <td *ngFor="let cell of line">
        	<cell (click)="onClick(cell)" [cell]="cell"></cell>
        </td>
    </tr>
</table>
<creatures_queue [creatures]="creatures" (choose)="chooseCreature($event)"></creatures_queue>
<button (click)="getCreatures()"></button>`
})


export class FieldComponent implements OnInit{
    ngOnInit(): void {
        this.matrix = new Array<any>()
        for(var i = 0; i < 10 ; i++){
            var line = new Array<Cell>();
            for(var j = 0 ; j < 10; j++){
                line.push (new Cell(i, j));
            }
            this.matrix.push(line);
        }
    }

    matrix: Cell[][];
    creatures: CreatureStack[];
    currentCreatureStack: CreatureStack;

    constructor(private gameServer: GameService) {
        this.gameServer.getCreatures().then(creatures => this.creatures = creatures);
    }

    onClick(message: Cell): void {
    	this.matrix[message.x][message.y].stack = this.currentCreatureStack;
        this.creatures = this.creatures.filter(creature => creature !== this.currentCreatureStack);
        this.currentCreatureStack = null;
    }

    getCreatures(): void {
        this.gameServer.getCreatures().then(creatures => this.creatures = creatures);
    }

    chooseCreature(message: CreatureStack): void{
        this.currentCreatureStack = message;
    }
}