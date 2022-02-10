export class MockBlob {
    private contents: number[] = [];

    public constructor(private parts: ArrayLike<number>[]) {
        parts.forEach(p => Array.from(p).forEach(e => this.contents.push(e)));
    }

    public get size(): number {
        return this.contents.length;
    }
}
