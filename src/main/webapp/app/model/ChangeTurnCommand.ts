import {Command} from "./Command";
import {Cell} from "./Cell";
export class ChangeTurnCommand implements Command{
    type: string;

    currentCell: Cell;

    yourTurn: boolean;
}