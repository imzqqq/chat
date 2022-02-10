import ScalarAuthClient from '../src/ScalarAuthClient';
import { MatrixClientPeg } from '../src/MatrixClientPeg';
import { stubClient } from './test-utils';

describe('ScalarAuthClient', function() {
    beforeEach(function() {
        window.localStorage.getItem = jest.fn((arg) => {
            if (arg === "mx_scalar_token") return "brokentoken";
        });
        stubClient();
    });

    it('should request a new token if the old one fails', async function() {
        const sac = new ScalarAuthClient();

        sac.getAccountName = jest.fn((arg) => {
            switch (arg) {
                case "brokentoken":
                    return Promise.reject({
                        message: "Invalid token",
                    });
                case "wokentoken":
                    return Promise.resolve(MatrixClientPeg.get().getUserId());
            }
        });

        MatrixClientPeg.get().getOpenIdToken = jest.fn().mockResolvedValue('this is your openid token');

        sac.exchangeForScalarToken = jest.fn((arg) => {
            if (arg === "this is your openid token") return Promise.resolve("wokentoken");
        });

        await sac.connect();

        expect(sac.exchangeForScalarToken).toBeCalledWith('this is your openid token');
        expect(sac.scalarToken).toEqual('wokentoken');
    });
});
