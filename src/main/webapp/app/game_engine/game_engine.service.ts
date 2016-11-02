import {Injectable} from "@angular/core"
import {Observable, Subject} from "rxjs"
import {Cell} from "../model/Cell";
import {CreatureStack} from "../model/creatureStack";
import {GameService} from "../game.service/game.service";
import {NullCell} from "../model/nullCell";
import {MoveCreatureCommand} from "../model/moveCreatureCommand";
import {Command} from "../model/Command";
import {AddCreatureCommand} from "../model/addCreauteCommand";
import {StartPlacingCommand} from "../model/StartPlacingCommand";
import {AttackMessage} from "../model/AttackMessage";

@Injectable()
export class GameEngine {
    commandChainSubject: Subject<Command> = new Subject<Command>();
    creaturesListObservable: Subject<CreatureStack[]> = new Subject<CreatureStack[]>();
    command: Command;

    commandChain(): Observable<Command> {
        return this.commandChainSubject.asObservable();
    }

    chooseCell(cell: Cell): void {
        this.gameService.sendMoveCreatureMessage(cell);
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
            .subscribe(command => {
                if (typeof this[command.type] === "function") {
                    this[command.type](command)
                }
                this.commandChainSubject.next(command);
            });
        this.gameService.sendCreaturesPlacingMessage();
    }

    startPlacing(command: StartPlacingCommand){
        this.commandChainSubject.next({type: "removeCreatures"} as Command);
        command.list.forEach(command => {
            this.commandChainSubject.next(command);
        });
    }

    private setCreatures(creatures: CreatureStack[]) {
        this.creaturesListObservable.next(creatures);
    }

    sendAtackMessage(message: AttackMessage) {
        this.gameService.sendAttackMessage(message);
    }
}