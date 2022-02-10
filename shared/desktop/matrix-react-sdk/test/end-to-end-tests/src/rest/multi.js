const Logger = require('../logger');

module.exports = class RestMultiSession {
    constructor(sessions, groupName) {
        this.log = new Logger(groupName);
        this.sessions = sessions;
    }

    slice(groupName, start, end) {
        return new RestMultiSession(this.sessions.slice(start, end), groupName);
    }

    pop(userName) {
        const idx = this.sessions.findIndex((s) => s.userName() === userName);
        if (idx === -1) {
            throw new Error(`user ${userName} not found`);
        }
        const session = this.sessions.splice(idx, 1)[0];
        return session;
    }

    async setDisplayName(fn) {
        this.log.step("set their display name");
        await Promise.all(this.sessions.map(async (s) => {
            s.log.mute();
            await s.setDisplayName(fn(s));
            s.log.unmute();
        }));
        this.log.done();
    }

    async join(roomIdOrAlias) {
        this.log.step(`join ${roomIdOrAlias}`);
        const rooms = await Promise.all(this.sessions.map(async (s) => {
            s.log.mute();
            const room = await s.join(roomIdOrAlias);
            s.log.unmute();
            return room;
        }));
        this.log.done();
        return new RestMultiRoom(rooms, roomIdOrAlias, this.log);
    }

    room(roomIdOrAlias) {
        const rooms = this.sessions.map(s => s.room(roomIdOrAlias));
        return new RestMultiRoom(rooms, roomIdOrAlias, this.log);
    }
};

class RestMultiRoom {
    constructor(rooms, roomIdOrAlias, log) {
        this.rooms = rooms;
        this.roomIdOrAlias = roomIdOrAlias;
        this.log = log;
    }

    async talk(message) {
        this.log.step(`say "${message}" in ${this.roomIdOrAlias}`);
        await Promise.all(this.rooms.map(async (r) => {
            r.log.mute();
            await r.talk(message);
            r.log.unmute();
        }));
        this.log.done();
    }

    async leave() {
        this.log.step(`leave ${this.roomIdOrAlias}`);
        await Promise.all(this.rooms.map(async (r) => {
            r.log.mute();
            await r.leave();
            r.log.unmute();
        }));
        this.log.done();
    }
}
