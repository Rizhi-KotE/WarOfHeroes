import {Cell} from "./Cell";
export class AttackMessage {
    constructor(attackCell: Cell, targetCell: Cell) {
        this.attackCell = attackCell;
        this.targetCell = targetCell;
    }

    attackCell: Cell;
    targetCell: Cell;
}