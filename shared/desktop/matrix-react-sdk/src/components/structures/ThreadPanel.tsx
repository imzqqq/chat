import React from 'react';
import { MatrixEvent, Room } from 'matrix-js-sdk/src';
import { Thread, ThreadEvent } from 'matrix-js-sdk/src/models/thread';

import BaseCard from "../views/right_panel/BaseCard";
import { RightPanelPhases } from "../../stores/RightPanelStorePhases";
import { replaceableComponent } from "../../utils/replaceableComponent";
import { MatrixClientPeg } from '../../MatrixClientPeg';

import ResizeNotifier from '../../utils/ResizeNotifier';
import EventTile from '../views/rooms/EventTile';

interface IProps {
    roomId: string;
    onClose: () => void;
    resizeNotifier: ResizeNotifier;
}

interface IState {
    threads?: Thread[];
}

@replaceableComponent("structures.ThreadView")
export default class ThreadPanel extends React.Component<IProps, IState> {
    private room: Room;

    constructor(props: IProps) {
        super(props);
        this.room = MatrixClientPeg.get().getRoom(this.props.roomId);
    }

    public componentDidMount(): void {
        this.room.on(ThreadEvent.Update, this.onThreadEventReceived);
        this.room.on(ThreadEvent.Ready, this.onThreadEventReceived);
    }

    public componentWillUnmount(): void {
        this.room.removeListener(ThreadEvent.Update, this.onThreadEventReceived);
        this.room.removeListener(ThreadEvent.Ready, this.onThreadEventReceived);
    }

    private onThreadEventReceived = () => this.updateThreads();

    private updateThreads = (callback?: () => void): void => {
        this.setState({
            threads: this.room.getThreads(),
        }, callback);
    };

    private renderEventTile(event: MatrixEvent): JSX.Chat {
        return <EventTile
            key={event.getId()}
            mxEvent={event}
            enableFlair={false}
            showReadReceipts={false}
            as="div"
        />;
    }

    public render(): JSX.Chat {
        return (
            <BaseCard
                className="mx_ThreadPanel"
                onClose={this.props.onClose}
                previousPhase={RightPanelPhases.RoomSummary}
            >
                {
                    this.state?.threads.map((thread: Thread) => {
                        if (thread.ready) {
                            return this.renderEventTile(thread.rootEvent);
                        }
                    })
                }
            </BaseCard>
        );
    }
}
