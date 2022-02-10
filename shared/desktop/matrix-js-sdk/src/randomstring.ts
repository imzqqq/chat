const LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
const UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
const DIGITS = "0123456789";

export function randomString(len: number): string {
    return randomStringFrom(len, UPPERCASE + LOWERCASE + DIGITS);
}

export function randomLowercaseString(len: number): string {
    return randomStringFrom(len, LOWERCASE);
}

export function randomUppercaseString(len: number): string {
    return randomStringFrom(len, UPPERCASE);
}

function randomStringFrom(len: number, chars: string): string {
    let ret = "";

    for (let i = 0; i < len; ++i) {
        ret += chars.charAt(Math.floor(Math.random() * chars.length));
    }

    return ret;
}
