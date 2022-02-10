import { MatrixClientPeg } from '../MatrixClientPeg';

const CALL_BEHAVIOUR_WK_KEY = "io.element.call_behaviour";
const E2EE_WK_KEY = "io.element.e2ee";
const E2EE_WK_KEY_DEPRECATED = "im.vector.riot.e2ee";

/* eslint-disable camelcase */
export interface ICallBehaviourWellKnown {
    widget_build_url?: string;
}

export interface IE2EEWellKnown {
    default?: boolean;
    secure_backup_required?: boolean;
    secure_backup_setup_methods?: SecureBackupSetupMethod[];
}
/* eslint-enable camelcase */

export function getCallBehaviourWellKnown(): ICallBehaviourWellKnown {
    const clientWellKnown = MatrixClientPeg.get().getClientWellKnown();
    return clientWellKnown?.[CALL_BEHAVIOUR_WK_KEY];
}

export function getE2EEWellKnown(): IE2EEWellKnown {
    const clientWellKnown = MatrixClientPeg.get().getClientWellKnown();
    if (clientWellKnown && clientWellKnown[E2EE_WK_KEY]) {
        return clientWellKnown[E2EE_WK_KEY];
    }
    if (clientWellKnown && clientWellKnown[E2EE_WK_KEY_DEPRECATED]) {
        return clientWellKnown[E2EE_WK_KEY_DEPRECATED];
    }
    return null;
}

export function isSecureBackupRequired(): boolean {
    const wellKnown = getE2EEWellKnown();
    return wellKnown && wellKnown["secure_backup_required"] === true;
}

export enum SecureBackupSetupMethod {
    Key = "key",
    Passphrase = "passphrase",
}

export function getSecureBackupSetupMethods(): SecureBackupSetupMethod[] {
    const wellKnown = getE2EEWellKnown();
    if (
        !wellKnown ||
        !wellKnown["secure_backup_setup_methods"] ||
        !wellKnown["secure_backup_setup_methods"].length ||
        !(
            wellKnown["secure_backup_setup_methods"].includes(SecureBackupSetupMethod.Key) ||
            wellKnown["secure_backup_setup_methods"].includes(SecureBackupSetupMethod.Passphrase)
        )
    ) {
        return [
            SecureBackupSetupMethod.Key,
            SecureBackupSetupMethod.Passphrase,
        ];
    }
    return wellKnown["secure_backup_setup_methods"];
}
