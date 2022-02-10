import {Element} from "./Element.js";
import {Weechat} from "./Weechat.js";
import {Nheko} from "./Nheko.js";
import {Fractal} from "./Fractal.js";
import {Quaternion} from "./Quaternion.js";
import {Tensor} from "./Tensor.js";
import {Fluffychat} from "./Fluffychat.js";

export function createClients() {
    return [
        new Element(),
        new Weechat(),
        new Nheko(),
        new Fractal(),
        new Quaternion(),
        new Tensor(),
        new Fluffychat(),
    ];
}
