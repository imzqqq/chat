import { WidgetApiDirection } from "./WidgetApiDirection";
import { WidgetApiAction } from "./WidgetApiAction";

export interface IWidgetApiRequestData {
    [key: string]: unknown;
}

export interface IWidgetApiRequestEmptyData extends IWidgetApiRequestData {
    // nothing
}

export interface IWidgetApiRequest {
    api: WidgetApiDirection;
    requestId: string;
    action: WidgetApiAction;
    widgetId: string;
    data: IWidgetApiRequestData;
}
