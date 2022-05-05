import { MatrixEvent } from "matrix-js-sdk/src";
import { MediaEventHelper } from "../../../utils/MediaEventHelper";
import React from "react";
import { RovingAccessibleTooltipButton } from "../../../accessibility/RovingTabIndex";
import Spinner from "../elements/Spinner";
import classNames from "classnames";
import { _t } from "../../../languageHandler";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import { FileDownloader } from "../../../utils/FileDownloader";

interface IProps {
    mxEvent: MatrixEvent;

    // XXX: It can take a cycle or two for the MessageActionBar to have all the props/setup
    // required to get us a MediaEventHelper, so we use a getter function instead to prod for
    // one.
    mediaEventHelperGet: () => MediaEventHelper;
}

interface IState {
    loading: boolean;
    blob?: Blob;
}

@replaceableComponent("views.messages.DownloadActionButton")
export default class DownloadActionButton extends React.PureComponent<IProps, IState> {
    private downloader = new FileDownloader();

    public constructor(props: IProps) {
        super(props);

        this.state = {
            loading: false,
        };
    }

    private onDownloadClick = async () => {
        if (this.state.loading) return;

        this.setState({ loading: true });

        if (this.state.blob) {
            // Cheat and trigger a download, again.
            return this.doDownload();
        }

        const blob = await this.props.mediaEventHelperGet().sourceBlob.value;
        this.setState({ blob });
        await this.doDownload();
    };

    private async doDownload() {
        await this.downloader.download({
            blob: this.state.blob,
            name: this.props.mediaEventHelperGet().fileName,
        });
        this.setState({ loading: false });
    }

    public render() {
        let spinner: JSX.Chat;
        if (this.state.loading) {
            spinner = <Spinner w={18} h={18} />;
        }

        const classes = classNames({
            'mx_MessageActionBar_maskButton': true,
            'mx_MessageActionBar_downloadButton': true,
            'mx_MessageActionBar_downloadSpinnerButton': !!spinner,
        });

        return <RovingAccessibleTooltipButton
            className={classes}
            title={spinner ? _t("Decrypting") : _t("Download")}
            onClick={this.onDownloadClick}
            disabled={!!spinner}
        >
            { spinner }
        </RovingAccessibleTooltipButton>;
    }
}
