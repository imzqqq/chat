// This is intended to fix re-resizer because of its unguarded `instanceof TouchEvent` checks.
export function polyfillTouchEvent() {
    // Firefox doesn't have touch events without touch devices being present, so create a fake
    // one we can rely on lying about.
    if (!window.TouchEvent) {
        // We have no intention of actually using this, so just lie.
        window.TouchEvent = class TouchEvent extends UIEvent {
            public get altKey(): boolean { return false; }
            public get changedTouches(): any { return []; }
            public get ctrlKey(): boolean { return false; }
            public get metaKey(): boolean { return false; }
            public get shiftKey(): boolean { return false; }
            public get targetTouches(): any { return []; }
            public get touches(): any { return []; }
            public get rotation(): number { return 0.0; }
            public get scale(): number { return 0.0; }
            constructor(eventType: string, params?: any) {
                super(eventType, params);
            }
        };
    }
}
