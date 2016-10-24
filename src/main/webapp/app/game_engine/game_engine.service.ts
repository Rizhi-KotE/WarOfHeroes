import {Injectable} from "@angular/core"
import {Observable, Subject} from "rxjs"
import {Cell} from "../model/Cell";
import {CreatureStack} from "../model/creatureStack";
import {GameService} from "../game.service/game.service";
import {NullCell} from "../model/nullCell";
import {MoveCreatureCommand} from "../model/moveCreatureCommand";

@Injectable()
export class GameEngine {
    creaturesMovementObservable: Subject<MoveCreatureCommand> = new Subject<MoveCreatureCommand>();
    creaturesListObservable: Subject<CreatureStack[]> = new Subject<CreatureStack[]>();

    currentCreatureCell: Cell = new NullCell();
    creaturesList: CreatureStack[];

    creatureMove(): Observable<MoveCreatureCommand> {
        return this.creaturesMovementObservable.asObservable();
    }

    chooseCell(cell: Cell): void {
        if (cell.stack) {
            this.creaturesMovementObservable.next(new MoveCreatureCommand(this.currentCreatureCell, cell));
            if (!this.currentCreatureCell.stack) {
                this.setCreatures(this.creaturesList.filter(creature => creature !== this.currentCreatureCell.stack));
            }
            this.currentCreatureCell = cell;
        }
        this.currentCreatureCell = null;
    }

    chooseStack(stack: CreatureStack): void {
        this.currentCreatureCell = new NullCell();
        this.currentCreatureCell.stack = stack;
    }

    getCreaturesObservable(): Observable<CreatureStack[]> {
        return this.creaturesListObservable.asObservable();
    }

    constructor(private gameService: GameService) {
        this.gameService.getCreatures().then(creatures => this.setCreatures(creatures));
    }

    private setCreatures(creatures: CreatureStack[]) {
        this.creaturesList = creatures;
        this.creaturesListObservable.next(this.creaturesList);
    }
}