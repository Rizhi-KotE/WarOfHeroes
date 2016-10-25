import {Command} from "./Command";
import {Cell} from "./Cell";
import {CreatureStack} from "./creatureStack";

class Pair {
    key: CreatureStack;
    value: Cell[];
}
export class AvailableEnemiesCommand implements Command {
    complete(x: number, y: number) {
    }

    type: string;

    enemiesToCell: Pair[];
}