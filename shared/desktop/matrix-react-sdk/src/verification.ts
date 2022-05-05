import { User } from "matrix-js-sdk/src/models/user";

import { MatrixClientPeg } from './MatrixClientPeg';
import dis from "./dispatcher/dispatcher";
import Modal from './Modal';
import { RightPanelPhases } from "./stores/RightPanelStorePhases";
import { findDMForUser } from './createRoom';
import { accessSecretStorage } from './SecurityManager';
import { verificationMethods as VerificationMethods } from 'matrix-js-sdk/src/crypto';
import { Action } from './dispatcher/actions';
import UntrustedDeviceDialog from "./components/views/dialogs/UntrustedDeviceDialog";
import { IDevice } from "./components/views/right_panel/UserInfo";
import ManualDeviceKeyVerificationDialog from "./components/views/dialogs/ManualDeviceKeyVerificationDialog";

async function enable4SIfNeeded() {
    const cli = MatrixClientPeg.get();
    if (!cli.isCryptoEnabled()) {
        return false;
    }
    const usk = cli.getCrossSigningId("user_signing");
    if (!usk) {
        await accessSecretStorage();
        return false;
    }

    return true;
}

export async function verifyDevice(user: User, device: IDevice) {
    const cli = MatrixClientPeg.get();
    if (cli.isGuest()) {
        dis.dispatch({ action: 'require_registration' });
        return;
    }
    // if cross-signing is not explicitly disabled, check if it should be enabled first.
    if (cli.getCryptoTrustCrossSignedDevices()) {
        if (!await enable4SIfNeeded()) {
            return;
        }
    }

    Modal.createTrackedDialog("Verification warning", "unverified session", UntrustedDeviceDialog, {
        user,
        device,
        onFinished: async (action) => {
            if (action === "sas") {
                const verificationRequestPromise = cli.legacyDeviceVerification(
                    user.userId,
                    device.deviceId,
                    VerificationMethods.SAS,
                );
                dis.dispatch({
                    action: Action.SetRightPanelPhase,
                    phase: RightPanelPhases.EncryptionPanel,
                    refireParams: { member: user, verificationRequestPromise },
                });
            } else if (action === "legacy") {
                Modal.createTrackedDialog("Legacy verify session", "legacy verify session",
                    ManualDeviceKeyVerificationDialog,
                    {
                        userId: user.userId,
                        device,
                    },
                );
            }
        },
    });
}

export async function legacyVerifyUser(user: User) {
    const cli = MatrixClientPeg.get();
    if (cli.isGuest()) {
        dis.dispatch({ action: 'require_registration' });
        return;
    }
    // if cross-signing is not explicitly disabled, check if it should be enabled first.
    if (cli.getCryptoTrustCrossSignedDevices()) {
        if (!await enable4SIfNeeded()) {
            return;
        }
    }
    const verificationRequestPromise = cli.requestVerification(user.userId);
    dis.dispatch({
        action: Action.SetRightPanelPhase,
        phase: RightPanelPhases.EncryptionPanel,
        refireParams: { member: user, verificationRequestPromise },
    });
}

export async function verifyUser(user: User) {
    const cli = MatrixClientPeg.get();
    if (cli.isGuest()) {
        dis.dispatch({ action: 'require_registration' });
        return;
    }
    if (!await enable4SIfNeeded()) {
        return;
    }
    const existingRequest = pendingVerificationRequestForUser(user);
    dis.dispatch({
        action: Action.SetRightPanelPhase,
        phase: RightPanelPhases.EncryptionPanel,
        refireParams: {
            member: user,
            verificationRequest: existingRequest,
        },
    });
}

export function pendingVerificationRequestForUser(user: User) {
    const cli = MatrixClientPeg.get();
    const dmRoom = findDMForUser(cli, user.userId);
    if (dmRoom) {
        return cli.findVerificationRequestDMInProgress(dmRoom.roomId);
    }
}
