import { MatrixClientPeg } from "../MatrixClientPeg";
import SdkConfig from "../SdkConfig";

export function isPresenceEnabled() {
    const hsUrl = MatrixClientPeg.get().baseUrl;
    const urls = SdkConfig.get()['enable_presence_by_hs_url'];
    if (!urls) return true;
    if (urls[hsUrl] || urls[hsUrl] === undefined) return true;
    return false;
}
