import {Component, OnInit} from "@angular/core"
import {Router} from "@angular/router";
import {GameService} from "../game.service/game.service";

@Component({
    styleUrls:["app/dialog/dialog.component.css"],
    selector: "dialog",
    template: `
<div class="dialog">
    <p class="message" [ngSwitch]="message">
        <span *ngSwitchCase="'win'">Вы победили!!!</span>
        <span *ngSwitchCase="'lose'">Вы проиграли!!!</span>
    </p>
    <button (click)="click()" ></button>
</div>
`
})
export class DialogComponent implements OnInit {
    ngOnInit(): void {
        this.gameService.commandChain().filter(command => command && typeof this[command.type] === "function")
            .subscribe(command => this[command.type](command));
    }

    visible: boolean = true;
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
    }

    constructor(private gameService: GameService, private router: Router) {
    }
}