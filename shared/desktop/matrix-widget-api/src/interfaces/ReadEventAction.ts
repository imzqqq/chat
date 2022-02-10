import { IWidgetApiRequest, IWidgetApiRequestData } from "./IWidgetApiRequest";
import { WidgetApiFromWidgetAction } from "./WidgetApiAction";
import { IWidgetApiResponseData } from "./IWidgetApiResponse";
import { Symbols } from "../Symbols";

export interface IReadEventFromWidgetRequestData extends IWidgetApiRequestData {
    state_key?: string | boolean; // eslint-disable-line camelcase
    msgtype?: string;
    type: string;
    limit?: number;
    room_ids?: Symbols.AnyRoom | string[]; // eslint-disable-line camelcase
}

export interface IReadEventFromWidgetActionRequest extends IWidgetApiRequest {
    action: WidgetApiFromWidgetAction.MSC2876ReadEvents;
    data: IReadEventFromWidgetRequestData;
}

export interface IReadEventFromWidgetResponseData extends IWidgetApiResponseData {
    events: unknown[];
}

export interface IReadEventFromWidgetActionResponse extends IReadEventFromWidgetActionRequest {
    response: IReadEventFromWidgetResponseData;
}
