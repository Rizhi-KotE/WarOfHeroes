import {Injectable} from "@angular/core";
import {Stomp} from "stompjs";
import SockJS from "sockjs-client";

@Injectable()
export class StompService {
    stompClientPromise: Promise<any>;

    connect(): void {
        this.stompClientPromise = new Promise((resolve, reject)=> {
            var ws = new SockJS("/battle");
            var stompClient = Stomp.over(ws);
            stompClient.connect({}, frame => {
                console.log(frame);
                resolve(stompClient);
            });
        })
    }

    send(destination: string, body?: Object): void {
        if(!this.stompClientPromise){
            this.connect();
        }
        this.stompClientPromise.then(client  => client.send(destination, {}, JSON.stringify(body)));
    }

    subscribe(destination: string, callback: Function): void {
        if(!this.stompClientPromise){
            this.connect();
        }
        this.stompClientPromise.then(client  => client.subscribe(destination, callback))
    }
}