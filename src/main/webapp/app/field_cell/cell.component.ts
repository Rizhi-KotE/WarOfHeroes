import {Component, Input, Output, EventEmitter} from "@angular/core";
import {Cell} from "../model/Cell";
import {DomSanitizer} from "@angular/platform-browser"

@Component({
    styleUrls: ['app/field_cell/cell.component.css'],
    template: `
    <div
    style="
    width: 50px;
    height: 50px;
    position:relative;"
    class="cell"
    (click)="onChoose()"
    (mouseenter)="highlited=true"
    (mouseleave)="highlited=false">
        <div [ngClass]="{enemy: cell.availableEnemy, current: cell.yourTurn}" class="second"></div>
        <div [ngClass]="{enemy: cell.availableEnemy, current: cell.yourTurn}" class="first"></div>
        <div [ngClass]="{
        choose: chosen, 
        available: cell.available,
        highlited: highlited}" 
        [style.background-image]="getImage()" 
        class="shield" style="background-size: cover">
            <span *ngIf="cell.stack" class="stackSize" style="position: absolute; bottom: 0px; right: 0px">
                {{cell.stack.size}}
            </span>
        </div>
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