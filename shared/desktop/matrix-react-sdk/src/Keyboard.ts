export const Key = {
    HOME: "Home",
    END: "End",
    PAGE_UP: "PageUp",
    PAGE_DOWN: "PageDown",
    BACKSPACE: "Backspace",
    DELETE: "Delete",
    ARROW_UP: "ArrowUp",
    ARROW_DOWN: "ArrowDown",
    ARROW_LEFT: "ArrowLeft",
    ARROW_RIGHT: "ArrowRight",
    TAB: "Tab",
    ESCAPE: "Escape",
    ENTER: "Enter",
    ALT: "Alt",
    CONTROL: "Control",
    META: "Meta",
    SHIFT: "Shift",
    CONTEXT_MENU: "ContextMenu",

    COMMA: ",",
    PERIOD: ".",
    LESS_THAN: "<",
    GREATER_THAN: ">",
    BACKTICK: "`",
    SPACE: " ",
    SLASH: "/",
    SQUARE_BRACKET_LEFT: "[",
    SQUARE_BRACKET_RIGHT: "]",
    A: "a",
    B: "b",
    C: "c",
    D: "d",
    E: "e",
    F: "f",
    G: "g",
    H: "h",
    I: "i",
    J: "j",
    K: "k",
    L: "l",
    M: "m",
    N: "n",
    O: "o",
    P: "p",
    Q: "q",
    R: "r",
    S: "s",
    T: "t",
    U: "u",
    V: "v",
    W: "w",
    X: "x",
    Y: "y",
    Z: "z",
};

export const isMac = navigator.platform.toUpperCase().indexOf('MAC') >= 0;

export function isOnlyCtrlOrCmdKeyEvent(ev) {
    if (isMac) {
        return ev.metaKey && !ev.altKey && !ev.ctrlKey && !ev.shiftKey;
    } else {
        return ev.ctrlKey && !ev.altKey && !ev.metaKey && !ev.shiftKey;
    }
}

export function isOnlyCtrlOrCmdIgnoreShiftKeyEvent(ev) {
    if (isMac) {
        return ev.metaKey && !ev.altKey && !ev.ctrlKey;
    } else {
        return ev.ctrlKey && !ev.altKey && !ev.metaKey;
    }
}
