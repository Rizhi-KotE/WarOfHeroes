import {Component} from "@angular/core";
import {Http} from "@angular/http";
import {GameService} from "./game.service/game.service";
import {CreatureStack} from "./model/creatureStack";

@Component({
    selector: "my-app",
    template: `
<div class="container">
    <h1>War of Heroes</h1>
    <div routerLink="/field">field</div>
    <button (click)="startGame()">Start game</button>
    <router-outlet></router-outlet>
</div>`
})


export class AppComponent {
    constructor(private gameService: GameService, private http: Http) {

    }

    startGame(): void {
        this.http.get("/creature").toPromise().then(
            responce => {
                return responce.json()[0].map(creature => {
                    var creatureChoice : CreatureStack = new CreatureStack();
                    creatureChoice.size = 10;
                    creatureChoice.creature = creature;
                    return creatureChoice;
                });
            })
            .then(creaturesChoise=>this.gameService.startMessage(creaturesChoise));
    }
}