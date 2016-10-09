import {Component, OnInit} from "@angular/core";
import {Hero} from './hero'
import {HeroesService} from "./mock-heroes"

@Component({
    styleUrls: ['app/app.styles.css'],
    selector: 'my-heroes',
    template: `
    <h1>{{title}}</h1>
    <my-hero-details [hero]="selectedHero"></my-hero-details>
    <ul class="heroes">
        <li *ngFor="let hero of heroes" (click)="onSelect(hero)"
            [class.selected]="hero == selectedHero">
            <span class="badge">{{hero.id}}</span> {{hero.name}}
        </li>
    </ul>`

})

export class HeroesComponent implements OnInit{
    ngOnInit(): void {
        this.getHeroes();
    }
    constructor(private heroesService: HeroesService){}
    title = 'Tour of Heroes';
    selectedHero: Hero;
    heroes: Hero[];
    onSelect(hero: Hero): void {
        this.selectedHero = hero;
    }
    getHeroes(): void{
        this.heroesService.getHeroes().then(heroes => this.heroes = heroes)
    }
}