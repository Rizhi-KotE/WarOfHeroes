import {Injectable} from "@angular/core"
import {Observable, Subject} from "rxjs"
import {Cell} from "../model/Cell";
import {CreatureStack} from "../model/creatureStack";
import {GameService} from "../game.service/game.service";
import {NullCell} from "../model/nullCell";
import {MoveCreatureCommand} from "../model/moveCreatureCommand";
import {Command} from "../model/Command";
import {AddCreatureCommand} from "../model/addCreauteCommand";

@Injectable()
export class GameEngine {
    commandChainSubject: Subject<Command> = new Subject<Command>();
    creaturesListObservable: Subject<CreatureStack[]> = new Subject<CreatureStack[]>();
    command: Command;

    commandChain(): Observable<Command> {
        return this.commandChainSubject.asObservable();
    }

    chooseCell(cell: Cell): void {
        if (this.command) {
            this.command.complete(cell.x, cell.y);
            this.commandChainSubject.next(this.command);
            this.command = null;
        } else {
            this.command = new MoveCreatureCommand(cell);
        }
    }

    chooseStack(stack: CreatureStack): void {
            this.command = new AddCreatureCommand(stack);
    }

    getCreaturesObservable(): Observable<CreatureStack[]> {
        return this.creaturesListObservable.asObservable();
    }

    constructor(private gameService: GameService) {
        // this.gameService.getCreatures().then(creatures => this.setCreatures(creatures));
        this.gameService.commandChain()
            .filter(command => command)
            .filter(command => typeof this[command.type] === "function")
            .subscribe(command => this[command.type](command));
        this.gameService.sendCreaturesPlacingMessage();
    }

    private setCreatures(creatures: CreatureStack[]) {
        this.creaturesListObservable.next(creatures);
    }
}