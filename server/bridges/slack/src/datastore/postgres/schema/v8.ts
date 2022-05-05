import { IDatabase } from "pg-promise";

export const runSchema = async(db: IDatabase<unknown>): Promise<void> => {
    await db.none(`
        CREATE INDEX events_matrix_idx ON events (roomid, eventid);
        CREATE INDEX events_slack_idx ON events (slackchannel, slackts);`);
};
