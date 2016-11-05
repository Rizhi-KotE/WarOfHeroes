import {Cell} from "./Cell";
export class DamageCommand {
    type: string = "damage";

    currentCell: Cell;
    targetCell: Cell;
    damage: number;
}
