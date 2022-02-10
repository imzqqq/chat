import React from 'react';
import { IMyDevice } from 'matrix-js-sdk/src/client';

import { _t } from '../../../languageHandler';
import { MatrixClientPeg } from '../../../MatrixClientPeg';
import { formatDate } from '../../../DateUtils';
import StyledCheckbox from '../elements/StyledCheckbox';
import { replaceableComponent } from "../../../utils/replaceableComponent";
import EditableTextContainer from "../elements/EditableTextContainer";

interface IProps {
    device?: IMyDevice;
    onDeviceToggled?: (device: IMyDevice) => void;
    selected?: boolean;
}

@replaceableComponent("views.settings.DevicesPanelEntry")
export default class DevicesPanelEntry extends React.Component<IProps> {
    public static defaultProps = {
        onDeviceToggled: () => {},
    };

    private onDisplayNameChanged = (value: string): Promise<{}> => {
        const device = this.props.device;
        return MatrixClientPeg.get().setDeviceDetails(device.device_id, {
            display_name: value,
        }).catch((e) => {
            console.error("Error setting session display name", e);
            throw new Error(_t("Failed to set display name"));
        });
    };

    private onDeviceToggled = (): void => {
        this.props.onDeviceToggled(this.props.device);
    };

    public render(): JSX.Chat {
        const device = this.props.device;

        let lastSeen = "";
        if (device.last_seen_ts) {
            const lastSeenDate = formatDate(new Date(device.last_seen_ts));
            lastSeen = device.last_seen_ip + " @ " +
                lastSeenDate.toLocaleString();
        }

        let myDeviceClass = '';
        if (device.device_id === MatrixClientPeg.get().getDeviceId()) {
            myDeviceClass = " mx_DevicesPanel_myDevice";
        }

        return (
            <div className={"mx_DevicesPanel_device" + myDeviceClass}>
                <div className="mx_DevicesPanel_deviceId">
                    { device.device_id }
                </div>
                <div className="mx_DevicesPanel_deviceName">
                    <EditableTextContainer initialValue={device.display_name}
                        onSubmit={this.onDisplayNameChanged}
                        placeholder={device.device_id}
                    />
                </div>
                <div className="mx_DevicesPanel_lastSeen">
                    { lastSeen }
                </div>
                <div className="mx_DevicesPanel_deviceButtons">
                    <StyledCheckbox onChange={this.onDeviceToggled} checked={this.props.selected} />
                </div>
            </div>
        );
    }
}
