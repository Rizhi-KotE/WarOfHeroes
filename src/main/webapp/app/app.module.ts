import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {AppComponent} from "./app.component";
import {WebSocketService} from "./websocket.service";
import {StompService} from "./stomp.service";
import {StartPageComponent} from "./start-page.component";
import {FieldComponent} from "./field.component";


@NgModule({
    imports: [
        BrowserModule,
        RouterModule.forRoot([
            {
                path: "",
                component: StartPageComponent
            },
            {
                path: "field",
                component: FieldComponent
            }
        ])
    ],
    providers: [
        WebSocketService,
        StompService
    ],
    declarations: [
        AppComponent, StartPageComponent, FieldComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
