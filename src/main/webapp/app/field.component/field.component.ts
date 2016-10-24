import {Component, OnInit} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";
import {CreatureStack} from "../model/creatureStack";
import {GameEngine} from "../game_engine/game_engine.service";
import {NullCell} from "../model/nullCell";

@Component({
    selector: "field",
    template: `
<table>
    <tr *ngFor="let line of matrix">
        <td *ngFor="let cell of line">
        	<cell (click)="chooseCell(cell)" [cell]="cell"></cell>
        </td>
    </tr>
</table>
<creatures_queue></creatures_queue>`
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

    constructor(private gameEngine: GameEngine) {
        this.gameEngine.creatureMove().subscribe(move => {
            if(move.output instanceof NullCell) {
                this.matrix[move.input.x][move.input.y].stack = move.output.stack;
            }else {
                this.matrix[move.input.x][move.input.y].stack = move.output.stack;
                this.matrix[move.output.x][move.output.y].stack = move.input.stack;
            }
        })
    }

    chooseCell(message: Cell): void {
    	this.gameEngine.chooseCell(message);
    }
}