import React from 'react';
import SpellCheckLanguagesDropdown from "../../../components/views/elements/SpellCheckLanguagesDropdown";
import AccessibleButton from "../../../components/views/elements/AccessibleButton";
import { _t } from "../../../languageHandler";
import { replaceableComponent } from "../../../utils/replaceableComponent";

interface ExistingSpellCheckLanguageIProps {
    language: string;
    onRemoved(language: string);
}

interface SpellCheckLanguagesIProps {
    languages: Array<string>;
    onLanguagesChange(languages: Array<string>);
}

interface SpellCheckLanguagesIState {
    newLanguage: string;
}

export class ExistingSpellCheckLanguage extends React.Component<ExistingSpellCheckLanguageIProps> {
    private onRemove = (e) => {
        e.stopPropagation();
        e.preventDefault();

        return this.props.onRemoved(this.props.language);
    };

    render() {
        return (
            <div className="mx_ExistingSpellCheckLanguage">
                <span className="mx_ExistingSpellCheckLanguage_language">{ this.props.language }</span>
                <AccessibleButton onClick={this.onRemove} kind="danger_sm">
                    { _t("Remove") }
                </AccessibleButton>
            </div>
        );
    }
}

@replaceableComponent("views.settings.SpellCheckLanguages")
export default class SpellCheckLanguages extends React.Component<SpellCheckLanguagesIProps, SpellCheckLanguagesIState> {
    constructor(props) {
        super(props);
        this.state = {
            newLanguage: "",
        };
    }

    private onRemoved = (language: string) => {
        const languages = this.props.languages.filter((e) => e !== language);
        this.props.onLanguagesChange(languages);
    };

    private onAddClick = (e) => {
        e.stopPropagation();
        e.preventDefault();

        const language = this.state.newLanguage;

        if (!language) return;
        if (this.props.languages.includes(language)) return;

        this.props.languages.push(language);
        this.props.onLanguagesChange(this.props.languages);
    };

    private onNewLanguageChange = (language: string) => {
        if (this.state.newLanguage === language) return;
        this.setState({ newLanguage: language });
    };

    render() {
        const existingSpellCheckLanguages = this.props.languages.map((e) => {
            return <ExistingSpellCheckLanguage language={e} onRemoved={this.onRemoved} key={e} />;
        });

        const addButton = (
            <AccessibleButton onClick={this.onAddClick} kind="primary">
                { _t("Add") }
            </AccessibleButton>
        );

        return (
            <div className="mx_SpellCheckLanguages">
                { existingSpellCheckLanguages }
                <form onSubmit={this.onAddClick} noValidate={true}>
                    <SpellCheckLanguagesDropdown
                        className="mx_GeneralUserSettingsTab_spellCheckLanguageInput"
                        value={this.state.newLanguage}
                        onOptionChange={this.onNewLanguageChange} />
                    { addButton }
                </form>
            </div>
        );
    }
}
