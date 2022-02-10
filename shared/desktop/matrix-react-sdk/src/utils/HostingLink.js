import SdkConfig from '../SdkConfig';
import { MatrixClientPeg } from '../MatrixClientPeg';

export function getHostingLink(campaign) {
    const hostingLink = SdkConfig.get().hosting_signup_link;
    if (!hostingLink) return null;
    if (!campaign) return hostingLink;

    if (MatrixClientPeg.get().getDomain() !== 'matrix.org') return null;

    try {
        const hostingUrl = new URL(hostingLink);
        hostingUrl.searchParams.set("utm_campaign", campaign);
        return hostingUrl.format();
    } catch (e) {
        return hostingLink;
    }
}
