import {Component, OnInit} from "@angular/core";
import {GameService} from "../game.service/game.service";
import {CreatureStack} from "../model/creatureStack";
import {Router} from "@angular/router";
import {Http} from "@angular/http";

@Component({
    selector: "my-app",
    template: `<button (click)="click()">Start Game</button>
    <!--<creatures [(creatures)]="creaturesChoice"></creatures>-->`
})


export class StartPageComponent implements OnInit{
    ngOnInit(): void {
        this.http.get("/creature").toPromise().then(
            responce => {
                this.creaturesChoice = responce.json()[0].map(creature => {
                    var creatureChoice : CreatureStack = new CreatureStack();
                    creatureChoice.size = 10;
                    creatureChoice.creature = creature;
                    return creatureChoice;
                });
            })
            .then(()=>this.click())
    }
    creaturesChoice: Array<CreatureStack>;


    click(): void {
        var body =  this.creaturesChoice.reduce((result, choice: CreatureStack)=>{
            if(choice.size!=0){
                result.push(choice)
            }
            return result;
        }, new Array());
        this.gameService.startMessage(body).then(()=>{
            this.router.navigate(["field"])
        });
    }

    constructor(private gameService: GameService, private router: Router, private http: Http) {}
}