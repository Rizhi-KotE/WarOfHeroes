import {Cell} from "./Cell";
import {Command} from "./Command";
import {CreatureStack} from "./creatureStack";

export class MoveCreatureCommand implements Command {
    type: string = "moveCreature";

    output: Cell;
    input: Cell;
    creature: CreatureStack;

    complete(x: number, y: number) {
        this.input = new Cell(x, y);
    }

    constructor(output: Cell, input?: Cell) {
        this.output = output;
        this.input = input;
    }
}