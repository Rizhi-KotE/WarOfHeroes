import {Creature} from "./creature";
export class CreatureStack {
    creature: Creature;
    size: number;

    constructor(creature: Creature, size: number) {
        this.creature = creature;
        this.size = size;
    }
}