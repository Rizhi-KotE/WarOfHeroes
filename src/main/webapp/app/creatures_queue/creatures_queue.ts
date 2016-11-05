import {Component, Input, Output, EventEmitter} from "@angular/core"
import {CreatureStack} from "../model/creatureStack";
import {GameEngine} from "../game_engine/game_engine.service";

@Component({
	selector: "creatures_queue",
	template: `
<div>
	<div *ngFor="let stack of creatures">
		<stack [stack]="stack" (click)="onChoose(stack)"></stack>
	</div>
</div>`
})

export class CreaturesQueueComponent {
	creatures: CreatureStack[];

	onChoose(stack: CreatureStack): void{
		this.gameEngine.chooseStack(stack);
	}

	constructor(private gameEngine: GameEngine){
	    this.gameEngine.getCreaturesObservable().subscribe(creatures => this.creatures = creatures)
    }
}