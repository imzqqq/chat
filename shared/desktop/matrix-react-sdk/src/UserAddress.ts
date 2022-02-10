const emailRegex = /^\S+@\S+\.\S+$/;
const mxUserIdRegex = /^@\S+:\S+$/;
const mxRoomIdRegex = /^!\S+:\S+$/;

export enum AddressType {
    Email = "email",
    MatrixUserId = "mx-user-id",
    MatrixRoomId = "mx-room-id",
}

export const addressTypes = [AddressType.Email, AddressType.MatrixRoomId, AddressType.MatrixUserId];

// PropType definition for an object describing
// an address that can be invited to a room (which
// could be a third party identifier or a matrix ID)
// along with some additional information about the
// address / target.
export interface IUserAddress {
    addressType: AddressType;
    address: string;
    displayName?: string;
    avatarMxc?: string;
    // true if the address is known to be a valid address (eg. is a real
    // user we've seen) or false otherwise (eg. is just an address the
    // user has entered)
    isKnown?: boolean;
}

export function getAddressType(inputText: string): AddressType | null {
    if (emailRegex.test(inputText)) {
        return AddressType.Email;
    } else if (mxUserIdRegex.test(inputText)) {
        return AddressType.MatrixUserId;
    } else if (mxRoomIdRegex.test(inputText)) {
        return AddressType.MatrixRoomId;
    } else {
        return null;
    }
}
