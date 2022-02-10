declare module "diff-dom" {
    export interface IDiff {
        action: string;
        name: string;
        text?: string;
        route: number[];
        value: string;
        element: unknown;
        oldValue: string;
        newValue: string;
    }

    interface IOpts {
    }

    export class DiffDOM {
        public constructor(opts?: IOpts);
        public apply(tree: unknown, diffs: IDiff[]): unknown;
        public undo(tree: unknown, diffs: IDiff[]): unknown;
        public diff(a: HTMLElement | string, b: HTMLElement | string): IDiff[];
    }
}
