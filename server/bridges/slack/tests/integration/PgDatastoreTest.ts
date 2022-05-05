/*
Copyright 2019 The Matrix.org Foundation C.I.C.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import { Logging } from "matrix-appservice-bridge";
import pgInit from "pg-promise";
import { PgDatastore } from "../../src/datastore/postgres/PgDatastore";
import { expect } from "chai";
import { doDatastoreTests } from "./SharedDatastoreTests";
const log = Logging.get("PgDatastoreTest");

const enableTest = process.env.SLACKBRIDGE_TEST_ENABLEPG === "yes";
const describeFnl = enableTest ? describe : describe.skip;

const DATABASE = process.env.SLACKBRIDGE_TEST_PGDB || "slackintegtest";
const POSTGRES_URL = process.env.SLACKBRIDGE_TEST_PGURL || `postgresql://postgres:pass@localhost`;
const POSTGRES_URL_DB = `${POSTGRES_URL}/${DATABASE}`;

const pgp = pgInit();
describeFnl("PgDatastore", () => {
    let superDb: pgInit.IDatabase<any>;
    let ds: PgDatastore;
    before(async () => {
        Logging.configure({console: "info"});
        superDb = pgp(POSTGRES_URL + "/postgres");
        ds = new PgDatastore(POSTGRES_URL_DB);
        try {
            await superDb.none(`DROP DATABASE ${DATABASE}`);
        } catch (ex) {
            log.warn("Failed to drop database");
        }
        try {
            await superDb.none(`CREATE DATABASE ${DATABASE}`);
        } catch (ex) {
            log.warn("Failed to create database");
            throw ex;
        }
    });

    it("should be able to exec the current schema set successfully", async () => {
        await ds.ensureSchema();
        const { version } = (await ds.postgresDb.one(`SELECT version FROM schema`));
        expect(version).to.equal(PgDatastore.LATEST_SCHEMA);
    });

    doDatastoreTests(() => ds, async () => {
        await ds.postgresDb.none("DELETE FROM rooms");
    });

    describe("reactions", () => {
        afterEach(async () => {
            await ds.postgresDb.none("DELETE FROM reactions");
        });

        it("should insert and retrieve a reaction by its Matrix identifiers", async () => {
            const entry = {
                roomId: "!foo:bar",
                eventId: "$foo:bar",
                slackChannelId: "F00",
                slackMessageTs: "BAR",
                slackUserId: "U010AAR88B1",
                reaction: "hugging_face",
            };
            await ds.upsertReaction(entry);
            const reaction = await ds.getReactionByMatrixId(entry.roomId, entry.eventId);
            expect(reaction).to.deep.equal(entry);
        });
        it("should insert and retrieve a reaction by its Slack identifiers", async () => {
            const entry = {
                roomId: "!foo:bar",
                eventId: "$foo:bar",
                slackChannelId: "F00",
                slackMessageTs: "BAR",
                slackUserId: "U010AAR88B1",
                reaction: "hugging_face",
            };
            await ds.upsertReaction(entry);
            const reaction = await ds.getReactionBySlackId(entry.slackChannelId, entry.slackMessageTs, entry.slackUserId, entry.reaction);
            expect(reaction).to.deep.equal(entry);
        });
        it("should insert and delete a reaction by its Matrix identifiers", async () => {
            const entry = {
                roomId: "!foo:bar",
                eventId: "$foo:bar",
                slackChannelId: "F00",
                slackMessageTs: "BAR",
                slackUserId: "U010AAR88B1",
                reaction: "hugging_face",
            };
            await ds.upsertReaction(entry);
            await ds.deleteReactionByMatrixId(entry.roomId, entry.eventId);
            const reaction = await ds.getReactionByMatrixId(entry.roomId, entry.eventId);
            expect(reaction).to.be.null;
        });
        it("should insert and delete a reaction by its Slack identifiers", async () => {
            const entry = {
                roomId: "!foo:bar",
                eventId: "$foo:bar",
                slackChannelId: "F00",
                slackMessageTs: "BAR",
                slackUserId: "U010AAR88B1",
                reaction: "hugging_face",
            };
            await ds.upsertReaction(entry);
            await ds.deleteReactionBySlackId(entry.slackChannelId, entry.slackMessageTs, entry.slackUserId, entry.reaction);
            const reaction = await ds.getReactionBySlackId(entry.slackChannelId, entry.slackMessageTs, entry.slackUserId, entry.reaction);
            expect(reaction).to.be.null;
        });
        it("should not throw when an reaction is upserted twice", async () => {
            const entry = {
                roomId: "!foo:bar",
                eventId: "$foo:bar",
                slackChannelId: "F00",
                slackMessageTs: "BAR",
                slackUserId: "U010AAR88B1",
                reaction: "hugging_face",
            };
            await ds.upsertReaction(entry);
            await ds.upsertReaction(entry);
        });
    });

    after(async () => {
        Logging.configure({console: "off"});
    });
});
