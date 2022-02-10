import { IdServerUnbindResult } from "./partials";

// Types relating to Chat server Admin APIs

/* eslint-disable camelcase */
export interface ISynapseAdminWhoisResponse {
    user_id: string;
    devices: {
        [deviceId: string]: {
            sessions: {
                connections: {
                    ip: string;
                    last_seen: number; // millis since epoch
                    user_agent: string;
                }[];
            }[];
        };
    };
}

export interface ISynapseAdminDeactivateResponse {
    id_server_unbind_result: IdServerUnbindResult;
}
/* eslint-enable camelcase */
