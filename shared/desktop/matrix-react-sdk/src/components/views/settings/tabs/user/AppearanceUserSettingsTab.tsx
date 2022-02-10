import React from 'react';
import { _t } from "../../../../../languageHandler";
import SdkConfig from "../../../../../SdkConfig";
import { MatrixClientPeg } from '../../../../../MatrixClientPeg';
import SettingsStore from "../../../../../settings/SettingsStore";
import { enumerateThemes } from "../../../../../theme";
import ThemeWatcher from "../../../../../settings/watchers/ThemeWatcher";
import Slider from "../../../elements/Slider";
import AccessibleButton from "../../../elements/AccessibleButton";
import dis from "../../../../../dispatcher/dispatcher";
import { FontWatcher } from "../../../../../settings/watchers/FontWatcher";
import { RecheckThemePayload } from '../../../../../dispatcher/payloads/RecheckThemePayload';
import { Action } from '../../../../../dispatcher/actions';
import { IValidationResult, IFieldState } from '../../../elements/Validation';
import SettingsFlag from '../../../elements/SettingsFlag';
import Field from '../../../elements/Field';
import StyledRadioGroup from "../../../elements/StyledRadioGroup";
import { SettingLevel } from "../../../../../settings/SettingLevel";
import { Layout } from "../../../../../settings/Layout";
import { replaceableComponent } from "../../../../../utils/replaceableComponent";
import { compare } from "../../../../../utils/strings";
import LayoutSwitcher from "../../LayoutSwitcher";
import StyledRadioButton from '../../../elements/StyledRadioButton';
import { Theme } from '../../../../../settings/Theme';
import { UserNameColorMode } from '../../../../../settings/UserNameColorMode';
import { RoomListStyle } from '../../../../../settings/RoomListStyle';

interface IProps {
}

interface IThemeState {
    lightTheme: string;
    darkTheme: string;
    themeInUse: Theme;
}

export interface CustomThemeMessage {
    isError: boolean;
    text: string;
}

interface IState extends IThemeState {
    // String displaying the current selected fontSize.
    // Needs to be string for things like '17.' without
    // trailing 0s.
    fontSize: string;
    roomListStyle: RoomListStyle;
    customThemeUrl: string;
    customThemeMessage: CustomThemeMessage;
    useCustomFontSize: boolean;
    useSystemFont: boolean;
    systemFont: string;
    showAdvanced: boolean;
    showAdvancedThemeSettings: boolean;
    layout: Layout;
    userNameColorModeDM: UserNameColorMode;
    userNameColorModeGroup: UserNameColorMode;
    userNameColorModePublic: UserNameColorMode;
    // User profile data for the message preview
    userId?: string;
    displayName: string;
    avatarUrl: string;
}

@replaceableComponent("views.settings.tabs.user.AppearanceUserSettingsTab")
export default class AppearanceUserSettingsTab extends React.Component<IProps, IState> {
    private readonly MESSAGE_PREVIEW_TEXT = _t("Hey you. You're the best!");

    private themeTimer: number;
    private unmounted = false;

    constructor(props: IProps) {
        super(props);

        this.state = {
            fontSize: (SettingsStore.getValue("baseFontSize", null) + FontWatcher.SIZE_DIFF).toString(),
            lightTheme: SettingsStore.getValue("light_theme"),
            darkTheme: SettingsStore.getValue("dark_theme"),
            themeInUse: SettingsStore.getValue("theme_in_use"),
            roomListStyle: SettingsStore.getValue("roomListStyle"),
            customThemeUrl: "",
            customThemeMessage: { isError: false, text: "" },
            useCustomFontSize: SettingsStore.getValue("useCustomFontSize"),
            useSystemFont: SettingsStore.getValue("useSystemFont"),
            systemFont: SettingsStore.getValue("systemFont"),
            showAdvancedThemeSettings: false,
            showAdvanced: true,
            layout: SettingsStore.getValue("layout"),
            userNameColorModeDM: SettingsStore.getValue("userNameColorModeDM"),
            userNameColorModeGroup: SettingsStore.getValue("userNameColorModeGroup"),
            userNameColorModePublic: SettingsStore.getValue("userNameColorModePublic"),
            userId: null,
            displayName: null,
            avatarUrl: null,
        };
    }

    async componentDidMount() {
        // Fetch the current user profile for the message preview
        const client = MatrixClientPeg.get();
        const userId = client.getUserId();
        const profileInfo = await client.getProfileInfo(userId);
        if (this.unmounted) return;

        this.setState({
            userId,
            displayName: profileInfo.displayname,
            avatarUrl: profileInfo.avatar_url,
        });
    }

    componentWillUnmount() {
        this.unmounted = true;
    }

    private onLightThemeChange = (newTheme: string): void => {
        if (this.state.lightTheme === newTheme) return;

        // doing getValue in the .catch will still return the value we failed to set,
        // so remember what the value was before we tried to set it so we can revert
        const oldTheme: string = SettingsStore.getValue('light_theme');
        SettingsStore.setValue("light_theme", null, SettingLevel.DEVICE, newTheme).catch(() => {
            dis.dispatch<RecheckThemePayload>({ action: Action.RecheckTheme });
            this.setState({ lightTheme: oldTheme });
        });
        this.setState({ lightTheme: newTheme });
        // The settings watcher doesn't fire until the echo comes back from the
        // server, so to make the theme change immediately we need to manually
        // do the dispatch now
        dis.dispatch<RecheckThemePayload>({ action: Action.RecheckTheme });
    };

    private onDarkThemeChange = (newTheme: string): void => {
        if (this.state.darkTheme === newTheme) return;

        // doing getValue in the .catch will still return the value we failed to set,
        // so remember what the value was before we tried to set it so we can revert
        const oldTheme: string = SettingsStore.getValue('dark_theme');
        SettingsStore.setValue("dark_theme", null, SettingLevel.DEVICE, newTheme).catch(() => {
            dis.dispatch<RecheckThemePayload>({ action: Action.RecheckTheme });
            this.setState({ darkTheme: oldTheme });
        });
        this.setState({ darkTheme: newTheme });
        // The settings watcher doesn't fire until the echo comes back from the
        // server, so to make the theme change immediately we need to manually
        // do the dispatch now
        dis.dispatch<RecheckThemePayload>({ action: Action.RecheckTheme });
    };

    private onThemeInUseChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
        const themeInUse = e.target.value as Theme;
        this.setState({ themeInUse: themeInUse });
        SettingsStore.setValue("theme_in_use", null, SettingLevel.DEVICE, themeInUse);
        dis.dispatch<RecheckThemePayload>({ action: Action.RecheckTheme });
    };

    private onRoomListStyleChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
        const roomListStyle = e.target.value as RoomListStyle;
        this.setState({ roomListStyle: roomListStyle });
        SettingsStore.setValue("roomListStyle", null, SettingLevel.DEVICE, roomListStyle);
    };

    private onFontSizeChanged = (size: number): void => {
        this.setState({ fontSize: size.toString() });
        SettingsStore.setValue("baseFontSize", null, SettingLevel.DEVICE, size - FontWatcher.SIZE_DIFF);
    };

    private onValidateFontSize = async ({ value }: Pick<IFieldState, "value">): Promise<IValidationResult> => {
        const parsedSize = parseFloat(value);
        const min = FontWatcher.MIN_SIZE + FontWatcher.SIZE_DIFF;
        const max = FontWatcher.MAX_SIZE + FontWatcher.SIZE_DIFF;

        if (isNaN(parsedSize)) {
            return { valid: false, feedback: _t("Size must be a number") };
        }

        if (!(min <= parsedSize && parsedSize <= max)) {
            return {
                valid: false,
                feedback: _t('Custom font size can only be between %(min)s pt and %(max)s pt', { min, max }),
            };
        }

        SettingsStore.setValue(
            "baseFontSize",
            null,
            SettingLevel.DEVICE,
            parseInt(value, 10) - FontWatcher.SIZE_DIFF,
        );

        return { valid: true, feedback: _t('Use between %(min)s pt and %(max)s pt', { min, max }) };
    };

    private onAddCustomTheme = async (): Promise<void> => {
        let currentThemes: string[] = SettingsStore.getValue("custom_themes");
        if (!currentThemes) currentThemes = [];
        currentThemes = currentThemes.map(c => c); // cheap clone

        if (this.themeTimer) {
            clearTimeout(this.themeTimer);
        }

        try {
            const r = await fetch(this.state.customThemeUrl);
            // XXX: need some schema for this
            const themeInfo = await r.json();
            if (!themeInfo || typeof(themeInfo['name']) !== 'string' || typeof(themeInfo['colors']) !== 'object') {
                this.setState({ customThemeMessage: { text: _t("Invalid theme schema."), isError: true } });
                return;
            }
            currentThemes.push(themeInfo);
        } catch (e) {
            console.error(e);
            this.setState({ customThemeMessage: { text: _t("Error downloading theme information."), isError: true } });
            return; // Don't continue on error
        }

        await SettingsStore.setValue("custom_themes", null, SettingLevel.ACCOUNT, currentThemes);
        this.setState({ customThemeUrl: "", customThemeMessage: { text: _t("Theme added!"), isError: false } });

        this.themeTimer = setTimeout(() => {
            this.setState({ customThemeMessage: { text: "", isError: false } });
        }, 3000);
    };

    private onCustomThemeChange = (e: React.ChangeEvent<HTMLSelectElement | HTMLInputElement>): void => {
        this.setState({ customThemeUrl: e.target.value });
    };

    private onLayoutChanged = (layout: Layout): void => {
        this.setState({ layout: layout });
    };

    private onIRCLayoutChange = (enabled: boolean) => {
        if (enabled) {
            this.setState({ layout: Layout.IRC });
            SettingsStore.setValue("layout", null, SettingLevel.DEVICE, Layout.IRC);
        } else {
            this.setState({ layout: Layout.Group });
            SettingsStore.setValue("layout", null, SettingLevel.DEVICE, Layout.Group);
        }
    };

    private onUserNameColorModeChange = (setting: string, e: React.ChangeEvent<HTMLInputElement>): void => {
        const mode = e.target.value as UserNameColorMode;
        this.setState((prevState) => ({
            ...prevState,
            [setting]: mode,
        }));
        SettingsStore.setValue(setting, null, SettingLevel.DEVICE, mode);
    };

    private renderThemeSection() {
        const themeWatcher = new ThemeWatcher();
        const themeInUseSection = <div className="mx_SettingsTab_inlineRadioSelectors">
            <label>
                <StyledRadioButton
                    name="theme_in_use"
                    value={Theme.Light}
                    checked={this.state.themeInUse === Theme.Light}
                    onChange={this.onThemeInUseChange}
                >
                    { _t("Light") }
                </StyledRadioButton>
            </label>
            <label>
                <StyledRadioButton
                    name="theme_in_use"
                    value={Theme.Dark}
                    checked={this.state.themeInUse === Theme.Dark}
                    onChange={this.onThemeInUseChange}
                >
                    { _t("Dark") }
                </StyledRadioButton>
            </label>
            { themeWatcher.isSystemThemeSupported() ?
                <label>
                    <StyledRadioButton
                        name="theme_in_use"
                        value={Theme.System}
                        checked={this.state.themeInUse === Theme.System}
                        onChange={this.onThemeInUseChange}
                    >
                        { _t("System") }
                    </StyledRadioButton>
                </label> : null
            }
        </div>;

        let customThemeForm: JSX.Chat;
        if (SettingsStore.getValue("feature_custom_themes")) {
            let messageElement = null;
            if (this.state.customThemeMessage.text) {
                if (this.state.customThemeMessage.isError) {
                    messageElement = <div className='text-error'>{ this.state.customThemeMessage.text }</div>;
                } else {
                    messageElement = <div className='text-success'>{ this.state.customThemeMessage.text }</div>;
                }
            }
            customThemeForm = (
                <div className='mx_SettingsTab_section mx_AppearanceUserSettingsTab_addCustomThemeSection'>
                    <span className="mx_SettingsTab_subheading">{ _t("Add custom theme") }</span>
                    <form onSubmit={this.onAddCustomTheme}>
                        <Field
                            label={_t("Custom theme URL")}
                            type='text'
                            id='mx_GeneralUserSettingsTab_customThemeInput'
                            autoComplete="off"
                            onChange={this.onCustomThemeChange}
                            value={this.state.customThemeUrl}
                        />
                        <AccessibleButton
                            onClick={this.onAddCustomTheme}
                            type="submit"
                            kind="primary_sm"
                            disabled={!this.state.customThemeUrl.trim()}
                        >
                            { _t("Add theme") }
                        </AccessibleButton>
                        { messageElement }
                    </form>
                </div>
            );
        }

        const toggle = <div
            className="mx_AppearanceUserSettingsTab_AdvancedToggle"
            onClick={() => this.setState({ showAdvancedThemeSettings: !this.state.showAdvancedThemeSettings })}
        >
            { this.state.showAdvancedThemeSettings ?
                _t("Hide advanced theme settings") : _t("Show advanced theme settings") }
        </div>;

        let advanced: React.ReactNode;

        if (this.state.showAdvancedThemeSettings) {
            // XXX: replace any type here
            const themes = Object.entries<any>(enumerateThemes())
                .map(p => ({ id: p[0], name: p[1] })); // convert pairs to objects for code readability
            const builtInThemes = themes.filter(p => !p.id.startsWith("custom-"));
            const customThemes = themes.filter(p => !builtInThemes.includes(p))
                .sort((a, b) => compare(a.name, b.name));
            const orderedThemes = [...builtInThemes, ...customThemes];

            advanced = <>
                <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_themeSection">
                    <span className="mx_SettingsTab_subheading">{ _t("Light theme") }</span>
                    <div className="mx_ThemeSelectors">
                        <StyledRadioGroup
                            name="light_theme"
                            definitions={orderedThemes.map(t => ({
                                value: t.id,
                                label: t.name,
                                className: "mx_ThemeSelector_" + t.id,
                            }))}
                            onChange={this.onLightThemeChange}
                            value={this.state.lightTheme}
                            outlined
                        />
                    </div>
                </div>
                <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_themeSection">
                    <span className="mx_SettingsTab_subheading">{ _t("Dark theme") }</span>
                    <div className="mx_ThemeSelectors">
                        <StyledRadioGroup
                            name="dark_theme"
                            definitions={orderedThemes.map(t => ({
                                value: t.id,
                                label: t.name,
                                className: "mx_ThemeSelector_" + t.id,
                            }))}
                            onChange={this.onDarkThemeChange}
                            value={this.state.darkTheme}
                            outlined
                        />
                    </div>
                </div>
                { customThemeForm }
                { this.renderUserNameColorModeSection() }
            </>;
        }

        return <>
            <div className="mx_SettingsTab_heading">{ _t("Theme") }</div>
            <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_themeInUseSection">
                <span className="mx_SettingsTab_subheading">{ _t("Theme in use") }</span>
                { themeInUseSection }
            </div>
            <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_Advanced">
                { toggle }
                { advanced }
            </div>
        </>;
    }

    private renderRoomListSection() {
        const roomListStyleSection = <div className="mx_SettingsTab_multilineRadioSelectors">
            <label>
                <StyledRadioButton
                    name="room_list_style"
                    value={RoomListStyle.Compact}
                    checked={this.state.roomListStyle === RoomListStyle.Compact}
                    onChange={this.onRoomListStyleChange}
                >
                    { _t("Compact: tiny avatar together with name and preview in one line") }
                </StyledRadioButton>
            </label>
            <label>
                <StyledRadioButton
                    name="room_list_style"
                    value={RoomListStyle.Intermediate}
                    checked={this.state.roomListStyle === RoomListStyle.Intermediate}
                    onChange={this.onRoomListStyleChange}
                >
                    { _t("Intermediate: medium sized avatar with single-line preview") }
                </StyledRadioButton>
            </label>
            <label>
                <StyledRadioButton
                    name="room_list_style"
                    value={RoomListStyle.Roomy}
                    checked={this.state.roomListStyle === RoomListStyle.Roomy}
                    onChange={this.onRoomListStyleChange}
                >
                    { _t("Roomy: big avatar with two-line preview") }
                </StyledRadioButton>
            </label>
        </div>;

        return <>
            <div className="mx_SettingsTab_heading">{ _t("Room list") }</div>
            <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_fontScaling">
                <SettingsFlag
                    name="unifiedRoomList"
                    level={SettingLevel.DEVICE}
                    useCheckbox={true}
                />
            </div>
            <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_RoomListStyleSection">
                <span className="mx_SettingsTab_subheading">{ _t("Room list style") }</span>
                { roomListStyleSection }
            </div>
        </>;
    }

    private renderFontSection() {
        const brand = SdkConfig.get().brand;
        const systemFontTooltipContent = _t(
            "Set the name of a font installed on your system & %(brand)s will attempt to use it.",
            { brand },
        );

        return <>
            <div className="mx_SettingsTab_heading">{ _t("Font size and typeface") }</div>
            <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_fontScaling">
                { /* <EventTilePreview
                    className="mx_AppearanceUserSettingsTab_fontSlider_preview"
                    message={this.MESSAGE_PREVIEW_TEXT}
                    layout={this.state.layout}
                    userId={this.state.userId}
                    displayName={this.state.displayName}
                    avatarUrl={this.state.avatarUrl}
                /> */ }
                <div className="mx_AppearanceUserSettingsTab_fontSlider">
                    <div className="mx_AppearanceUserSettingsTab_fontSlider_smallText">Aa</div>
                    <Slider
                        values={[13, 14, 15, 16, 18]}
                        value={parseInt(this.state.fontSize, 10)}
                        onSelectionChange={this.onFontSizeChanged}
                        displayFunc={_ => ""}
                        disabled={this.state.useCustomFontSize}
                    />
                    <div className="mx_AppearanceUserSettingsTab_fontSlider_largeText">Aa</div>
                </div>

                <div className="mx_AppearanceUserSettingsTab_inlineCustomValues">
                    <div>
                        <SettingsFlag
                            name="useCustomFontSize"
                            level={SettingLevel.ACCOUNT}
                            onChange={(checked) => this.setState({ useCustomFontSize: checked })}
                            useCheckbox={true}
                        />
                        <Field
                            type="number"
                            label={_t("Font size")}
                            autoComplete="off"
                            placeholder={this.state.fontSize.toString()}
                            value={this.state.fontSize.toString()}
                            id="font_size_field"
                            onValidate={this.onValidateFontSize}
                            onChange={(value) => this.setState({ fontSize: value.target.value })}
                            disabled={!this.state.useCustomFontSize}
                            className="mx_SettingsTab_customFontSizeField"
                        />
                    </div>
                    <div>
                        <SettingsFlag
                            name="useSystemFont"
                            level={SettingLevel.DEVICE}
                            useCheckbox={true}
                            onChange={(checked) => this.setState({ useSystemFont: checked })}
                        />
                        <Field
                            className="mx_AppearanceUserSettingsTab_systemFont"
                            label={SettingsStore.getDisplayName("systemFont")}
                            onChange={(value) => {
                                this.setState({
                                    systemFont: value.target.value,
                                });

                                SettingsStore.setValue("systemFont", null, SettingLevel.DEVICE, value.target.value);
                            }}
                            tooltipContent={systemFontTooltipContent}
                            forceTooltipVisible={true}
                            disabled={!this.state.useSystemFont}
                            value={this.state.systemFont}
                        />
                    </div>
                </div>
            </div>
        </>;
    }

    private renderUserNameColorModeSection() {
        const makeRadio = (setting: string, mode: UserNameColorMode) => (
            console.log("asdf"+this.state[setting]),
            <StyledRadioButton
                name={setting}
                value={mode}
                checked={this.state[setting] === mode}
                onChange={(e) => this.onUserNameColorModeChange(setting, e)}
            />
        );

        const makeRow = (description: string, setting: string) => (<tr>
            <td>{ description }</td>
            <td>{ makeRadio(setting, UserNameColorMode.Uniform) }</td>
            <td>{ makeRadio(setting, UserNameColorMode.PowerLevel) }</td>
            <td>{ makeRadio(setting, UserNameColorMode.MXID) }</td>
        </tr>);

        return <>
            <div className="mx_SettingsTab_section mx_AppearanceUserSettingsTab_userNameColorModeSection">
                <table className='mx_SettingsTab_settingsTable'>
                    <thead>
                        <tr>
                            <th><span className="mx_SettingsTab_subheading">{ _t("User name color mode") }</span></th>
                            <th>{ _t("Uniform") }</th>
                            <th>{ _t("PowerLevel") }</th>
                            <th>{ _t("MXID") }</th>
                        </tr>
                    </thead>
                    <tbody>
                        { makeRow(_t("For people"), "userNameColorModeDM") }
                        { makeRow(_t("In group chats"), "userNameColorModeGroup") }
                        { makeRow(_t("In public rooms"), "userNameColorModePublic") }
                    </tbody>
                </table>
            </div>
        </>;
    }

    render() {
        const brand = SdkConfig.get().brand;

        const layoutSection = (
            <LayoutSwitcher
                userId={this.state.userId}
                displayName={this.state.displayName}
                avatarUrl={this.state.avatarUrl}
                messagePreviewText={this.MESSAGE_PREVIEW_TEXT}
                onLayoutChanged={this.onLayoutChanged}
            />
        );

        return (
            <div className="mx_SettingsTab mx_AppearanceUserSettingsTab">
                <div className="mx_SettingsTab_heading">{ _t("Customise your appearance") }</div>
                <div className="mx_SettingsTab_SubHeading">
                    { _t("Appearance Settings only affect this %(brand)s session.", { brand }) }
                </div>
                { this.renderThemeSection() }
                { this.renderRoomListSection() }
                { layoutSection }
                { this.renderFontSection() }
            </div>
        );
    }
}
