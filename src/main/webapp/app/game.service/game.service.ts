import {Injectable} from "@angular/core";
import {CreatureStack} from "../model/creatureStack";
import {StompService} from "../stomp.service";
import {BehaviorSubject, Observable} from "rxjs";
import {Http} from "@angular/http"
import {Cell} from "../model/Cell";

@Injectable()
export class GameService {
    subject: BehaviorSubject<any>;

    startRequest(creaturesChoice: CreatureStack): Promise<any> {
        return this.http.post("/game/start", creaturesChoice).map(responce => responce.json()).toPromise()
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

    makeAMove(message): Promise<any> {
        return this.http.post("/game/makemove", message).map(body => body.json()).toPromise();
    }

    constructor(private stompService: StompService, private http: Http) {
        this.subject = new BehaviorSubject(null);
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

    start(signal: string): void{
        this.sendCreaturesPlacingMessage();
    }

    commandChain(): Observable<any> {
        return this.subject.asObservable();
    }

    sendMoveCreatureMessage(cell: Cell) {
        this.stompService.send("/user/queue/game.move", cell);
    }

    sendAvailableCellMessage(cell: Cell){
        this.stompService.send("/user/queue/game.availableCells", cell);
    }
}