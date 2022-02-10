import React from 'react';
import ReactDOM from 'react-dom';
import ReactTestUtils from 'react-dom/test-utils';
import sdk from '../../../skinned-sdk';
import SdkConfig from '../../../../src/SdkConfig';
import { mkServerConfig } from "../../../test-utils";

const Registration = sdk.getComponent(
    'structures.auth.Registration',
);

describe('Registration', function() {
    let parentDiv;

    beforeEach(function() {
        parentDiv = document.createElement('div');
        document.body.appendChild(parentDiv);
    });

    afterEach(function() {
        ReactDOM.unmountComponentAtNode(parentDiv);
        parentDiv.remove();
    });

    function render() {
        return ReactDOM.render(<Registration
            serverConfig={mkServerConfig("https://matrix.org", "https://vector.im")}
            makeRegistrationUrl={() => {}}
            onLoggedIn={() => {}}
            onLoginClick={() => {}}
            onServerConfigChange={() => {}}
        />, parentDiv);
    }

    it('should show server picker', function() {
        const root = render();
        const selector = ReactTestUtils.findRenderedDOMComponentWithClass(root, "mx_ServerPicker");
        expect(selector).toBeTruthy();
    });

    it('should show form when custom URLs disabled', function() {
        jest.spyOn(SdkConfig, "get").mockReturnValue({
            disable_custom_urls: true,
        });

        const root = render();

        // Set non-empty flows & matrixClient to get past the loading spinner
        root.setState({
            flows: [{
                stages: [],
            }],
            matrixClient: {},
            busy: false,
        });

        const form = ReactTestUtils.findRenderedComponentWithType(
            root,
            sdk.getComponent('auth.RegistrationForm'),
        );
        expect(form).toBeTruthy();
    });

    it("should show SSO options if those are available", () => {
        jest.spyOn(SdkConfig, "get").mockReturnValue({
            disable_custom_urls: true,
        });

        const root = render();

        // Set non-empty flows & matrixClient to get past the loading spinner
        root.setState({
            flows: [{
                stages: [],
            }],
            ssoFlow: {
                type: "m.login.sso",
            },
            matrixClient: {},
            busy: false,
        });

        const ssoButton = ReactTestUtils.findRenderedDOMComponentWithClass(root, "mx_SSOButton");
        expect(ssoButton).toBeTruthy();
    });
});
