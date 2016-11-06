import {Component} from "@angular/core";

import {CreatureStack} from "../model/creatureStack";
import {GameService} from "../game.service/game.service";
import {Creature} from "../model/creature";
import {Router} from "@angular/router";
@Component({
    selector: "creatures",
    template: `
<div>
    <button class="btn btn-primary" style="display: inline-block" (click)="gatherGroup()">Собрать отряд</button>
    <div class="dropdown" style="display: inline-block">
        <button class="btn btn-warning dropdown-toggle" type="button" data-toggle="dropdown">{{currentRace}}
        <span class="caret"></span></button>
        <ul class="dropdown-menu">
            <li *ngFor="let race of getRaces()"><a href="#" (click)="setRace(race)">{{race}}</a></li>
        </ul>
    </div>
    <div class="container">
        <div *ngFor="let stack of currentCreatures" style="display: inline-block; width: 30%">
            <img [src]="stack.creature.image" style="left: 50%; width: 96px;margin-left: 48px;">
            <div>
                <div>{{stack.size}}</div>
                <md-slider min="0" [max]="getMaxStackSize(stack)" [(ngModel)]="stack.size"></md-slider>
                <span>{{getMaxStackSize(stack)}}</span>
            </div>
        </div>
    </div>
</div>`
})
export class CreatureComponent{
    races: Map<string, Creature[]>;

    maxHp = 1000;

    getMaxStackSize(currentStack: CreatureStack): number {
        var filled = this.currentCreatures.reduce((number, stack)=>number += currentStack === stack ?
            0 : stack.size * stack.creature.health, 0);
        var stackSize = (this.maxHp - filled) / currentStack.creature.health;
        return Math.round(stackSize < 0 ? 0 : stackSize);
    }

    currentRace: string;

    currentCreatures: CreatureStack[] = [];

    constructor(private gameService: GameService, private router: Router) {
        gameService.getCreaturesRaces().then(responce => {
            this.races = responce;
            this.setRace(Object.keys(this.races)[0]);
        });
    }

    gatherGroup(): void {
        var creaturesChoice = this.currentCreatures.filter(stack => stack.size > 0);
        this.gameService.startMessage(creaturesChoice).then(()=> {
            this.router.navigate(["field"])
        });
    }

    getRaces(): string[] {
        return this.races ? Object.keys(this.races) : [];
    }

    setRace(race: string): void {
        this.currentRace = race;
        this.currentCreatures = this.races[race].map(creature => new CreatureStack(creature, 0));
    }
}