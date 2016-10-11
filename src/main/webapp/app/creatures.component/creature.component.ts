import {Component, Input} from "@angular/core";
import 'hammerjs'
import {CreatureStack} from "../model/creatureStack";

@Component({
    selector: "creatures",
    templateUrl: "/app/creatures.component/creature.component.html"
})
export class CreatureComponent{
    @Input()
    creatures: Array<CreatureStack>;
}