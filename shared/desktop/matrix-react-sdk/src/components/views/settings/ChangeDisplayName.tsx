import React from 'react';
import { MatrixClientPeg } from '../../../MatrixClientPeg';
import { _t } from '../../../languageHandler';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import EditableTextContainer from "../elements/EditableTextContainer";

@replaceableComponent("views.settings.ChangeDisplayName")
export default class ChangeDisplayName extends React.Component {
    private getDisplayName = async (): Promise<string> => {
        const cli = MatrixClientPeg.get();
        try {
            const res = await cli.getProfileInfo(cli.getUserId());
            return res.displayname;
        } catch (e) {
            throw new Error("Failed to fetch display name");
        }
    };

    private changeDisplayName = (newDisplayname: string): Promise<{}> => {
        const cli = MatrixClientPeg.get();
        return cli.setDisplayName(newDisplayname).catch(function() {
            throw new Error("Failed to set display name");
        });
    };

    public render(): JSX.Chat {
        return (
            <EditableTextContainer
                getInitialValue={this.getDisplayName}
                placeholder={_t("No display name")}
                blurToSubmit={true}
                onSubmit={this.changeDisplayName} />
        );
    }
}
