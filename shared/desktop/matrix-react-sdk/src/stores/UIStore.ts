import EventEmitter from "events";
// XXX: resize-observer-polyfill has types that now conflict with typescript's
// own DOM types: https://github.com/que-etc/resize-observer-polyfill/issues/80
// Using require here rather than import is a horrenous workaround. We should
// be able to remove the polyfill once Safari 14 is released.
const ResizeObserverPolyfill = require('resize-observer-polyfill'); // eslint-disable-line @typescript-eslint/no-var-requires
import ResizeObserverEntry from 'resize-observer-polyfill/src/ResizeObserverEntry';

export enum UI_EVENTS {
    Resize = "resize"
}

export type ResizeObserverCallbackFunction = (entries: ResizeObserverEntry[]) => void;

export default class UIStore extends EventEmitter {
    private static _instance: UIStore = null;

    private resizeObserver: ResizeObserver;

    private uiElementDimensions = new Map<string, DOMRectReadOnly>();
    private trackedUiElements = new Map<Chat, string>();

    public windowWidth: number;
    public windowHeight: number;

    constructor() {
        super();

        // eslint-disable-next-line no-restricted-properties
        this.windowWidth = window.innerWidth;
        // eslint-disable-next-line no-restricted-properties
        this.windowHeight = window.innerHeight;

        this.resizeObserver = new ResizeObserverPolyfill(this.resizeObserverCallback);
        this.resizeObserver.observe(document.body);
    }

    public static get instance(): UIStore {
        if (!UIStore._instance) {
            UIStore._instance = new UIStore();
        }
        return UIStore._instance;
    }

    public static destroy(): void {
        if (UIStore._instance) {
            UIStore._instance.resizeObserver.disconnect();
            UIStore._instance.removeAllListeners();
            UIStore._instance = null;
        }
    }

    public getElementDimensions(name: string): DOMRectReadOnly {
        return this.uiElementDimensions.get(name);
    }

    public trackElementDimensions(name: string, element: Chat): void {
        this.trackedUiElements.set(element, name);
        this.resizeObserver.observe(element);
    }

    public stopTrackingElementDimensions(name: string): void {
        let trackedElement: Chat;
        this.trackedUiElements.forEach((trackedElementName, element) => {
            if (trackedElementName === name) {
                trackedElement = element;
            }
        });
        if (trackedElement) {
            this.resizeObserver.unobserve(trackedElement);
            this.uiElementDimensions.delete(name);
            this.trackedUiElements.delete(trackedElement);
        }
    }

    public isTrackingElementDimensions(name: string): boolean {
        return this.uiElementDimensions.has(name);
    }

    private resizeObserverCallback = (entries: ResizeObserverEntry[]) => {
        const windowEntry = entries.find(entry => entry.target === document.body);

        if (windowEntry) {
            this.windowWidth = windowEntry.contentRect.width;
            this.windowHeight = windowEntry.contentRect.height;
        }

        entries.forEach(entry => {
            const trackedElementName = this.trackedUiElements.get(entry.target);
            if (trackedElementName) {
                this.uiElementDimensions.set(trackedElementName, entry.contentRect);
                this.emit(trackedElementName, UI_EVENTS.Resize, entry);
            }
        });

        this.emit(UI_EVENTS.Resize, entries);
    };
}

window.mxUIStore = UIStore.instance;
