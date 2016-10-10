import {Injectable} from '@angular/core'
import {Stomp} from "stompjs";
import SockJS from "sockjs-client";


@Injectable()
export class StompService {
    stompClient: any;
    connect(): stompClient {
        var ws = new SockJS("/battle");
        this.stompClient = Stomp.over(ws);
        this.stompClient.connect({}, frame => {
            console.log(frame);
        });
        return this.stompClient;
    }
}