import {Component} from "@angular/core";

import {CreatureStack} from "../model/creatureStack";
import {GameService} from "../game.service/game.service";
import {Creature} from "../model/creature";
import {Router} from "@angular/router";
import {Race} from "../model/Race";
@Component({
    styleUrls: ["app/creatures.component/creature.component.css"],
    selector: "creatures",
    template: `
<div>
    <div class="race_card">
        <div *ngFor="let stack of getCurrentStacks()" class="card_place">
            <card [stack]="stack"></card>
        </div>
    </div>
    <div style="display: inline-block;">
        <div class="race_list" style="display: inline-block">
            <img 
            *ngFor="let race of races" 
            [src]="race.image" 
            class="race_image" 
            style="display: block" 
            (click)="currentRace = race">
            <button class="gather_group" (click)="gatherGroup()">Собрать отряд</button>
        </div>
    </div>
</div>`
})
export class CreatureComponent{
    races: Race[];

    currentRace: Race;

    constructor(private gameService: GameService, private router: Router) {
        gameService.getCreaturesRaces().then(responce => {
            this.races = responce;
            this.setRace(this.races[0]);
        });
    }

    gatherGroup(): void {
        var creaturesChoice = this.currentRace.creatures
            .map(creature => new CreatureStack(creature, creature.increase * 2))
        this.gameService.startMessage(creaturesChoice).then(()=> {
            this.router.navigate(["field"])
        });
    }

    getRaces(): string[] {
        return this.races ? Object.keys(this.races) : [];
    }

    setRace(race: Race): void {
        this.currentRace = race;
    }

    getCurrentStacks(): CreatureStack[] {
        return this.currentRace ? this.currentRace.creatures
            .map(creature => new CreatureStack(creature, creature.increase * 2)) : []
    }
}