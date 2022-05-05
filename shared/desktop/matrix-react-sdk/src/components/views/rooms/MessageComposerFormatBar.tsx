import React, { createRef } from 'react';
import classNames from 'classnames';

import { _t } from '../../../languageHandler';
import AccessibleTooltipButton from "../elements/AccessibleTooltipButton";
import { replaceableComponent } from "../../../utils/replaceableComponent";

export enum Formatting {
    Bold = "bold",
    Italics = "italics",
    Strikethrough = "strikethrough",
    Code = "code",
    Quote = "quote",
}

interface IProps {
    shortcuts: Partial<Record<Formatting, string>>;
    onAction(action: Formatting): void;
}

interface IState {
    visible: boolean;
}

@replaceableComponent("views.rooms.MessageComposerFormatBar")
export default class MessageComposerFormatBar extends React.PureComponent<IProps, IState> {
    private readonly formatBarRef = createRef<HTMLDivElement>();

    constructor(props: IProps) {
        super(props);
        this.state = { visible: false };
    }

    render() {
        const classes = classNames("mx_MessageComposerFormatBar", {
            "mx_MessageComposerFormatBar_shown": this.state.visible,
        });
        return (<div className={classes} ref={this.formatBarRef}>
            <FormatButton label={_t("Bold")} onClick={() => this.props.onAction(Formatting.Bold)} icon="Bold" shortcut={this.props.shortcuts.bold} visible={this.state.visible} />
            <FormatButton label={_t("Italics")} onClick={() => this.props.onAction(Formatting.Italics)} icon="Italic" shortcut={this.props.shortcuts.italics} visible={this.state.visible} />
            <FormatButton label={_t("Strikethrough")} onClick={() => this.props.onAction(Formatting.Strikethrough)} icon="Strikethrough" visible={this.state.visible} />
            <FormatButton label={_t("Code block")} onClick={() => this.props.onAction(Formatting.Code)} icon="Code" visible={this.state.visible} />
            <FormatButton label={_t("Quote")} onClick={() => this.props.onAction(Formatting.Quote)} icon="Quote" shortcut={this.props.shortcuts.quote} visible={this.state.visible} />
        </div>);
    }

    public showAt(selectionRect: DOMRect): void {
        if (!this.formatBarRef.current) return;

        this.setState({ visible: true });
        const parentRect = this.formatBarRef.current.parentElement.getBoundingClientRect();

        // don't overflow the parent box on the right
        // 6 is an offset that felt ok.
        let left;
        const width = this.formatBarRef.current.clientWidth;
        const rightOffset = 6;
        if (selectionRect.left + width + rightOffset < parentRect.right) {
            left = selectionRect.left - parentRect.left;
        } else {
            left = parentRect.right - parentRect.left - width - rightOffset;
        }
        this.formatBarRef.current.style.left = `${left}px`;

        // 12 is half the height of the bar (e.g. to center it) and 16 is an offset that felt ok.
        this.formatBarRef.current.style.top = `${selectionRect.top - parentRect.top - 16 - 12}px`;
    }

    public hide(): void {
        this.setState({ visible: false });
    }
}

interface IFormatButtonProps {
    label: string;
    icon: string;
    shortcut?: string;
    visible?: boolean;
    onClick(): void;
}

class FormatButton extends React.PureComponent<IFormatButtonProps> {
    render() {
        const className = `mx_MessageComposerFormatBar_button mx_MessageComposerFormatBar_buttonIcon${this.props.icon}`;
        let shortcut;
        if (this.props.shortcut) {
            shortcut = <div className="mx_MessageComposerFormatBar_tooltipShortcut">
                { this.props.shortcut }
            </div>;
        }
        const tooltip = <div>
            <div className="mx_Tooltip_title">
                { this.props.label }
            </div>
            <div className="mx_Tooltip_sub">
                { shortcut }
            </div>
        </div>;

        return (
            <AccessibleTooltipButton
                element="button"
                type="button"
                onClick={this.props.onClick}
                title={this.props.label}
                tooltip={tooltip}
                className={className} />
        );
    }
}
