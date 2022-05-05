
import { IWidgetApiRequest, IWidgetApiRequestData } from "./IWidgetApiRequest";
import { WidgetApiToWidgetAction } from "./WidgetApiAction";
import { IWidgetApiResponseData } from "./IWidgetApiResponse";
import { IOpenIDCredentials, OpenIDRequestState } from "./GetOpenIDAction";

export interface IOpenIDCredentialsActionRequestData extends IWidgetApiRequestData, IOpenIDCredentials {
    state: OpenIDRequestState;
    original_request_id: string; // eslint-disable-line camelcase
}

export interface IOpenIDCredentialsActionRequest extends IWidgetApiRequest {
    action: WidgetApiToWidgetAction.OpenIDCredentials;
    data: IOpenIDCredentialsActionRequestData;
}

export interface IOpenIDCredentialsActionResponseData extends IWidgetApiResponseData {
    // nothing
}

export interface IOpenIDCredentialsIDActionResponse extends IOpenIDCredentialsActionRequest {
    response: IOpenIDCredentialsActionResponseData;
}
