import {Component} from "@angular/core";

@Component({
    selector: "my-app",
    template: `
<h1>War of Heroes</h1>
<a routerLink="/field">field</a>
<router-outlet></router-outlet>`
})


export class AppComponent {
}