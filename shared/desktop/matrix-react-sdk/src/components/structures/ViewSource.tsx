import React from "react";
import SyntaxHighlight from "../views/elements/SyntaxHighlight";
import { _t } from "../../languageHandler";
import MatrixClientContext from "../../contexts/MatrixClientContext";
import { SendCustomEvent } from "../views/dialogs/DevtoolsDialog";
import { canEditContent } from "../../utils/EventUtils";
import { MatrixClientPeg } from '../../MatrixClientPeg';
import { replaceableComponent } from "../../utils/replaceableComponent";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { IDialogProps } from "../views/dialogs/IDialogProps";
import BaseDialog from "../views/dialogs/BaseDialog";

interface IProps extends IDialogProps {
    mxEvent: MatrixEvent; // the MatrixEvent associated with the context menu
}

interface IState {
    isEditing: boolean;
}

@replaceableComponent("structures.ViewSource")
export default class ViewSource extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            isEditing: false,
        };
    }

    private onBack(): void {
        // TODO: refresh the "Event ID:" modal header
        this.setState({ isEditing: false });
    }

    private onEdit(): void {
        this.setState({ isEditing: true });
    }

    // returns the dialog body for viewing the event source
    private viewSourceContent(): JSX.Element {
        const mxEvent = this.props.mxEvent.replacingEvent() || this.props.mxEvent; // show the replacing event, not the original, if it is an edit
        const isEncrypted = mxEvent.isEncrypted();
        // @ts-ignore
        const decryptedEventSource = mxEvent.clearEvent; // FIXME: clearEvent is private
        const originalEventSource = mxEvent.event;

        if (isEncrypted) {
            return (
                <>
                    <details open className="mx_ViewSource_details">
                        <summary>
                            <span className="mx_ViewSource_heading">{ _t("Decrypted event source") }</span>
                        </summary>
                        <SyntaxHighlight className="json">{ JSON.stringify(decryptedEventSource, null, 2) }</SyntaxHighlight>
                    </details>
                    <details className="mx_ViewSource_details">
                        <summary>
                            <span className="mx_ViewSource_heading">{ _t("Original event source") }</span>
                        </summary>
                        <SyntaxHighlight className="json">{ JSON.stringify(originalEventSource, null, 2) }</SyntaxHighlight>
                    </details>
                </>
            );
        } else {
            return (
                <>
                    <div className="mx_ViewSource_heading">{ _t("Original event source") }</div>
                    <SyntaxHighlight className="json">{ JSON.stringify(originalEventSource, null, 2) }</SyntaxHighlight>
                </>
            );
        }
    }

    // returns the id of the initial message, not the id of the previous edit
    private getBaseEventId(): string {
        const mxEvent = this.props.mxEvent.replacingEvent() || this.props.mxEvent; // show the replacing event, not the original, if it is an edit
        const isEncrypted = mxEvent.isEncrypted();
        const baseMxEvent = this.props.mxEvent;

        if (isEncrypted) {
            // `relates_to` field is inside the encrypted event
            return mxEvent.event.content["m.relates_to"]?.event_id ?? baseMxEvent.getId();
        } else {
            return mxEvent.getContent()["m.relates_to"]?.event_id ?? baseMxEvent.getId();
        }
    }

    // returns the SendCustomEvent component prefilled with the correct details
    private editSourceContent(): JSX.Element {
        const mxEvent = this.props.mxEvent.replacingEvent() || this.props.mxEvent; // show the replacing event, not the original, if it is an edit

        const isStateEvent = mxEvent.isState();
        const roomId = mxEvent.getRoomId();
        const originalContent = mxEvent.getContent();

        if (isStateEvent) {
            return (
                <MatrixClientContext.Consumer>
                    { (cli) => (
                        <SendCustomEvent
                            room={cli.getRoom(roomId)}
                            forceStateEvent={true}
                            onBack={() => this.onBack()}
                            inputs={{
                                eventType: mxEvent.getType(),
                                evContent: JSON.stringify(originalContent, null, "\t"),
                                stateKey: mxEvent.getStateKey(),
                            }}
                        />
                    ) }
                </MatrixClientContext.Consumer>
            );
        } else {
            // prefill an edit-message event
            // keep only the `body` and `msgtype` fields of originalContent
            const bodyToStartFrom = originalContent["m.new_content"]?.body ?? originalContent.body; // prefill the last edit body, to start editing from there
            const newContent = {
                "body": ` * ${bodyToStartFrom}`,
                "msgtype": originalContent.msgtype,
                "m.new_content": {
                    body: bodyToStartFrom,
                    msgtype: originalContent.msgtype,
                },
                "m.relates_to": {
                    rel_type: "m.replace",
                    event_id: this.getBaseEventId(),
                },
            };
            return (
                <MatrixClientContext.Consumer>
                    { (cli) => (
                        <SendCustomEvent
                            room={cli.getRoom(roomId)}
                            forceStateEvent={false}
                            forceGeneralEvent={true}
                            onBack={() => this.onBack()}
                            inputs={{
                                eventType: mxEvent.getType(),
                                evContent: JSON.stringify(newContent, null, "\t"),
                            }}
                        />
                    ) }
                </MatrixClientContext.Consumer>
            );
        }
    }

    private canSendStateEvent(mxEvent: MatrixEvent): boolean {
        const cli = MatrixClientPeg.get();
        const room = cli.getRoom(mxEvent.getRoomId());
        return room.currentState.mayClientSendStateEvent(mxEvent.getType(), cli);
    }

    public render(): JSX.Element {
        const mxEvent = this.props.mxEvent.replacingEvent() || this.props.mxEvent; // show the replacing event, not the original, if it is an edit

        const isEditing = this.state.isEditing;
        const roomId = mxEvent.getRoomId();
        const eventId = mxEvent.getId();
        const canEdit = mxEvent.isState() ? this.canSendStateEvent(mxEvent) : canEditContent(this.props.mxEvent);
        return (
            <BaseDialog className="mx_ViewSource" onFinished={this.props.onFinished} title={_t("View Source")}>
                <div>
                    <div>Room ID: { roomId }</div>
                    <div>Event ID: { eventId }</div>
                    <div className="mx_ViewSource_separator" />
                    { isEditing ? this.editSourceContent() : this.viewSourceContent() }
                </div>
                { !isEditing && canEdit && (
                    <div className="mx_Dialog_buttons">
                        <button onClick={() => this.onEdit()}>{ _t("Edit") }</button>
                    </div>
                ) }
            </BaseDialog>
        );
    }
}
