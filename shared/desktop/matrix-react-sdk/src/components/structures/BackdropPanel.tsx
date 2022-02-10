import React, { CSSProperties } from "react";

interface IProps {
    backgroundImage?: string;
    blurMultiplier?: number;
}

export const BackdropPanel: React.FC<IProps> = ({ backgroundImage, blurMultiplier }) => {
    // SC: no damn avatar background in room list
    return null;

    if (!backgroundImage) return null;

    const styles: CSSProperties = {};
    if (blurMultiplier) {
        const rootStyle = getComputedStyle(document.documentElement);
        const blurValue = rootStyle.getPropertyValue('--lp-background-blur');
        const pixelsValue = blurValue.replace('px', '');
        const parsed = parseInt(pixelsValue, 10);
        if (!isNaN(parsed)) {
            styles.filter = `blur(${parsed * blurMultiplier}px)`;
        }
    }
    return <div className="mx_BackdropPanel">
        <img
            style={styles}
            className="mx_BackdropPanel--image"
            src={backgroundImage} />
    </div>;
};
export default BackdropPanel;
