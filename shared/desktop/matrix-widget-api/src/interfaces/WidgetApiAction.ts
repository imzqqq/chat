export enum WidgetApiToWidgetAction {
    SupportedApiVersions = "supported_api_versions",
    Capabilities = "capabilities",
    NotifyCapabilities = "notify_capabilities",
    TakeScreenshot = "screenshot",
    UpdateVisibility = "visibility",
    OpenIDCredentials = "openid_credentials",
    WidgetConfig = "widget_config",
    CloseModalWidget = "close_modal",
    ButtonClicked = "button_clicked",
    SendEvent = "send_event",
}

export enum WidgetApiFromWidgetAction {
    SupportedApiVersions = "supported_api_versions",
    ContentLoaded = "content_loaded",
    SendSticker = "m.sticker",
    UpdateAlwaysOnScreen = "set_always_on_screen",
    GetOpenIDCredentials = "get_openid",
    CloseModalWidget = "close_modal",
    OpenModalWidget = "open_modal",
    SetModalButtonEnabled = "set_button_enabled",
    SendEvent = "send_event",

    /**
     * @deprecated It is not recommended to rely on this existing - it can be removed without notice.
     */
    MSC2876ReadEvents = "org.matrix.msc2876.read_events",

    /**
     * @deprecated It is not recommended to rely on this existing - it can be removed without notice.
     */
    MSC2931Navigate = "org.matrix.msc2931.navigate",

    /**
     * @deprecated It is not recommended to rely on this existing - it can be removed without notice.
     */
    MSC2974RenegotiateCapabilities = "org.matrix.msc2974.request_capabilities",
}

export type WidgetApiAction = WidgetApiToWidgetAction | WidgetApiFromWidgetAction | string;
