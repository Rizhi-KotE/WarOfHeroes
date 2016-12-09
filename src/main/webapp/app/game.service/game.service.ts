import {Injectable} from "@angular/core";
import {CreatureStack} from "../model/creatureStack";
import {StompService} from "../stomp.service";
import {Subject, Observable} from "rxjs";
import {Http} from "@angular/http"
import {Cell} from "../model/Cell";
import {AttackMessage} from "../model/AttackMessage";
import {Creature} from "../model/creature";
import {ClientResponse} from "http";
import {Race} from "../model/Race";
import {Command} from "../model/Command";

@Injectable()
export class GameService {
    subject: Subject<any> = new Subject();

    startMessage(creaturesChoice: CreatureStack[]): Promise<any> {
        return this.http.post("/game/start", creaturesChoice).map(body => body.json()).toPromise();
    }

    subscribe(destination: string) {
        return this.subject;
    }

    getCreatures(): Promise<CreatureStack[]> {
        return this.http.get("/game/creatures").map(body => body.json() as CreatureStack[]).toPromise();
    }

    sendCreaturesPlacingMessage(): void {
        this.stompService.send("/user/queue/game.creatures");
    }

    constructor(private stompService: StompService, private http: Http) {
        this.stompService.subscribe("/user/queue/game*", result => {
            this.subject.next(JSON.parse(result.body));
        });
        this.subject.filter(command => command).subscribe(command => {
            if(command instanceof Array){
                (command as Array<any>).forEach(command => this.subject.next(command));
            }else if(command.type && typeof this[command.type] === "function"){
                this[command.type](command);
            }else if(typeof command === "string" && typeof this[command] === "function"){
                this[command](command);
            }
        })
    }

    startGame(signal: string): void{
        this.sendCreaturesPlacingMessage();
    }

    commandChain(): Observable<any> {
        return this.subject.asObservable();
    }

    sendMoveCreatureMessage(cell: Cell) {
        this.stompService.send("/user/queue/game.moveMessage", cell);
    }

    sendWaitMessage():void{
        this.stompService.send("/user/queue/game.waitMessage");
    }

    sendAvailableCellMessage(cell?: Cell){
        if(cell)
            this.stompService.send("/user/queue/game.availableCells", cell);
        else
            this.stompService.send("/user/queue/game.currentAvailableCells")
    }

    sendAttackMessage(message: AttackMessage) {
        this.stompService.send("/user/queue/game.attackMessage", message);
    }

    getCreaturesRaces(): Promise<Race[]>{
        return this.http.get("/creature").map(responce => responce.json() as Race[]).toPromise();
    }

    sendFinishMessage() {
        this.stompService.send("/user/queue/game.finish");
    }
}