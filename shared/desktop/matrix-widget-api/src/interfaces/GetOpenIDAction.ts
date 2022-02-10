import { IWidgetApiRequest, IWidgetApiRequestData } from "./IWidgetApiRequest";
import { WidgetApiFromWidgetAction } from "./WidgetApiAction";
import { IWidgetApiResponseData } from "./IWidgetApiResponse";

export enum OpenIDRequestState {
    Allowed = "allowed",
    Blocked = "blocked",
    PendingUserConfirmation = "request",
}

export interface IOpenIDCredentials {
    access_token?: string; // eslint-disable-line camelcase
    expires_in?: number; // eslint-disable-line camelcase
    matrix_server_name?: string; // eslint-disable-line camelcase
    token_type?: "Bearer" | string; // eslint-disable-line camelcase
}

export interface IGetOpenIDActionRequestData extends IWidgetApiRequestData {
    // nothing
}

export interface IGetOpenIDActionRequest extends IWidgetApiRequest {
    action: WidgetApiFromWidgetAction.GetOpenIDCredentials;
    data: IGetOpenIDActionRequestData;
}

export interface IGetOpenIDActionResponseData extends IWidgetApiResponseData, IOpenIDCredentials {
    state: OpenIDRequestState;
}

export interface IGetOpenIDActionResponse extends IGetOpenIDActionRequest {
    response: IGetOpenIDActionResponseData;
}
