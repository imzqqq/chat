import { encode } from "blurhash";

const ctx: Worker = self as any;

interface IBlurhashWorkerRequest {
    seq: number;
    imageData: ImageData;
}

ctx.addEventListener("message", (event: MessageEvent<IBlurhashWorkerRequest>): void => {
    const { seq, imageData } = event.data;
    const blurhash = encode(
        imageData.data,
        imageData.width,
        imageData.height,
        // use 4 components on the longer dimension, if square then both
        imageData.width >= imageData.height ? 4 : 3,
        imageData.height >= imageData.width ? 4 : 3,
    );

    ctx.postMessage({ seq, blurhash });
});
