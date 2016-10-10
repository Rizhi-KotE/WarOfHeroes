import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {AppComponent} from "./app.component";
import {WebSocketService} from "./websocket.service";
import {StompService} from "./stomp.service";


@NgModule({
    imports: [
        BrowserModule,
    ],
    providers: [
        WebSocketService,
        StompService
    ],
    declarations: [
        AppComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
