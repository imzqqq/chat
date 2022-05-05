import SettingsHandler from "./SettingsHandler";
import { MatrixClient } from "matrix-js-sdk/src/client";

// Dev note: This whole class exists in the event someone logs out and back in - we want
// to make sure the right MatrixClient is listening for changes.

/**
 * Represents the base class for settings handlers which need access to a MatrixClient.
 * This class performs no logic and should be overridden.
 */
export default abstract class MatrixClientBackedSettingsHandler extends SettingsHandler {
    private static _matrixClient: MatrixClient;
    private static instances: MatrixClientBackedSettingsHandler[] = [];

    public static set matrixClient(client: MatrixClient) {
        const oldClient = MatrixClientBackedSettingsHandler._matrixClient;
        MatrixClientBackedSettingsHandler._matrixClient = client;

        for (const instance of MatrixClientBackedSettingsHandler.instances) {
            instance.initMatrixClient(oldClient, client);
        }
    }

    protected constructor() {
        super();

        MatrixClientBackedSettingsHandler.instances.push(this);
    }

    public get client(): MatrixClient {
        return MatrixClientBackedSettingsHandler._matrixClient;
    }

    protected initMatrixClient(oldClient: MatrixClient, newClient: MatrixClient) {
        console.warn("initMatrixClient not overridden");
    }
}
