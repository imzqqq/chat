import React, { FunctionComponent, useEffect, useRef } from 'react';
import dis from '../../../dispatcher/dispatcher';
import ICanvasEffect from '../../../effects/ICanvasEffect';
import { CHAT_EFFECTS } from '../../../effects';
import UIStore, { UI_EVENTS } from "../../../stores/UIStore";

interface IProps {
    roomWidth: number;
}

const EffectsOverlay: FunctionComponent<IProps> = ({ roomWidth }) => {
    const canvasRef = useRef<HTMLCanvasElement>(null);
    const effectsRef = useRef<Map<string, ICanvasEffect>>(new Map<string, ICanvasEffect>());

    const lazyLoadEffectModule = async (name: string): Promise<ICanvasEffect> => {
        if (!name) return null;
        let effect: ICanvasEffect | null = effectsRef.current[name] || null;
        if (effect === null) {
            const options = CHAT_EFFECTS.find((e) => e.command === name)?.options;
            try {
                const { default: Effect } = await import(`../../../effects/${name}`);
                effect = new Effect(options);
                effectsRef.current[name] = effect;
            } catch (err) {
                console.warn(`Unable to load effect module at '../../../effects/${name}.`, err);
            }
        }
        return effect;
    };

    useEffect(() => {
        const resize = () => {
            if (canvasRef.current && canvasRef.current?.height !== UIStore.instance.windowHeight) {
                canvasRef.current.height = UIStore.instance.windowHeight;
            }
        };
        const onAction = (payload: { action: string }) => {
            const actionPrefix = 'effects.';
            if (payload.action.indexOf(actionPrefix) === 0) {
                const effect = payload.action.substr(actionPrefix.length);
                lazyLoadEffectModule(effect).then((module) => module?.start(canvasRef.current));
            }
        };
        const dispatcherRef = dis.register(onAction);
        const canvas = canvasRef.current;
        canvas.height = UIStore.instance.windowHeight;
        UIStore.instance.on(UI_EVENTS.Resize, resize);

        return () => {
            dis.unregister(dispatcherRef);
            UIStore.instance.off(UI_EVENTS.Resize, resize);
            // eslint-disable-next-line react-hooks/exhaustive-deps
            const currentEffects = effectsRef.current; // this is not a react node ref, warning can be safely ignored
            for (const effect in currentEffects) {
                const effectModule: ICanvasEffect = currentEffects[effect];
                if (effectModule && effectModule.isRunning) {
                    effectModule.stop();
                }
            }
        };
    }, []);

    return (
        <canvas
            ref={canvasRef}
            width={roomWidth}
            style={{
                display: 'block',
                zIndex: 999999,
                pointerEvents: 'none',
                position: 'fixed',
                top: 0,
                right: 0,
            }}
        />
    );
};

export default EffectsOverlay;
