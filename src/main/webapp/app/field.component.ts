import {Component} from "@angular/core";

@Component({
    template: `
<table>
  <tr *ngFor="let line of matrix">
    <td *ngFor="let number of line">...</td>
  </tr>
</table>`
})


export class FieldComponent {
    matrix: number[][] = new Array()

    constructor() {
        for (var lineNumber = 0; lineNumber < 10; lineNumber++) {
            this.matrix.push(new Array(10))
        }
    }
}