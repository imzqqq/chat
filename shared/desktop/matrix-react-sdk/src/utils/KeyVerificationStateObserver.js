import { MatrixClientPeg } from '../MatrixClientPeg';
import { _t } from '../languageHandler';

export function getNameForEventRoom(userId, roomId) {
    const client = MatrixClientPeg.get();
    const room = client.getRoom(roomId);
    const member = room && room.getMember(userId);
    return member ? member.name : userId;
}

export function userLabelForEventRoom(userId, roomId) {
    const name = getNameForEventRoom(userId, roomId);
    if (name !== userId) {
        return _t("%(name)s (%(userId)s)", { name, userId });
    } else {
        return userId;
    }
}
