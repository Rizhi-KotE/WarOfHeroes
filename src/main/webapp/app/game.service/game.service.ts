import {Injectable} from "@angular/core";
import {CreatureStack} from "../model/creatureStack";
import {StompService} from "../stomp.service";
import {AsyncSubject, BehaviorSubject} from "rxjs";

@Injectable()
export class GameService {
    subject: BehaviorSubject<any>;
    start(creaturesChoice: CreatureStack): void {
    this.subject = new BehaviorSubject();
        this.stompService.subscribe("/user/queue/game.*", result => {
            this.subject.next(JSON.parse(result.body));
        })
        this.stompService.send("/user/queue/game.start", creaturesChoice);
    }

    subscribe(destination: string) {
        return this.subject;
    }

    constructor(private stompService: StompService) {
    }
}