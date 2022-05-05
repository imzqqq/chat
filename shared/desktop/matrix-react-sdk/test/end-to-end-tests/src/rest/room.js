const uuidv4 = require('uuid/v4');

/* no pun intented */
module.exports = class RestRoom {
    constructor(session, roomId, log) {
        this.session = session;
        this._roomId = roomId;
        this.log = log;
    }

    async talk(message) {
        this.log.step(`says "${message}" in ${this._roomId}`);
        const txId = uuidv4();
        await this.session._put(`/rooms/${this._roomId}/send/m.room.message/${txId}`, {
            "msgtype": "m.text",
            "body": message,
        });
        this.log.done();
        return txId;
    }

    async leave() {
        this.log.step(`leaves ${this._roomId}`);
        await this.session._post(`/rooms/${this._roomId}/leave`);
        this.log.done();
    }

    roomId() {
        return this._roomId;
    }
};
