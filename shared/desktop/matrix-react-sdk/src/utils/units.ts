/* Simple utils for formatting style values
 */

// converts a pixel value to rem.
export function toRem(pixelValue: number): string {
    return pixelValue / 10 + "rem";
}

export function toPx(pixelValue: number): string {
    return pixelValue + "px";
}
