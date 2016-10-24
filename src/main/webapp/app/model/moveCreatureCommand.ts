import {Cell} from "./Cell";
import {Command} from "./Command";
import {CreatureStack} from "./creatureStack";

export class MoveCreatureCommand implements Command {
    type: string = "moveCreature";

    outX: number;
    outY: number;
    inX: number;
    inY: number;
    stack: CreatureStack;

    complete(x: number, y: number) {
        this.inX = x;
        this.inY = y;
    }

    constructor(outX: number, outY: number, inX?: number, inY?: number) {
        this.outX = outX;
        this.outY = outY;
        this.inX = inX;
        this.inY = inY;
    }
}