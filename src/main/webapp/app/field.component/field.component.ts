import {Component, OnInit} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";
import {MoveCreatureCommand} from "../model/moveCreatureCommand";
import {AddCreatureCommand} from "../model/addCreauteCommand";
import {RemoveCreatureCommand} from "../model/removeCreatureCommand";
import {AvailableCellsCommand} from "../model/AvailableCellsCommand";
import {AvailableEnemiesCommand} from "../model/AvailableEnemiesCommand";
import {AttackMessage} from "../model/AttackMessage";
import {DamageCommand} from "../model/DamageCommand";
import {ChangeTurnCommand} from "../model/ChangeTurnCommand";

@Component({
    selector: "field",
    template: `
<div style="position: relative; display: inline-block;">
    <table>
        <tr *ngFor="let line of matrix"> 
            <td *ngFor="let cell of line"><cell 
        	(choose)="chooseCell($event)" 
        	[cell]="cell"></cell></td>
        </tr>
    </table>
</div>
<div style="display: inline-block; vertical-align: top">
    <button (click)="sendWaitMessage()">NEXT CREATURE</button>
    <creatures_queue></creatures_queue>
</div>`

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
        this.gameService.sendCreaturesPlacingMessage();
    }

    matrix: Cell[][];
    currentCell: Cell;
    yourTurn: boolean = false;

    constructor(private gameService: GameService) {
        this.gameService.commandChain()
            .filter(command => typeof this[command.type] ===  "function")
            .subscribe(command =>this[command.type](command));
    }

    addCreature(command: AddCreatureCommand) {
        this.matrix[command.x][command.y].stack = command.stack;
    }

    moveCreature(moveCommand: MoveCreatureCommand) {
        this.matrix[moveCommand.inX][moveCommand.inY].stack = moveCommand.stack;
        this.matrix[moveCommand.outX][moveCommand.outY] = new Cell(moveCommand.outX, moveCommand.outY);
    }

    removeCreature(command: RemoveCreatureCommand) {
        this.matrix[command.x][command.y].stack = null;
    }

    chooseCell(cell: Cell): void {
        if (this.yourTurn) {
            if (cell.available)
                this.gameService.sendMoveCreatureMessage(cell);
            else if (cell.availableEnemy) {
                var message = new AttackMessage(cell);
                this.gameService.sendAttackMessage(message);
            }
        }
    }

    availableCells(command: AvailableCellsCommand) {
        this.clearMatrix();
        this.currentCell = command.currentCell;
        this.matrix[command.currentCell.x][command.currentCell.y].yourTurn = true;
        command.cells.forEach(cell => this.matrix[cell.x][cell.y].available = true);
    }

    private removeCreatures() {
        this.clearMatrix("stack")
    }

    private clearMatrix(field?: string) {
        this.matrix.forEach(line => line.forEach(
            cell => cell.clear(field)
        ));
    }

    availableEnemies(command: AvailableEnemiesCommand) {
        this.clearMatrix("enemiesNeighbour")
        command.availableEnemies
            .forEach(cell=>this.matrix[cell.x][cell.y].availableEnemy = true);
    }

    sendCreaturesPlacesMessage(): void {
        this.gameService.sendCreaturesPlacingMessage();
    }

    sendAvailableCellsMessage(): void{
        this.gameService.sendAvailableCellMessage();
    }

    sendWaitMessage(): void {
        if (this.yourTurn)
            this.gameService.sendWaitMessage();
    }

    damage(command: DamageCommand): void {
        var cell = this.matrix[command.targetCell.x][command.targetCell.y];
        cell.stack = command.targetCell.stack;
    }

    changeTurn(command: ChangeTurnCommand): void {
        this.yourTurn = command.yourTurn;
    }
}