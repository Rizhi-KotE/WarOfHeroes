import {Component, Input, Output, EventEmitter} from "@angular/core";
import {Cell} from "../model/Cell";
import {DomSanitizer} from "@angular/platform-browser"
import {CreatureStack} from "../model/creatureStack";

@Component({
    styleUrls: ['app/creature_card/creature_card.component.css'],
    template: `
    <div class="card" *ngIf="stack">
		<div class="image">
			<img [src]="stack.creature.image">
		</div>
		<div class="properties">
			<div class="images"></div>
			<div class="pair">
				<span class="key">Атака</span>
				<span class="value">{{stack.creature.attack}}</span>
			</div>
			<div class="pair">
				<span class="key">Защита</span>
				<span class="value">{{stack.creature.defence}}</span>
			</div>
			<div class="pair">
				<span class="key">Урон</span>
				<span class="value">{{stack.creature.damage}}</span>
			</div>
			<div class="pair">
				<span class="key">Здоровье</span>
				<span class="value">{{stack.creature.health}}</span>
			</div>
			<div class="pair">
				<span class="key">Скорость</span>
				<span class="value">{{stack.creature.speed}}</span>
			</div>
		</div>
	</div>`,
    selector: "card"
})

export class CreatureCardComponent {
    @Input()
    stack: CreatureStack;

    @Input()
    inBattle: boolean;
}
