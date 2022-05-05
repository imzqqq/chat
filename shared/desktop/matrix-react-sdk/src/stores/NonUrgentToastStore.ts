import EventEmitter from "events";
import { ComponentClass } from "../@types/common";
import { UPDATE_EVENT } from "./AsyncStore";

export type ToastReference = symbol;

export default class NonUrgentToastStore extends EventEmitter {
    private static _instance: NonUrgentToastStore;

    private toasts = new Map<ToastReference, ComponentClass>();

    public static get instance(): NonUrgentToastStore {
        if (!NonUrgentToastStore._instance) {
            NonUrgentToastStore._instance = new NonUrgentToastStore();
        }
        return NonUrgentToastStore._instance;
    }

    public get components(): ComponentClass[] {
        return Array.from(this.toasts.values());
    }

    public addToast(c: ComponentClass): ToastReference {
        const ref: ToastReference = Symbol();
        this.toasts.set(ref, c);
        this.emit(UPDATE_EVENT);
        return ref;
    }

    public removeToast(ref: ToastReference) {
        this.toasts.delete(ref);
        this.emit(UPDATE_EVENT);
    }
}
