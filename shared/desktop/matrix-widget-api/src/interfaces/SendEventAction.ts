import { IWidgetApiRequest, IWidgetApiRequestData } from "./IWidgetApiRequest";
import { WidgetApiFromWidgetAction, WidgetApiToWidgetAction } from "./WidgetApiAction";
import { IWidgetApiResponseData } from "./IWidgetApiResponse";
import { IRoomEvent } from "./IRoomEvent";

export interface ISendEventFromWidgetRequestData extends IWidgetApiRequestData {
    state_key?: string; // eslint-disable-line camelcase
    type: string;
    content: unknown;
    room_id?: string; // eslint-disable-line camelcase
}

export interface ISendEventFromWidgetActionRequest extends IWidgetApiRequest {
    action: WidgetApiFromWidgetAction.SendEvent;
    data: ISendEventFromWidgetRequestData;
}

export interface ISendEventFromWidgetResponseData extends IWidgetApiResponseData {
    room_id: string; // eslint-disable-line camelcase
    event_id: string; // eslint-disable-line camelcase
}

export interface ISendEventFromWidgetActionResponse extends ISendEventFromWidgetActionRequest {
    response: ISendEventFromWidgetResponseData;
}

export interface ISendEventToWidgetRequestData extends IWidgetApiRequestData, IRoomEvent {
}

export interface ISendEventToWidgetActionRequest extends IWidgetApiRequest {
    action: WidgetApiToWidgetAction.SendEvent;
    data: ISendEventToWidgetRequestData;
}

export interface ISendEventToWidgetResponseData extends IWidgetApiResponseData {
    // nothing
}

export interface ISendEventToWidgetActionResponse extends ISendEventToWidgetActionRequest {
    response: ISendEventToWidgetResponseData;
}
