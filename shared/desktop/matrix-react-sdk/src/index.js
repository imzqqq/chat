import Skinner from './Skinner';

export function loadSkin(skinObject) {
    Skinner.load(skinObject);
}

export function resetSkin() {
    Skinner.reset();
}

export function getComponent(componentName) {
    return Skinner.getComponent(componentName);
}

// Import the js-sdk so the proper `request` object can be set. This does some
// magic with the browser injection to make all subsequent imports work fine.
import "matrix-js-sdk";
