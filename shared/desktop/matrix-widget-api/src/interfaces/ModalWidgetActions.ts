import { IWidgetApiRequest, IWidgetApiRequestData } from "./IWidgetApiRequest";
import { WidgetApiFromWidgetAction, WidgetApiToWidgetAction } from "./WidgetApiAction";
import { IWidgetApiAcknowledgeResponseData, IWidgetApiResponse } from "./IWidgetApiResponse";
import { IWidget } from "./IWidget";
import { ModalButtonKind } from "./ModalButtonKind";

export enum BuiltInModalButtonID {
    Close = "m.close",
}
export type ModalButtonID = BuiltInModalButtonID | string;

export interface IModalWidgetCreateData extends IWidgetApiRequestData {
    [key: string]: unknown;
}

export interface IModalWidgetReturnData {
    [key: string]: unknown;
}

// Types for a normal modal requesting the opening a modal widget
export interface IModalWidgetOpenRequestDataButton {
    id: ModalButtonID;
    label: string;
    kind: ModalButtonKind | string;
    disabled?: boolean;
}

export interface IModalWidgetOpenRequestData extends IModalWidgetCreateData, Omit<IWidget, "id" | "creatorUserId"> {
    buttons?: IModalWidgetOpenRequestDataButton[];
}

export interface IModalWidgetOpenRequest extends IWidgetApiRequest {
    action: WidgetApiFromWidgetAction.OpenModalWidget;
    data: IModalWidgetOpenRequestData;
}

export interface IModalWidgetOpenResponse extends IWidgetApiResponse {
    response: IWidgetApiAcknowledgeResponseData;
}

// Types for a modal widget receiving notifications that its buttons have been pressed
export interface IModalWidgetButtonClickedRequestData extends IWidgetApiRequestData {
    id: IModalWidgetOpenRequestDataButton["id"];
}

export interface IModalWidgetButtonClickedRequest extends IWidgetApiRequest {
    action: WidgetApiToWidgetAction.ButtonClicked;
    data: IModalWidgetButtonClickedRequestData;
}

export interface IModalWidgetButtonClickedResponse extends IWidgetApiResponse {
    response: IWidgetApiAcknowledgeResponseData;
}

// Types for a modal widget requesting close
export interface IModalWidgetCloseRequest extends IWidgetApiRequest {
    action: WidgetApiFromWidgetAction.CloseModalWidget;
    data: IModalWidgetReturnData;
}

export interface IModalWidgetCloseResponse extends IWidgetApiResponse {
    response: IWidgetApiAcknowledgeResponseData;
}

// Types for a normal widget being notified that the modal widget it opened has been closed
export interface IModalWidgetCloseNotificationRequest extends IWidgetApiRequest {
    action: WidgetApiToWidgetAction.CloseModalWidget;
    data: IModalWidgetReturnData;
}

export interface IModalWidgetCloseNotificationResponse extends IWidgetApiResponse {
    response: IWidgetApiAcknowledgeResponseData;
}
