import {Command} from "./Command";
import {Cell} from "./Cell";
export class ChangeTurnCommand implements Command{
    type: string;

    complete(x: number, y: number) {
    }

    currentCell: Cell;

    yourTurn: boolean;
}