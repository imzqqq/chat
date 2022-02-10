import { randomString } from '../randomstring';

const DEFAULT_ITERATIONS = 500000;

const DEFAULT_BITSIZE = 256;

/* eslint-disable camelcase */
interface IAuthData {
    private_key_salt?: string;
    private_key_iterations?: number;
    private_key_bits?: number;
}
/* eslint-enable camelcase */

interface IKey {
    key: Uint8Array;
    salt: string;
    iterations: number;
}

export async function keyFromAuthData(authData: IAuthData, password: string): Promise<Uint8Array> {
    if (!global.Olm) {
        throw new Error("Olm is not available");
    }

    if (!authData.private_key_salt || !authData.private_key_iterations) {
        throw new Error(
            "Salt and/or iterations not found: " +
            "this backup cannot be restored with a passphrase",
        );
    }

    return await deriveKey(
        password, authData.private_key_salt,
        authData.private_key_iterations,
        authData.private_key_bits || DEFAULT_BITSIZE,
    );
}

export async function keyFromPassphrase(password: string): Promise<IKey> {
    if (!global.Olm) {
        throw new Error("Olm is not available");
    }

    const salt = randomString(32);

    const key = await deriveKey(password, salt, DEFAULT_ITERATIONS, DEFAULT_BITSIZE);

    return { key, salt, iterations: DEFAULT_ITERATIONS };
}

export async function deriveKey(
    password: string,
    salt: string,
    iterations: number,
    numBits = DEFAULT_BITSIZE,
): Promise<Uint8Array> {
    const subtleCrypto = global.crypto.subtle;
    const TextEncoder = global.TextEncoder;
    if (!subtleCrypto || !TextEncoder) {
        // TODO: Implement this for node
        throw new Error("Password-based backup is not avaiable on this platform");
    }

    const key = await subtleCrypto.importKey(
        'raw',
        new TextEncoder().encode(password),
        { name: 'PBKDF2' },
        false,
        ['deriveBits'],
    );

    const keybits = await subtleCrypto.deriveBits(
        {
            name: 'PBKDF2',
            salt: new TextEncoder().encode(salt),
            iterations: iterations,
            hash: 'SHA-512',
        },
        key,
        numBits,
    );

    return new Uint8Array(keybits);
}
