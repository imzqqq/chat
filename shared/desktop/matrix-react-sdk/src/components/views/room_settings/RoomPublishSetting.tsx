import React from "react";
import { Visibility } from "matrix-js-sdk/src/@types/partials";

import LabelledToggleSwitch from "../elements/LabelledToggleSwitch";
import { _t } from "../../../languageHandler";
import { MatrixClientPeg } from "../../../MatrixClientPeg";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import DirectoryCustomisations from '../../../customisations/Directory';

interface IProps {
    roomId: string;
    label?: string;
    canSetCanonicalAlias?: boolean;
}

interface IState {
    isRoomPublished: boolean;
}

@replaceableComponent("views.room_settings.RoomPublishSetting")
export default class RoomPublishSetting extends React.PureComponent<IProps, IState> {
    constructor(props, context) {
        super(props, context);

        this.state = {
            isRoomPublished: false,
        };
    }

    private onRoomPublishChange = (e) => {
        const valueBefore = this.state.isRoomPublished;
        const newValue = !valueBefore;
        this.setState({ isRoomPublished: newValue });
        const client = MatrixClientPeg.get();

        client.setRoomDirectoryVisibility(
            this.props.roomId,
            newValue ? Visibility.Public : Visibility.Private,
        ).catch(() => {
            // Roll back the local echo on the change
            this.setState({ isRoomPublished: valueBefore });
        });
    };

    componentDidMount() {
        const client = MatrixClientPeg.get();
        client.getRoomDirectoryVisibility(this.props.roomId).then((result => {
            this.setState({ isRoomPublished: result.visibility === 'public' });
        }));
    }

    render() {
        const client = MatrixClientPeg.get();

        const enabled = (
            DirectoryCustomisations.requireCanonicalAliasAccessToPublish?.() === false ||
            this.props.canSetCanonicalAlias
        );

        return (
            <LabelledToggleSwitch value={this.state.isRoomPublished}
                onChange={this.onRoomPublishChange}
                // disabled={!enabled}
                label={_t("Publish this room to the public in %(domain)s's room directory?", {
                    domain: client.getDomain(),
                })}
            />
        );
    }
}
