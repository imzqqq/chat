// Regexp based on Simpler Version from https://gist.github.com/gregseth/5582254 - matches RFC2822
const EMAIL_ADDRESS_REGEX = new RegExp(
    "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*" + // localpart
    "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", "i");

export function looksValid(email: string): boolean {
    return EMAIL_ADDRESS_REGEX.test(email);
}
