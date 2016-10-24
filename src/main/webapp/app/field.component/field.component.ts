import {Component, OnInit} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";
import {CreatureStack} from "../model/creatureStack";
import {GameEngine} from "../game_engine/game_engine.service";
import {NullCell} from "../model/nullCell";
import {MoveCreatureCommand} from "../model/moveCreatureCommand";
import {AddCreatureCommand} from "../model/addCreauteCommand";
import {RemoveCreatureCommand} from "../model/removeCreatureCommand";

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
        this.gameEngine.commandChain().subscribe(command =>this[command.type](command));
    }

    addCreature(command: AddCreatureCommand) {
        this.matrix[command.x][command.y].stack = command.creature;
    }

    moveCreature(moveCommand: MoveCreatureCommand) {
        this.matrix[moveCommand.input.x][moveCommand.input.y].stack = moveCommand.output.stack;
        this.matrix[moveCommand.output.x][moveCommand.output.y].stack = null;
    }

    removeCreature(command: RemoveCreatureCommand) {
        this.matrix[command.x][command.y].stack = null;
    }

    chooseCell(message: Cell): void {
        this.gameEngine.chooseCell(message.clone());
    }
}