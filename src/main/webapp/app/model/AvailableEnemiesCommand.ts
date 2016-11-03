import {Command} from "./Command";
import {Cell} from "./Cell";
import {CreatureStack} from "./creatureStack";

export class AvailableEnemiesCommand implements Command {
    complete(x: number, y: number) {
    }

    type: string;

    availableEnemies: Cell[];
}