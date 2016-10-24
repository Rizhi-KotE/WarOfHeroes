import {Component, Input} from "@angular/core";

import {CreatureStack} from "../model/creatureStack";
@Component({
    selector: "creaturesQueue",
    templateUrl: "/app/creatures.component/creature.component.html"
})
export class CreatureComponent{
    @Input()
    creatures: Array<CreatureStack>;
}