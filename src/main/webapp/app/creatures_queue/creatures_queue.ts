import {Component, Input, Output, EventEmitter} from "@angular/core"
import {CreatureStack} from "../model/creatureStack";
import {GameEngine} from "../game_engine/game_engine.service";
import {CreatureQueueCommand} from "./CreatureQueueCommand";
import {QueuePlace} from "./QueuePlace";
import {DomSanitizer} from "@angular/platform-browser"


@Component({
	selector: "creatures_queue",
	template: `
<div style="overflow: hidden; width: 500px;">
    <div style="overflow: hidden; height: 55px;overflow-y: hidden; width: 10000000px;;">
        <span *ngFor="let place of creatures"
        [ngStyle]="{
            'width.px': 50,
            'height.px': 50}"
            
        [style.background-image]="getImage(place.stack)"
        style="background-size: cover; display: inline-block">
            
        </span>
    </div>
</div>`
})

export class CreaturesQueueComponent {
    creatures: QueuePlace[];

	onChoose(stack: CreatureStack): void{
		this.gameEngine.chooseStack(stack);
	}

    constructor(private gameEngine: GameEngine, private sanitizer: DomSanitizer) {
        this.gameEngine.commandChain()
            .filter(command => typeof this[command.type] === "function")
            .subscribe(command =>this[command.type](command));
    }

    creatureQueue(command: CreatureQueueCommand): void {
        this.creatures = command.queue;
    }

    getImage(stack: CreatureStack) {
        return stack &&
            this.sanitizer.bypassSecurityTrustStyle("url('" + stack.creature.image + "')");
    }
}