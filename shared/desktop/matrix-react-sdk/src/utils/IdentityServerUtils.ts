import { SERVICE_TYPES } from 'matrix-js-sdk/src/service-types';

import SdkConfig from '../SdkConfig';
import { MatrixClientPeg } from '../MatrixClientPeg';

export function getDefaultIdentityServerUrl(): string {
    return SdkConfig.get()['validated_server_config']['isUrl'];
}

export function useDefaultIdentityServer(): void {
    const url = getDefaultIdentityServerUrl();
    // Account data change will update localstorage, client, etc through dispatcher
    MatrixClientPeg.get().setAccountData("m.identity_server", {
        base_url: url,
    });
}

export async function doesIdentityServerHaveTerms(fullUrl: string): Promise<boolean> {
    let terms;
    try {
        terms = await MatrixClientPeg.get().getTerms(SERVICE_TYPES.IS, fullUrl);
    } catch (e) {
        console.error(e);
        if (e.cors === "rejected" || e.httpStatus === 404) {
            terms = null;
        } else {
            throw e;
        }
    }

    return terms && terms["policies"] && (Object.keys(terms["policies"]).length > 0);
}

export function doesAccountDataHaveIdentityServer(): boolean {
    const event = MatrixClientPeg.get().getAccountData("m.identity_server");
    return event && event.getContent() && event.getContent()['base_url'];
}
