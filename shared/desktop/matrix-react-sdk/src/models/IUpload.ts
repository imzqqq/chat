import { IAbortablePromise } from "matrix-js-sdk/src/@types/partials";

export interface IUpload {
    fileName: string;
    roomId: string;
    total: number;
    loaded: number;
    promise: IAbortablePromise<any>;
    canceled?: boolean;
}
