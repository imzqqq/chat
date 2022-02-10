import React from 'react';
import PropTypes from 'prop-types';

import * as sdk from '../../../index';
import * as languageHandler from '../../../languageHandler';
import SettingsStore from "../../../settings/SettingsStore";
import { _t } from "../../../languageHandler";
import { replaceableComponent } from "../../../utils/replaceableComponent";

function languageMatchesSearchQuery(query, language) {
    if (language.label.toUpperCase().includes(query.toUpperCase())) return true;
    if (language.value.toUpperCase() === query.toUpperCase()) return true;
    return false;
}

@replaceableComponent("views.elements.LanguageDropdown")
export default class LanguageDropdown extends React.Component {
    constructor(props) {
        super(props);
        this._onSearchChange = this._onSearchChange.bind(this);

        this.state = {
            searchQuery: '',
            langs: null,
        };
    }

    componentDidMount() {
        languageHandler.getAllLanguagesFromJson().then((langs) => {
            langs.sort(function(a, b) {
                if (a.label < b.label) return -1;
                if (a.label > b.label) return 1;
                return 0;
            });
            this.setState({ langs });
        }).catch(() => {
            this.setState({ langs: ['en'] });
        });

        if (!this.props.value) {
            // If no value is given, we start with the first
            // country selected, but our parent component
            // doesn't know this, therefore we do this.
            const language = languageHandler.getUserLanguage();
            this.props.onOptionChange(language);
        }
    }

    _onSearchChange(search) {
        this.setState({
            searchQuery: search,
        });
    }

    render() {
        if (this.state.langs === null) {
            const Spinner = sdk.getComponent('elements.Spinner');
            return <Spinner />;
        }

        const Dropdown = sdk.getComponent('elements.Dropdown');

        let displayedLanguages;
        if (this.state.searchQuery) {
            displayedLanguages = this.state.langs.filter((lang) => {
                return languageMatchesSearchQuery(this.state.searchQuery, lang);
            });
        } else {
            displayedLanguages = this.state.langs;
        }

        const options = displayedLanguages.map((language) => {
            return <div key={language.value}>
                { language.label }
            </div>;
        });

        // default value here too, otherwise we need to handle null / undefined
        // values between mounting and the initial value propgating
        let language = SettingsStore.getValue("language", null, /*excludeDefault:*/true);
        let value = null;
        if (language) {
            value = this.props.value || language;
        } else {
            language = navigator.language || navigator.userLanguage;
            value = this.props.value || language;
        }

        return <Dropdown
            id="mx_LanguageDropdown"
            className={this.props.className}
            onOptionChange={this.props.onOptionChange}
            onSearchChange={this._onSearchChange}
            searchEnabled={true}
            value={value}
            label={_t("Language Dropdown")}
            disabled={this.props.disabled}
        >
            { options }
        </Dropdown>;
    }
}

LanguageDropdown.propTypes = {
    className: PropTypes.string,
    onOptionChange: PropTypes.func.isRequired,
    value: PropTypes.string,
};
