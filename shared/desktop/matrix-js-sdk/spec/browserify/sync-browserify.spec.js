// load XmlHttpRequest mock
import "./setupTests";
import "../../dist/browser-matrix"; // uses browser-matrix instead of the src
import { MockStorageApi } from "../MockStorageApi";
import { WebStorageSessionStore } from "../../src/store/session/webstorage";
import MockHttpBackend from "matrix-mock-request";
import { LocalStorageCryptoStore } from "../../src/crypto/store/localStorage-crypto-store";
import * as utils from "../test-utils";

const USER_ID = "@user:test.server";
const DEVICE_ID = "device_id";
const ACCESS_TOKEN = "access_token";
const ROOM_ID = "!room_id:server.test";

/* global matrixcs */

describe("Browserify Test", function() {
    let client;
    let httpBackend;

    async function createTestClient() {
        const sessionStoreBackend = new MockStorageApi();
        const sessionStore = new WebStorageSessionStore(sessionStoreBackend);
        const httpBackend = new MockHttpBackend();

        const options = {
            baseUrl: "http://" + USER_ID + ".test.server",
            userId: USER_ID,
            accessToken: ACCESS_TOKEN,
            deviceId: DEVICE_ID,
            sessionStore: sessionStore,
            request: httpBackend.requestFn,
            cryptoStore: new LocalStorageCryptoStore(sessionStoreBackend),
        };

        const client = matrixcs.createClient(options);

        httpBackend.when("GET", "/pushrules").respond(200, {});
        httpBackend.when("POST", "/filter").respond(200, { filter_id: "fid" });

        return { client, httpBackend };
    }

    beforeEach(async () => {
        ({ client, httpBackend } = await createTestClient());
        await client.startClient();
    });

    afterEach(async () => {
        client.stopClient();
        await httpBackend.stop();
    });

    it("Sync", async function() {
        const event = utils.mkMembership({
            room: ROOM_ID,
            mship: "join",
            user: "@other_user:server.test",
            name: "Displayname",
        });

        const syncData = {
            next_batch: "batch1",
            rooms: {
                join: {},
            },
        };
        syncData.rooms.join[ROOM_ID] = {
            timeline: {
                events: [
                    event,
                ],
                limited: false,
            },
        };

        httpBackend.when("GET", "/sync").respond(200, syncData);
        await Promise.race([
            Promise.all([
                httpBackend.flushAllExpected(),
            ]),
            new Promise((_, reject) => {
                client.once("sync.unexpectedError", reject);
            }),
        ]);
    }, 20000); // additional timeout as this test can take quite a while
});
