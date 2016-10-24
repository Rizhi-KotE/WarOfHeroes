export interface Command {
    type: string;
    complete(x: number, y: number);
}