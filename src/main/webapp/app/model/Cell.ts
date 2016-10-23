import {CreatureStack} from "./creatureStack";
export class Cell {
    stack: CreatureStack;
    available: boolean;
    x: number;
    y: number;
}