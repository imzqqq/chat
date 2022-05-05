import React, { ErrorInfo } from 'react';

import { _t } from '../../../languageHandler';
import { MatrixClientPeg } from '../../../MatrixClientPeg';
import PlatformPeg from '../../../PlatformPeg';
import Modal from '../../../Modal';
import SdkConfig from "../../../SdkConfig";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import BugReportDialog from '../dialogs/BugReportDialog';
import AccessibleButton from './AccessibleButton';

interface IState {
    error: Error;
}

/**
 * This error boundary component can be used to wrap large content areas and
 * catch exceptions during rendering in the component tree below them.
 */
@replaceableComponent("views.elements.ErrorBoundary")
export default class ErrorBoundary extends React.PureComponent<{}, IState> {
    constructor(props) {
        super(props);

        this.state = {
            error: null,
        };
    }

    static getDerivedStateFromError(error: Error): Partial<IState> {
        // Side effects are not permitted here, so we only update the state so
        // that the next render shows an error message.
        return { error };
    }

    componentDidCatch(error: Error, { componentStack }: ErrorInfo): void {
        // Browser consoles are better at formatting output when native errors are passed
        // in their own `console.error` invocation.
        console.error(error);
        console.error(
            "The above error occured while React was rendering the following components:",
            componentStack,
        );
    }

    private onClearCacheAndReload = (): void => {
        if (!PlatformPeg.get()) return;

        MatrixClientPeg.get().stopClient();
        MatrixClientPeg.get().store.deleteAllData().then(() => {
            PlatformPeg.get().reload();
        });
    };

    private onBugReport = (): void => {
        Modal.createTrackedDialog('Bug Report Dialog', '', BugReportDialog, {
            label: 'react-soft-crash',
            error: this.state.error,
        });
    };

    render() {
        if (this.state.error) {
            const newIssueUrl = "https://github.com/imzqqq/issues/new/choose";

            let bugReportSection;
            if (SdkConfig.get().bug_report_endpoint_url) {
                bugReportSection = <React.Fragment>
                    <p>{ _t(
                        "Please <newIssueLink>create a new issue</newIssueLink> " +
                        "on GitHub so that we can investigate this bug.", {}, {
                            newIssueLink: (sub) => {
                                return <a target="_blank" rel="noreferrer noopener" href={newIssueUrl}>{ sub }</a>;
                            },
                        },
                    ) }</p>
                    <p>{ _t(
                        "If you've submitted a bug via GitHub, debug logs can help " +
                        "us track down the problem. Debug logs contain application " +
                        "usage data including your username, the IDs or aliases of " +
                        "the rooms or groups you have visited, which UI elements you " +
                        "last interacted with, and the usernames of other users. " +
                        "They do not contain messages.",
                    ) }</p>
                    <AccessibleButton onClick={this.onBugReport} kind='primary'>
                        { _t("Submit debug logs") }
                    </AccessibleButton>
                </React.Fragment>;
            }

            return <div className="mx_ErrorBoundary">
                <div className="mx_ErrorBoundary_body">
                    <h1>{ _t("Something went wrong!") }</h1>
                    { bugReportSection }
                    <AccessibleButton onClick={this.onClearCacheAndReload} kind='danger'>
                        { _t("Clear cache and reload") }
                    </AccessibleButton>
                </div>
            </div>;
        }

        return this.props.children;
    }
}
