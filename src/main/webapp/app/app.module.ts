import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { RouterModule }   from '@angular/router';

import { AppComponent }        from './app.component';
import { HeroDetailsComponent } from './hero-details.component';
import { HeroesComponent }     from './heroes.component';
import { HeroesService }         from './mock-heroes';
import {MyDashboardComponent} from "./dashboard.component";

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        RouterModule.forRoot([
            {
                path: 'heroes',
                component: HeroesComponent
            },
            {
                path: 'dashboard',
                component: MyDashboardComponent
            },
            {
                path: "",
                redirectTo: "/dashboard",
                pathMatch: "full"
            }
        ])
    ],
    declarations: [
        AppComponent,
        HeroDetailsComponent,
        HeroesComponent,
        MyDashboardComponent
    ],
    providers: [
        HeroesService
    ],
    bootstrap: [ AppComponent ]
})
export class AppModule {
}
