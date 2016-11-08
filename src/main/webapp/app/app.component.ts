import {Component} from "@angular/core";
import {Http} from "@angular/http";
import {GameService} from "./game.service/game.service";
import {CreatureStack} from "./model/creatureStack";

@Component({
    selector: "my-app",
    template: `
    <router-outlet></router-outlet>`
})


export class AppComponent {
    constructor(private gameService: GameService, private http: Http) {

    }

    startGame(): void {
        this.http.get("/creature").toPromise().then(
            responce => {
                return responce.json()[0].map(creature => {
                    new CreatureStack(creature, 10);
                });
            })
            .then(creaturesChoise=>this.gameService.startMessage(creaturesChoise));
    }
}