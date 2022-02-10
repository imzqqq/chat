import React from 'react';
import QuestionDialog from './QuestionDialog';
import { _t } from '../../../languageHandler';
import SdkConfig from '../../../SdkConfig';

export default (props) => {
    const brand = SdkConfig.get().brand;
    const description =
        _t(
            "%(brand)s now uses 3-5x less memory, by only loading information " +
            "about other users when needed. Please wait whilst we resynchronise " +
            "with the server!",
            { brand },
        );

    return (<QuestionDialog
        hasCancelButton={false}
        title={_t("Updating %(brand)s", { brand })}
        description={<div>{ description }</div>}
        button={_t("OK")}
        onFinished={props.onFinished}
    />);
};
