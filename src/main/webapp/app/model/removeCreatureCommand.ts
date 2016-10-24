import {Command} from "./Command";
export class RemoveCreatureCommand implements Command{
    type: string = "removeCreature";

    x: number;
    y: number;

    constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }
}