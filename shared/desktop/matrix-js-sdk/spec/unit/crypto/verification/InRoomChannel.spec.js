import { InRoomChannel } from "../../../../src/crypto/verification/request/InRoomChannel";
    "../../../../src/crypto/verification/request/ToDeviceChannel";
import { MatrixEvent } from "../../../../src/models/event";

describe("InRoomChannel tests", function() {
    const ALICE = "@alice:hs.tld";
    const BOB = "@bob:hs.tld";
    const MALORY = "@malory:hs.tld";
    const client = {
        getUserId() { return ALICE; },
    };

    it("getEventType only returns .request for a message with a msgtype", function() {
        const invalidEvent = new MatrixEvent({
            type: "m.key.verification.request",
        });
        expect(InRoomChannel.getEventType(invalidEvent)).toStrictEqual("");
        const validEvent = new MatrixEvent({
            type: "m.room.message",
            content: { msgtype: "m.key.verification.request" },
        });
        expect(InRoomChannel.getEventType(validEvent)).
            toStrictEqual("m.key.verification.request");
        const validFooEvent = new MatrixEvent({ type: "m.foo" });
        expect(InRoomChannel.getEventType(validFooEvent)).
            toStrictEqual("m.foo");
    });

    it("getEventType should return m.room.message for messages", function() {
        const messageEvent = new MatrixEvent({
            type: "m.room.message",
            content: { msgtype: "m.text" },
        });
        // XXX: The event type doesn't matter too much, just as long as it's not a verification event
        expect(InRoomChannel.getEventType(messageEvent)).
            toStrictEqual("m.room.message");
    });

    it("getEventType should return actual type for non-message events", function() {
        const event = new MatrixEvent({
            type: "m.room.member",
            content: { },
        });
        expect(InRoomChannel.getEventType(event)).
            toStrictEqual("m.room.member");
    });

    it("getOtherPartyUserId should not return anything for a request not " +
        "directed at me", function() {
        const event = new MatrixEvent({
            sender: BOB,
            type: "m.room.message",
            content: { msgtype: "m.key.verification.request", to: MALORY },
        });
        expect(InRoomChannel.getOtherPartyUserId(event, client)).toStrictEqual(undefined);
    });

    it("getOtherPartyUserId should not return anything an event that is not of a valid " +
        "request type", function() {
        // invalid because this should be a room message with msgtype
        const invalidRequest = new MatrixEvent({
            sender: BOB,
            type: "m.key.verification.request",
            content: { to: ALICE },
        });
        expect(InRoomChannel.getOtherPartyUserId(invalidRequest, client))
            .toStrictEqual(undefined);
        const startEvent = new MatrixEvent({
            sender: BOB,
            type: "m.key.verification.start",
            content: { to: ALICE },
        });
        expect(InRoomChannel.getOtherPartyUserId(startEvent, client))
            .toStrictEqual(undefined);
        const fooEvent = new MatrixEvent({
            sender: BOB,
            type: "m.foo",
            content: { to: ALICE },
        });
        expect(InRoomChannel.getOtherPartyUserId(fooEvent, client))
            .toStrictEqual(undefined);
    });
});
