import React from "react";
import {
    IconizedContextMenuOption,
    IconizedContextMenuOptionList,
} from "../views/context_menus/IconizedContextMenu";
import { _t } from "../../languageHandler";
import { HostSignupStore } from "../../stores/HostSignupStore";
import SdkConfig from "../../SdkConfig";
import { replaceableComponent } from "../../utils/replaceableComponent";

interface IProps {
    onClick?(): void;
}

interface IState {}

@replaceableComponent("structures.HostSignupAction")
export default class HostSignupAction extends React.PureComponent<IProps, IState> {
    private openDialog = async () => {
        this.props.onClick?.();
        await HostSignupStore.instance.setHostSignupActive(true);
    };

    public render(): React.ReactNode {
        const hostSignupConfig = SdkConfig.get().hostSignup;
        if (!hostSignupConfig?.brand) {
            return null;
        }

        return (
            <IconizedContextMenuOptionList>
                <IconizedContextMenuOption
                    iconClassName="mx_UserMenu_iconHosting"
                    label={_t(
                        "Upgrade to %(hostSignupBrand)s",
                        {
                            hostSignupBrand: hostSignupConfig.brand,
                        },
                    )}
                    onClick={this.openDialog}
                />
            </IconizedContextMenuOptionList>
        );
    }
}
