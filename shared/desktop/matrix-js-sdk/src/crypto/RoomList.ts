/**
 * @module crypto/RoomList
 *
 * Manages the list of encrypted rooms
 */

import { CryptoStore } from './store/base';
import { IndexedDBCryptoStore } from './store/indexeddb-crypto-store';

/* eslint-disable camelcase */
export interface IRoomEncryption {
    algorithm: string;
    rotation_period_ms?: number;
    rotation_period_msgs?: number;
}
/* eslint-enable camelcase */

/**
 * @alias module:crypto/RoomList
 */
export class RoomList {
    // Object of roomId -> room e2e info object (body of the m.room.encryption event)
    private roomEncryption: Record<string, IRoomEncryption> = {};

    constructor(private readonly cryptoStore: CryptoStore) {}

    public async init(): Promise<void> {
        await this.cryptoStore.doTxn(
            'readwrite', [IndexedDBCryptoStore.STORE_ROOMS], (txn) => {
                this.cryptoStore.getEndToEndRooms(txn, (result) => {
                    this.roomEncryption = result;
                });
            },
        );
    }

    public getRoomEncryption(roomId: string): IRoomEncryption {
        return this.roomEncryption[roomId] || null;
    }

    public isRoomEncrypted(roomId: string): boolean {
        return Boolean(this.getRoomEncryption(roomId));
    }

    public async setRoomEncryption(roomId: string, roomInfo: IRoomEncryption): Promise<void> {
        // important that this happens before calling into the store
        // as it prevents the Crypto::setRoomEncryption from calling
        // this twice for consecutive m.room.encryption events
        this.roomEncryption[roomId] = roomInfo;
        await this.cryptoStore.doTxn(
            'readwrite', [IndexedDBCryptoStore.STORE_ROOMS], (txn) => {
                this.cryptoStore.storeEndToEndRoom(roomId, roomInfo, txn);
            },
        );
    }
}
