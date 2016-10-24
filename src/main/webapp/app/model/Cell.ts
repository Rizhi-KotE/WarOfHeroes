import {CreatureStack} from "./creatureStack";
import objectContaining = jasmine.objectContaining;
export class Cell {
    constructor(x: number, y:number){
        this.x = x;
        this.y = y;
    }
    stack: CreatureStack;
    available: boolean;
    x: number;
    y: number;

    clone() {
        var clone = Object.create(this);
        Object.assign(clone, this);
        return clone;
    }
}