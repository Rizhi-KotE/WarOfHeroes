import {Cell} from "./Cell";

export class MoveCreatureCommand {
    output: Cell;
    input: Cell;
    constructor (output: Cell, input: Cell){
        this.output = output;
        this.input = input;
    }
}