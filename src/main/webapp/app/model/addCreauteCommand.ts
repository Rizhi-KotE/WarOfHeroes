import {Command} from "./Command";
import {CreatureStack} from "./creatureStack";
export class AddCreatureCommand implements Command {
    complete(x: number, y: number) {
        this.x = x;
        this.y = y;
    }

    type: string = "addCreature";
    x: number;
    y: number;
    stack: CreatureStack;

    constructor(creature: CreatureStack, x?: number, y?: number) {
        this.x = x;
        this.y = y;
        this.stack = creature;
    }
}