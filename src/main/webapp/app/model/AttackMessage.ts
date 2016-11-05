import {Cell} from "./Cell";
export class AttackMessage {
    constructor(targetCell: Cell) {
        this.targetCell = targetCell;
    }

    targetCell: Cell;
}