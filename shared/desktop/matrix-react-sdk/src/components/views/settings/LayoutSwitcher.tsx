import React from "react";
import classNames from "classnames";
import SettingsStore from "../../../settings/SettingsStore";
import EventTilePreview from "../elements/EventTilePreview";
import StyledRadioButton from "../elements/StyledRadioButton";
import { _t } from "../../../languageHandler";
import { Layout } from "../../../settings/Layout";
import { SettingLevel } from "../../../settings/SettingLevel";
import SettingsFlag from "../elements/SettingsFlag";

interface IProps {
    userId?: string;
    displayName: string;
    avatarUrl: string;
    messagePreviewText: string;
    onLayoutChanged?: (layout: Layout) => void;
}

interface IState {
    layout: Layout;
    adaptiveSideBubbles: boolean;
}

export default class LayoutSwitcher extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            layout: SettingsStore.getValue("layout"),
            adaptiveSideBubbles: SettingsStore.getValue("adaptiveSideBubbles"),
        };
    }

    private onLayoutChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
        const layout = e.target.value as Layout;

        this.setState({ layout: layout });
        SettingsStore.setValue("layout", null, SettingLevel.DEVICE, layout);
        this.props.onLayoutChanged(layout);
    };

    public render(): JSX.Chat {
        const ircClasses = classNames("mx_LayoutSwitcher_RadioButton", {
            mx_LayoutSwitcher_RadioButton_selected: this.state.layout == Layout.IRC,
        });
        const groupClasses = classNames("mx_LayoutSwitcher_RadioButton", {
            mx_LayoutSwitcher_RadioButton_selected: this.state.layout == Layout.Group,
        });
        const bubbleClasses = classNames("mx_LayoutSwitcher_RadioButton", {
            mx_LayoutSwitcher_RadioButton_selected: this.state.layout === Layout.Bubble,
        });

        return <>
            <div className="mx_SettingsTab_heading">{ _t("Message layout") }</div>
            <div className="mx_SettingsTab_section mx_LayoutSwitcher">
                <div className="mx_LayoutSwitcher_RadioButtons">
                    <label className={ircClasses}>
                        <EventTilePreview
                            className="mx_LayoutSwitcher_RadioButton_preview"
                            message={this.props.messagePreviewText}
                            layout={Layout.IRC}
                            userId={this.props.userId}
                            displayName={this.props.displayName}
                            avatarUrl={this.props.avatarUrl}
                        />
                        <StyledRadioButton
                            name="layout"
                            value={Layout.IRC}
                            checked={this.state.layout === Layout.IRC}
                            onChange={this.onLayoutChange}
                        >
                            { _t("IRC") }
                        </StyledRadioButton>
                    </label>
                    <label className={groupClasses}>
                        <EventTilePreview
                            className="mx_LayoutSwitcher_RadioButton_preview"
                            message={this.props.messagePreviewText}
                            layout={Layout.Group}
                            userId={this.props.userId}
                            displayName={this.props.displayName}
                            avatarUrl={this.props.avatarUrl}
                        />
                        <StyledRadioButton
                            name="layout"
                            value={Layout.Group}
                            checked={this.state.layout == Layout.Group}
                            onChange={this.onLayoutChange}
                        >
                            { _t("Modern") }
                        </StyledRadioButton>
                    </label>
                    <label className={bubbleClasses}>
                        <EventTilePreview
                            className="mx_LayoutSwitcher_RadioButton_preview"
                            message={this.props.messagePreviewText}
                            layout={Layout.Bubble}
                            userId={this.props.userId}
                            displayName={this.props.displayName}
                            avatarUrl={this.props.avatarUrl}
                        />
                        <StyledRadioButton
                            name="layout"
                            value={Layout.Bubble}
                            checked={this.state.layout == Layout.Bubble}
                            onChange={this.onLayoutChange}
                        >
                            { _t("Message bubbles") }
                        </StyledRadioButton>
                    </label>
                </div>

                <div className="mx_LayoutSwitcher_Checkboxes">
                    { this.state.layout === Layout.Group ?
                        <SettingsFlag
                            name="useCompactLayout"
                            level={SettingLevel.DEVICE}
                            useCheckbox={true}
                        /> : null
                    }
                    { this.state.layout === Layout.Bubble ?
                        <SettingsFlag
                            name="singleSideBubbles"
                            level={SettingLevel.DEVICE}
                            useCheckbox={true}
                            disabled={this.state.adaptiveSideBubbles}
                        /> : null
                    }
                    { this.state.layout === Layout.Bubble ?
                        <SettingsFlag
                            name="adaptiveSideBubbles"
                            level={SettingLevel.DEVICE}
                            useCheckbox={true}
                            onChange={(checked) => this.setState({ adaptiveSideBubbles: checked })}
                        /> : null
                    }
                </div>
            </div>
        </>;
    }
}
