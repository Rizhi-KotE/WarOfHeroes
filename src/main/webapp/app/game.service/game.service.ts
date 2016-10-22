import {Injectable} from "@angular/core";
import {CreatureStack} from "../model/creatureStack";
import {StompService} from "../stomp.service";
import {BehaviorSubject} from "rxjs";
import {Cell} from "../model/Cell";

@Injectable()
export class GameService {
    subject: BehaviorSubject<any>;

    filterByDestination(destination: string, match: RegExp): boolean {
        return match.test(destination);
    }
    
    start(creaturesChoice: CreatureStack): void {
        this.subject = new BehaviorSubject(null);
        this.stompService.subscribe("/user/queue/game*", result => {
            this.subject.next(JSON.parse(result.body));
        });
        this.stompService.send("/user/queue/game.start", creaturesChoice);
    }

    subscribe(destination: string) {
        return this.subject;
    }

    getCreatures(): Promise<Cell[][]> {
        return this.subject.asObservable()
            .filter(frame=>null && this.filterByDestination(frame.headers.destination, /game\.creatures/))
            .toPromise();
    }

    makeAMove(message): Promise<any> {
        return this.subject.asObservable()
            .filter(frame=>null && this.filterByDestination(frame.headers.destination, /game\.makemove/))
            .toPromise();
    }

    constructor(private stompService: StompService) {
    }
}