import { Whenable } from "../../utils/Whenable";

export type RunFn = () => Promise<void>;

export enum TransactionStatus {
    Pending,
    Success,
    Error,
}

export class EchoTransaction extends Whenable<TransactionStatus> {
    private _status = TransactionStatus.Pending;
    private didFail = false;

    public readonly startTime = new Date();

    public constructor(
        public readonly auditName,
        public runFn: RunFn,
    ) {
        super();
    }

    public get didPreviouslyFail(): boolean {
        return this.didFail;
    }

    public get status(): TransactionStatus {
        return this._status;
    }

    public run() {
        if (this.status === TransactionStatus.Success) {
            throw new Error("Cannot re-run a successful echo transaction");
        }
        this.setStatus(TransactionStatus.Pending);
        this.runFn()
            .then(() => this.setStatus(TransactionStatus.Success))
            .catch(() => this.setStatus(TransactionStatus.Error));
    }

    public cancel() {
        // Success basically means "done"
        this.setStatus(TransactionStatus.Success);
    }

    private setStatus(status: TransactionStatus) {
        this._status = status;
        if (status === TransactionStatus.Error) {
            this.didFail = true;
        } else if (status === TransactionStatus.Success) {
            this.didFail = false;
        }
        this.notifyCondition(status);
    }
}
