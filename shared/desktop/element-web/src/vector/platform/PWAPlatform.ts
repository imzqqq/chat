import WebPlatform from "./WebPlatform";

export default class PWAPlatform extends WebPlatform {
    setNotificationCount(count: number) {
        if (!navigator.setAppBadge) return super.setNotificationCount(count);
        if (this.notificationCount === count) return;
        this.notificationCount = count;

        navigator.setAppBadge(count).catch(e => {
            console.error("Failed to update PWA app badge", e);
        });
    }
}
