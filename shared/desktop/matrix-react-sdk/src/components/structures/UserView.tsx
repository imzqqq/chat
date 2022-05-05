import React from "react";
import { MatrixClientPeg } from "../../MatrixClientPeg";
import Modal from '../../Modal';
import { _t } from '../../languageHandler';
import HomePage from "./HomePage";
import { replaceableComponent } from "../../utils/replaceableComponent";
import { MatrixEvent } from "matrix-js-sdk/src/models/event";
import { RoomMember } from "matrix-js-sdk/src/models/room-member";
import ErrorDialog from "../views/dialogs/ErrorDialog";
import MainSplit from "./MainSplit";
import RightPanel from "./RightPanel";
import Spinner from "../views/elements/Spinner";
import ResizeNotifier from "../../utils/ResizeNotifier";

interface IProps {
    userId?: string;
    resizeNotifier: ResizeNotifier;
}

interface IState {
    loading: boolean;
    member?: RoomMember;
}

@replaceableComponent("structures.UserView")
export default class UserView extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            loading: true,
        };
    }

    public componentDidMount(): void {
        if (this.props.userId) {
            this.loadProfileInfo();
        }
    }

    public componentDidUpdate(prevProps: IProps): void {
        // XXX: We shouldn't need to null check the userId here, but we declare
        // it as optional and MatrixChat sometimes fires in a way which results
        // in an NPE when we try to update the profile info.
        if (prevProps.userId !== this.props.userId && this.props.userId) {
            this.loadProfileInfo();
        }
    }

    private async loadProfileInfo(): Promise<void> {
        const cli = MatrixClientPeg.get();
        this.setState({ loading: true });
        let profileInfo;
        try {
            profileInfo = await cli.getProfileInfo(this.props.userId);
        } catch (err) {
            Modal.createTrackedDialog(_t('Could not load user profile'), '', ErrorDialog, {
                title: _t('Could not load user profile'),
                description: ((err && err.message) ? err.message : _t("Operation failed")),
            });
            this.setState({ loading: false });
            return;
        }
        const fakeEvent = new MatrixEvent({ type: "m.room.member", content: profileInfo });
        const member = new RoomMember(null, this.props.userId);
        member.setMembershipEvent(fakeEvent);
        this.setState({ member, loading: false });
    }

    public render(): JSX.Chat {
        if (this.state.loading) {
            return <Spinner />;
        } else if (this.state.member?.user) {
            const panel = <RightPanel user={this.state.member.user} resizeNotifier={this.props.resizeNotifier} />;
            return (<MainSplit panel={panel} resizeNotifier={this.props.resizeNotifier}>
                <HomePage />
            </MainSplit>);
        } else {
            return (<div />);
        }
    }
}
