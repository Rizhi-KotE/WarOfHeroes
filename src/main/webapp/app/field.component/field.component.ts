import {Component, OnInit} from "@angular/core";
import {Cell} from "../model/Cell";
import {GameService} from "../game.service/game.service";
import {GameEngine} from "../game_engine/game_engine.service";
import {MoveCreatureCommand} from "../model/moveCreatureCommand";
import {AddCreatureCommand} from "../model/addCreauteCommand";
import {RemoveCreatureCommand} from "../model/removeCreatureCommand";
import {AvailableCellsCommand} from "../model/AvailableCellsCommand";
import {AvailableEnemiesCommand} from "../model/AvailableEnemiesCommand";
import {AttackMessage} from "../model/AttackMessage";
import {DamageCommand} from "../model/DamageCommand";

@Component({
    selector: "field",
    template: `
<button (click)="placeCreatures()">Place Creatures</button>
<button (click)="sendAvailableCellsMessage()">Get Available cells</button>
<button (click)="sendWaitMessage()">Wait</button>
<div style="position: relative">
    <div *ngFor="let line of matrix">
        <div *ngFor="let cell of line">
        	<cell 
        	(choose)="chooseCell($event)" 
        	[cell]="cell"></cell>
        </div>
    </div>
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
    chosenCell: Cell;

    constructor(private gameEngine: GameEngine, private gameService: GameService) {
        this.gameEngine.commandChain()
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
        if (cell.available)
            this.gameService.sendMoveCreatureMessage(cell);
        else if (cell.availableEnemy) {
            var message = new AttackMessage(cell);
            this.gameEngine.sendAtackMessage(message);
        }
    }

    availableCells(command: AvailableCellsCommand) {
        this.clearMatrix();
        this.currentCell = command.currentCell;
        command.cells.forEach(cell => this.matrix[cell.x][cell.y].available = true);
    }

    removeCreatures() {
        this.clearMatrix("stack")
    }

    clearMatrix(field?: string) {
        this.matrix.forEach(line => line.forEach(
            cell => cell.clear(field)
        ));
    }

    availableEnemies(command: AvailableEnemiesCommand) {
        this.clearMatrix("enemiesNeighbour")
        command.availableEnemies
            .forEach(cell=>this.matrix[cell.x][cell.y].availableEnemy = true);
    }

    placeCreatures(): void {
        this.gameService.sendCreaturesPlacingMessage();
    }

    sendAvailableCellsMessage(): void{
        this.gameService.sendAvailableCellMessage();
    }

    sendWaitMessage(): void {
        this.gameService.sendWaitMessage();
    }

    private calcCoordinateDelta(delta: number, second?: number) {
        if (second) {
            var delta = delta - second;
        }
        return delta ? delta < 0 ? -1 : 1 : 0;
    }

    damage(command: DamageCommand): void {
        var cell = this.matrix[command.targetCell.x][command.targetCell.y];
        cell.stack = command.targetCell.stack;
    }
}