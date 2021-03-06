import React from 'react';
import { CallType, MatrixCall } from 'matrix-js-sdk/src/webrtc/call';
import classNames from 'classnames';
import { replaceableComponent } from '../utils/replaceableComponent';
import CallHandler, { CallHandlerEvent } from '../CallHandler';
import dis from '../dispatcher/dispatcher';
import { MatrixClientPeg } from '../MatrixClientPeg';
import { _t } from '../languageHandler';
import RoomAvatar from '../components/views/avatars/RoomAvatar';
import AccessibleTooltipButton from '../components/views/elements/AccessibleTooltipButton';
import AccessibleButton from '../components/views/elements/AccessibleButton';

export const getIncomingCallToastKey = (callId: string) => `call_${callId}`;

interface IProps {
    call: MatrixCall;
}

interface IState {
    silenced: boolean;
}

@replaceableComponent("views.voip.IncomingCallToast")
export default class IncomingCallToast extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);

        this.state = {
            silenced: CallHandler.sharedInstance().isCallSilenced(this.props.call.callId),
        };
    }

    public componentDidMount = (): void => {
        CallHandler.sharedInstance().addListener(CallHandlerEvent.SilencedCallsChanged, this.onSilencedCallsChanged);
    };

    public componentWillUnmount(): void {
        CallHandler.sharedInstance().removeListener(CallHandlerEvent.SilencedCallsChanged, this.onSilencedCallsChanged);
    }

    private onSilencedCallsChanged = (): void => {
        this.setState({ silenced: CallHandler.sharedInstance().isCallSilenced(this.props.call.callId) });
    };

    private onAnswerClick = (e: React.MouseEvent): void => {
        e.stopPropagation();
        dis.dispatch({
            action: 'answer',
            room_id: CallHandler.sharedInstance().roomIdForCall(this.props.call),
        });
    };

    private onRejectClick= (e: React.MouseEvent): void => {
        e.stopPropagation();
        dis.dispatch({
            action: 'reject',
            room_id: CallHandler.sharedInstance().roomIdForCall(this.props.call),
        });
    };

    private onSilenceClick = (e: React.MouseEvent): void => {
        e.stopPropagation();
        const callId = this.props.call.callId;
        this.state.silenced ?
            CallHandler.sharedInstance().unSilenceCall(callId) :
            CallHandler.sharedInstance().silenceCall(callId);
    };

    public render() {
        const call = this.props.call;
        const room = MatrixClientPeg.get().getRoom(CallHandler.sharedInstance().roomIdForCall(call));
        const isVoice = call.type === CallType.Voice;

        const contentClass = classNames("mx_IncomingCallToast_content", {
            "mx_IncomingCallToast_content_voice": isVoice,
            "mx_IncomingCallToast_content_video": !isVoice,
        });
        const silenceClass = classNames("mx_IncomingCallToast_iconButton", {
            "mx_IncomingCallToast_unSilence": this.state.silenced,
            "mx_IncomingCallToast_silence": !this.state.silenced,
        });

        return <React.Fragment>
            <RoomAvatar
                room={room}
                height={32}
                width={32}
            />
            <div className={contentClass}>
                <span className="mx_CallEvent_caller">
                    { room ? room.name : _t("Unknown caller") }
                </span>
                <div className="mx_CallEvent_type">
                    <div className="mx_CallEvent_type_icon" />
                    { isVoice ? _t("Voice call") : _t("Video call") }
                </div>
                <div className="mx_IncomingCallToast_buttons">
                    <AccessibleButton
                        className="mx_IncomingCallToast_button mx_IncomingCallToast_button_decline"
                        onClick={this.onRejectClick}
                        kind="danger"
                    >
                        <span> { _t("Decline") } </span>
                    </AccessibleButton>
                    <AccessibleButton
                        className="mx_IncomingCallToast_button mx_IncomingCallToast_button_accept"
                        onClick={this.onAnswerClick}
                        kind="primary"
                    >
                        <span> { _t("Accept") } </span>
                    </AccessibleButton>
                </div>
            </div>
            <AccessibleTooltipButton
                className={silenceClass}
                onClick={this.onSilenceClick}
                title={this.state.silenced ? _t("Sound on") : _t("Silence call")}
            />
        </React.Fragment>;
    }
}
