import {Command} from "./Command";
import {AddCreatureCommand} from "./addCreauteCommand";
export class StartPlacingCommand implements Command{
    type: string;

    complete(x: number, y: number) {
    }

    list: AddCreatureCommand[];
}