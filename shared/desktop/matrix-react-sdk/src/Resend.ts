import { MatrixEvent, EventStatus } from 'matrix-js-sdk/src/models/event';
import { Room } from 'matrix-js-sdk/src/models/room';

import { MatrixClientPeg } from './MatrixClientPeg';
import dis from './dispatcher/dispatcher';

export default class Resend {
    static resendUnsentEvents(room: Room): Promise<void[]> {
        return Promise.all(room.getPendingEvents().filter(function(ev: MatrixEvent) {
            return ev.status === EventStatus.NOT_SENT;
        }).map(function(event: MatrixEvent) {
            return Resend.resend(event);
        }));
    }

    static cancelUnsentEvents(room: Room): void {
        room.getPendingEvents().filter(function(ev: MatrixEvent) {
            return ev.status === EventStatus.NOT_SENT;
        }).forEach(function(event: MatrixEvent) {
            Resend.removeFromQueue(event);
        });
    }

    static resend(event: MatrixEvent): Promise<void> {
        const room = MatrixClientPeg.get().getRoom(event.getRoomId());
        return MatrixClientPeg.get().resendEvent(event, room).then(function(res) {
            dis.dispatch({
                action: 'message_sent',
                event: event,
            });
        }, function(err: Error) {
            // XXX: temporary logging to try to diagnose
            // https://github.com/vector-im/element-web/issues/3148
            console.log('Resend got send failure: ' + err.name + '(' + err + ')');
        });
    }

    static removeFromQueue(event: MatrixEvent): void {
        MatrixClientPeg.get().cancelPendingEvent(event);
    }
}
