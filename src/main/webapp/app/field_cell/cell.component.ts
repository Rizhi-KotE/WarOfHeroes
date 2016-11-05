import {Component, Input, Output, EventEmitter} from "@angular/core";
import {Cell} from "../model/Cell";
import {DomSanitizer} from "@angular/platform-browser"
import Rx from "rxjs"

@Component({
    template: `
    <div 
    [ngStyle]="{
        'width.px': 50,
        'height.px': 50,
        'position': 'absolute',
        'top.px': 50*cell.y,
        'left.px': 50*cell.x}"
    [style.background-image]="getImage()"
    style="background-size: cover"
    [ngClass]="{
        cell: true, 
        choose: chosen, 
        available: cell.available, 
        availableEnemy: cell.availableEnemy, 
        highlited: highlited,
        yourTurn: cell.yourTurn}" 
    (click)="onChoose()"
    (mouseenter)="highlited=true"
    (mouseleave)="highlited=false">
        <span *ngIf="cell.stack" class="stackSize" style="position: absolute; bottom: 0px; right: 0px">{{cell.stack.size}}</span>
    </div>`,
    selector: "cell"
})


export class CellComponent {
    @Input()
    cell: Cell;

    getImage() {
        return this.cell &&
            this.cell.stack &&
            this.sanitizer.bypassSecurityTrustStyle("url('" + this.cell.stack.creature.image + "')");
    }

    @Output()
    choose: EventEmitter<Cell> = new EventEmitter<Cell>();

    onChoose() {
        if (this.cell.stack || this.cell.available) {
            this.choose.emit(this.cell);
        }
    }

    constructor(private sanitizer: DomSanitizer) {
    }

    mouseEnter = false;
    highlited = false;
}