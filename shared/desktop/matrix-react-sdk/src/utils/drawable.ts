/**
 * Fetch an image using the best available method based on browser compatibility
 * @param url the URL of the image to fetch
 * @returns a canvas drawable object
 */
export async function getDrawable(url: string): Promise<CanvasImageSource> {
    if ('createImageBitmap' in window) {
        const response = await fetch(url);
        const blob = await response.blob();
        return await createImageBitmap(blob);
    } else {
        return new Promise<HTMLImageElement>((resolve, reject) => {
            const img = document.createElement("img");
            img.crossOrigin = "anonymous";
            img.onload = () => resolve(img);
            img.onerror = (e) => reject(e);
            img.src = url;
        });
    }
}
