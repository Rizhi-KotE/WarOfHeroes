import {Component, Input} from  "@angular/core"
import {CreatureStack} from "../model/creatureStack";

@Component({
    selector: "stack",
    template: ``
	// <img *ng="!stack" style="{
	// position:absolute;
	// top: 0;
	// left: 0;
	// bottom: 0;
	// right: 0}" [src]="stack.creature.image">
	// <!--<span >{{stack.size}}</span>-->`
})

export class CreatureStackComponent {
    @Input()
    stack: CreatureStack;
}