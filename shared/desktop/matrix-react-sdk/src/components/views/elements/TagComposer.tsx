import React, { ChangeEvent, FormEvent } from "react";
import { replaceableComponent } from "../../../utils/replaceableComponent";
import Field from "./Field";
import { _t } from "../../../languageHandler";
import AccessibleButton from "./AccessibleButton";

interface IProps {
    tags: string[];
    onAdd: (tag: string) => void;
    onRemove: (tag: string) => void;
    disabled?: boolean;
    label?: string;
    placeholder?: string;
}

interface IState {
    newTag: string;
}

/**
 * A simple, controlled, composer for entering string tags. Contains a simple
 * input, add button, and per-tag remove button.
 */
@replaceableComponent("views.elements.TagComposer")
export default class TagComposer extends React.PureComponent<IProps, IState> {
    public constructor(props: IProps) {
        super(props);

        this.state = {
            newTag: "",
        };
    }

    private onInputChange = (ev: ChangeEvent<HTMLInputElement>) => {
        this.setState({ newTag: ev.target.value });
    };

    private onAdd = (ev: FormEvent) => {
        ev.preventDefault();
        if (!this.state.newTag) return;

        this.props.onAdd(this.state.newTag);
        this.setState({ newTag: "" });
    };

    private onRemove(tag: string) {
        // We probably don't need to proxy this, but for
        // sanity of `this` we'll do so anyways.
        this.props.onRemove(tag);
    }

    public render() {
        return <div className='mx_TagComposer'>
            <form className='mx_TagComposer_input' onSubmit={this.onAdd}>
                <Field
                    value={this.state.newTag}
                    onChange={this.onInputChange}
                    label={this.props.label || _t("Keyword")}
                    placeholder={this.props.placeholder || _t("New keyword")}
                    disabled={this.props.disabled}
                    autoComplete="off"
                />
                <AccessibleButton onClick={this.onAdd} kind='primary' disabled={this.props.disabled}>
                    { _t("Add") }
                </AccessibleButton>
            </form>
            <div className='mx_TagComposer_tags'>
                { this.props.tags.map((t, i) => (<div className='mx_TagComposer_tag' key={i}>
                    <span>{ t }</span>
                    <AccessibleButton onClick={this.onRemove.bind(this, t)} disabled={this.props.disabled} />
                </div>)) }
            </div>
        </div>;
    }
}
