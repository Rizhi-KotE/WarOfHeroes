import {CreatureStack} from "./creatureStack";
export class Cell {
    constructor(x: number, y:number){
        this.x = x;
        this.y = y;
    }
    stack: CreatureStack;
    available: boolean;
    x: number;
    y: number;
}