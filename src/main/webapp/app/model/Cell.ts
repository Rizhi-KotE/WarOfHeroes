import {CreatureStack} from "./creatureStack";
export class Cell {
    creatureStack: CreatureStack;
    available: boolean;
    x: number;
    y: number;
}