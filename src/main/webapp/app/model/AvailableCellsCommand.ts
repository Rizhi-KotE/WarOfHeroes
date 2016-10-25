import {Command} from "./Command";
import {Cell} from "./Cell";
export class AvailableCellsCommand implements Command {
    complete(x: number, y: number) {
    }

    type: string;

    currentCell: Cell;
    cells: Cell[];
}