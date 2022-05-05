export enum MatrixApiVersion {
    Prerelease1 = "0.0.1",
    Prerelease2 = "0.0.2",
    //V010 = "0.1.0", // first release
}

export enum UnstableApiVersion {
    MSC2762 = "org.matrix.msc2762",
    MSC2871 = "org.matrix.msc2871",
    MSC2931 = "org.matrix.msc2931",
    MSC2974 = "org.matrix.msc2974",
    MSC2876 = "org.matrix.msc2876",
}

export type ApiVersion = MatrixApiVersion | UnstableApiVersion | string;

export const CurrentApiVersions: ApiVersion[] = [
    MatrixApiVersion.Prerelease1,
    MatrixApiVersion.Prerelease2,
    //MatrixApiVersion.V010,
    UnstableApiVersion.MSC2762,
    UnstableApiVersion.MSC2871,
    UnstableApiVersion.MSC2931,
    UnstableApiVersion.MSC2974,
    UnstableApiVersion.MSC2876,
];
