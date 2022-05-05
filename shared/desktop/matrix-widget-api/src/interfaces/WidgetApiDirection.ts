export enum WidgetApiDirection {
    ToWidget = "toWidget",
    FromWidget = "fromWidget",
}

export function invertedDirection(dir: WidgetApiDirection): WidgetApiDirection {
    if (dir === WidgetApiDirection.ToWidget) {
        return WidgetApiDirection.FromWidget;
    } else if (dir === WidgetApiDirection.FromWidget) {
        return WidgetApiDirection.ToWidget;
    } else {
        throw new Error("Invalid direction");
    }
}
