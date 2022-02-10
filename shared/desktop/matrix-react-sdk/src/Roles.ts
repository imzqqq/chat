import { _t } from './languageHandler';

export function levelRoleMap(usersDefault: number) {
    return {
        undefined: _t('Default'),
        0: _t('Restricted'),
        [usersDefault]: _t('Default'),
        50: _t('Moderator'),
        100: _t('Admin'),
    };
}

export function textualPowerLevel(level: number, usersDefault: number): string {
    const LEVEL_ROLE_MAP = levelRoleMap(usersDefault);
    if (LEVEL_ROLE_MAP[level]) {
        return LEVEL_ROLE_MAP[level];
    } else {
        return _t("Custom (%(level)s)", { level });
    }
}
