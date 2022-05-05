export interface IRoomEvent {
    type: string;
    sender: string;
    event_id: string; // eslint-disable-line camelcase
    room_id: string; // eslint-disable-line camelcase
    state_key?: string; // eslint-disable-line camelcase
    origin_server_ts: number; // eslint-disable-line camelcase
    content: unknown;
    unsigned: unknown;
}
