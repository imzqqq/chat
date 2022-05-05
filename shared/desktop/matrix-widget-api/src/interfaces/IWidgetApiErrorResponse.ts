import { IWidgetApiResponse, IWidgetApiResponseData } from "./IWidgetApiResponse";

export interface IWidgetApiErrorResponseData extends IWidgetApiResponseData {
    error: {
        message: string;
    };
}

export interface IWidgetApiErrorResponse extends IWidgetApiResponse {
    response: IWidgetApiErrorResponseData;
}

export function isErrorResponse(responseData: IWidgetApiResponseData): boolean {
    if ("error" in responseData) {
        const err = <IWidgetApiErrorResponseData>responseData;
        return !!err.error.message;
    }
    return false;
}
