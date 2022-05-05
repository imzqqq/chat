// For Large the image gets drawn as big as possible.
// constraint by: timeline width, manual heigh overrides, SIZE_LARGE.h
const SIZE_LARGE = { w: 800, h: 600 };

// For Normal the image gets drawn to never exceed SIZE_NORMAL.w, SIZE_NORMAL.h
// constraint by: timeline width, manual heigh overrides
const SIZE_NORMAL_LANDSCAPE = { w: 324, h: 324 }; // for w > h
const SIZE_NORMAL_PORTRAIT = { w: 324 * (9/16), h: 324 }; // for h > w
export enum ImageSize {
    Normal = "normal",
    Large = "large",
}

export function suggestedSize(size: ImageSize, portrait = false): { w: number, h: number} {
    switch (size) {
        case ImageSize.Large:
            return SIZE_LARGE;
        case ImageSize.Normal:
        default:
            return portrait ? SIZE_NORMAL_PORTRAIT : SIZE_NORMAL_LANDSCAPE;
    }
}
