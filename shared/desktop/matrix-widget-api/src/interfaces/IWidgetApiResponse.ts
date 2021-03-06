import { IWidgetApiRequest } from "./IWidgetApiRequest";

export interface IWidgetApiResponseData {
    [key: string]: unknown;
}

export interface IWidgetApiAcknowledgeResponseData extends IWidgetApiResponseData {
    // nothing
}

export interface IWidgetApiResponse extends IWidgetApiRequest {
    response: IWidgetApiResponseData;
}
