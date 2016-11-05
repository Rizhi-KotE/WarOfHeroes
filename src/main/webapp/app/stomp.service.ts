import {Injectable} from "@angular/core";
import {Stomp} from "stompjs";
import SockJS from "sockjs-client";

@Injectable()
export class StompService {
    stompClientPromise: Promise<any>;

    connect(): void {
        this.stompClientPromise = new Promise((resolve, reject)=> {
            var ws = new SockJS("/game/connect");
            var stompClient = Stomp.over(ws);
            var numbers = 5;
            this.connectToSocket(stompClient)
                .then(client => resolve(client))
                .catch(()=>this.connectToSocket(stompClient)
                    .then(client=>resolve(client)))
        })
    }

    connectToSocket(stompClient: any): Promise<any> {
        return new Promise((resolve, reject)=> {
            stompClient.connect({}, frame => {
                console.log(frame);
                resolve(stompClient);
            }, frame=> reject());
        })
    }

    send(destination: string, body?: Object): void {
        if(!this.stompClientPromise){
            this.connect();
        }
        this.stompClientPromise.then(client  => client.send(destination, {}, JSON.stringify(body)));
    }

    subscribe(destination: string, callback: Function): Promise<any> {
        if(!this.stompClientPromise){
            this.connect();
        }
        return this.stompClientPromise.then(client  => client.subscribe(destination, callback))
    }
}