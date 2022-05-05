import { _t, _td } from "../languageHandler";
import { ConfettiOptions } from "./confetti";
import { Effect } from "./effect";
import { FireworksOptions } from "./fireworks";
import { SnowfallOptions } from "./snowfall";
import { SpaceInvadersOptions } from "./spaceinvaders";

/**
 * This configuration defines room effects that can be triggered by custom message types and emojis
 */
export const CHAT_EFFECTS: Array<Effect<{ [key: string]: any }>> = [
    {
        emojis: ['🎊', '🎉'],
        msgType: 'nic.custom.confetti',
        command: 'confetti',
        description: () => _td("Sends the given message with confetti"),
        fallbackMessage: () => _t("sends confetti") + " 🎉",
        options: {
            maxCount: 150,
            speed: 3,
            frameInterval: 15,
            alpha: 1.0,
            gradient: false,
        },
    } as Effect<ConfettiOptions>,
    {
        emojis: ['🎆'],
        msgType: 'nic.custom.fireworks',
        command: 'fireworks',
        description: () => _td("Sends the given message with fireworks"),
        fallbackMessage: () => _t("sends fireworks") + " 🎆",
        options: {
            maxCount: 500,
            gravity: 0.05,
        },
    } as Effect<FireworksOptions>,
    {
        emojis: ['❄', '🌨'],
        msgType: 'io.element.effect.snowfall',
        command: 'snowfall',
        description: () => _td("Sends the given message with snowfall"),
        fallbackMessage: () => _t("sends snowfall") + " ❄",
        options: {
            maxCount: 200,
            gravity: 0.05,
            maxDrift: 5,
        },
    } as Effect<SnowfallOptions>,
    {
        emojis: ["👾", "🌌"],
        msgType: "io.element.effects.space_invaders",
        command: "spaceinvaders",
        description: () => _td("Sends the given message with a space themed effect"),
        fallbackMessage: () => _t("sends space invaders") + " 👾",
        options: {
            maxCount: 50,
            gravity: 0.01,
        },
    } as Effect<SpaceInvadersOptions>,
];

