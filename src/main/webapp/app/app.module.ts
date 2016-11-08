import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {AppComponent} from "./app.component";
import {WebSocketService} from "./websocket.service";
import {StompService} from "./stomp.service";
import {FieldComponent} from "./field.component/field.component";
import {MaterialModule} from "@angular/material";
import {CreatureComponent} from "./creatures.component/creature.component";
import {GameService} from "./game.service/game.service";
import {CreaturesQueueComponent} from "./creatures_queue/creatures_queue";
import {CreatureStackComponent} from "./creature_stack.component/creature_stack.component";
import {GameEngine} from "./game_engine/game_engine.service";
import {CellComponent} from "./field_cell/cell.component";
import {CreatureCardComponent} from "./creature_card/creature_card.component";


@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        RouterModule.forRoot([
            {
                path: "",
                component: CreatureComponent
            },
            {
                path: "field",
                component: FieldComponent
            }
        ],
            {
                useHash: true
            }),
        MaterialModule.forRoot(),
    ],
    providers: [
        WebSocketService,
        StompService,
        GameService,
        GameEngine
    ],
    declarations: [
        AppComponent,
        FieldComponent,
        CreatureComponent,
        CellComponent,
        CreaturesQueueComponent,
        CreatureStackComponent,
        CreatureCardComponent
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
