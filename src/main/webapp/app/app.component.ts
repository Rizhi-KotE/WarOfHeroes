import {Component} from "@angular/core";
import {Http} from "@angular/http";
import {GameService} from "./game.service/game.service";
import {CreatureStack} from "./model/creatureStack";
import {GameEngine} from "./game_engine/game_engine.service";
import {Router} from "@angular/router";


@Component({
    selector: "my-app",
    styleUrls:["app/dialog/dialog.component.css"],
    template: `
<div class="dialog" [hidden]="!visible">
    <p class="message" [ngSwitch]="message">
        <span *ngSwitchCase="'win'">Вы победили!!!</span>
        <span *ngSwitchCase="'lose'">Вы проиграли!!!</span>
    </p>
    <button (click)="click()" ></button>
</div>
    <router-outlet></router-outlet>`
})


export class AppComponent {
    ngOnInit(): void {
        this.gameEngine.commandChain().filter(command => command && typeof this[command.type] === "function")
            .subscribe(command => this[command.type](command));
    }

    visible: boolean = false;
    message: string = "win";

    win() {
        this.message = "win";
        this.visible = true;
    }

    lose() {
        this.message = "lose";
        this.visible = true;
    }

    click() {
        this.router.navigateByUrl("");
        this.visible = false;
        this.gameEngine.sendFinishMessage();
    }

    constructor(private gameEngine: GameEngine, private router: Router) {
    }
}