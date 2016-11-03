import {CreatureStack} from "./creatureStack";
import objectContaining = jasmine.objectContaining;
export class Cell {
    stack: CreatureStack;
    available: boolean;
    availableEnemy: boolean;
    chosen: boolean;

    x: number;
    y: number;

    constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }

    clear(field?: string) {
        if (field) {
            this[field] = null;
        } else {
            this.available = false;
            this.availableEnemy = false;
            this.chosen = false;
        }
    }

    clone() {
        var clone = Object.create(this);
        Object.assign(clone, this);
        return clone;
    }
}