export enum ThreepidMedium {
    Email = "email",
    Phone = "msisdn",
}

// TODO: Are these types universal, or specific to just /account/3pid?
export interface IThreepid {
    medium: ThreepidMedium;
    address: string;
    validated_at: number; // eslint-disable-line camelcase
    added_at: number; // eslint-disable-line camelcase
}
