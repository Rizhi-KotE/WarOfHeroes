import {Command} from "../model/Command";
import {QueuePlace} from "./QueuePlace";
export class CreatureQueueCommand implements Command{
    complete(x: number, y: number) {
    }
    type: string;

    queue: QueuePlace[];

}