import React from 'react';
import RoomViewStore from '../../../stores/RoomViewStore';
import ActiveWidgetStore from '../../../stores/ActiveWidgetStore';
import WidgetUtils from '../../../utils/WidgetUtils';
import * as sdk from '../../../index';
import { MatrixClientPeg } from '../../../MatrixClientPeg';
import { replaceableComponent } from "../../../utils/replaceableComponent";

@replaceableComponent("views.elements.PersistentApp")
export default class PersistentApp extends React.Component {
    state = {
        roomId: RoomViewStore.getRoomId(),
        persistentWidgetId: ActiveWidgetStore.getPersistentWidgetId(),
    };

    componentDidMount() {
        this._roomStoreToken = RoomViewStore.addListener(this._onRoomViewStoreUpdate);
        ActiveWidgetStore.on('update', this._onActiveWidgetStoreUpdate);
        MatrixClientPeg.get().on("Room.myMembership", this._onMyMembership);
    }

    componentWillUnmount() {
        if (this._roomStoreToken) {
            this._roomStoreToken.remove();
        }
        ActiveWidgetStore.removeListener('update', this._onActiveWidgetStoreUpdate);
        if (MatrixClientPeg.get()) {
            MatrixClientPeg.get().removeListener("Room.myMembership", this._onMyMembership);
        }
    }

    _onRoomViewStoreUpdate = payload => {
        if (RoomViewStore.getRoomId() === this.state.roomId) return;
        this.setState({
            roomId: RoomViewStore.getRoomId(),
        });
    };

    _onActiveWidgetStoreUpdate = () => {
        this.setState({
            persistentWidgetId: ActiveWidgetStore.getPersistentWidgetId(),
        });
    };

    _onMyMembership = async (room, membership) => {
        const persistentWidgetInRoomId = ActiveWidgetStore.getRoomId(this.state.persistentWidgetId);
        if (membership !== "join") {
            // we're not in the room anymore - delete
            if (room.roomId === persistentWidgetInRoomId) {
                ActiveWidgetStore.destroyPersistentWidget(this.state.persistentWidgetId);
            }
        }
    };

    render() {
        if (this.state.persistentWidgetId) {
            const persistentWidgetInRoomId = ActiveWidgetStore.getRoomId(this.state.persistentWidgetId);

            const persistentWidgetInRoom = MatrixClientPeg.get().getRoom(persistentWidgetInRoomId);

            // Sanity check the room - the widget may have been destroyed between render cycles, and
            // thus no room is associated anymore.
            if (!persistentWidgetInRoom) return null;

            const myMembership = persistentWidgetInRoom.getMyMembership();
            if (this.state.roomId !== persistentWidgetInRoomId && myMembership === "join") {
                // get the widget data
                const appEvent = WidgetUtils.getRoomWidgets(persistentWidgetInRoom).find((ev) => {
                    return ev.getStateKey() === ActiveWidgetStore.getPersistentWidgetId();
                });
                const app = WidgetUtils.makeAppConfig(
                    appEvent.getStateKey(), appEvent.getContent(), appEvent.getSender(),
                    persistentWidgetInRoomId, appEvent.getId(),
                );
                const AppTile = sdk.getComponent('elements.AppTile');
                return <AppTile
                    key={app.id}
                    app={app}
                    fullWidth={true}
                    room={persistentWidgetInRoom}
                    userId={MatrixClientPeg.get().credentials.userId}
                    creatorUserId={app.creatorUserId}
                    widgetPageTitle={WidgetUtils.getWidgetDataTitle(app)}
                    waitForIframeLoad={app.waitForIframeLoad}
                    miniMode={true}
                    showMenubar={false}
                />;
            }
        }
        return null;
    }
}

