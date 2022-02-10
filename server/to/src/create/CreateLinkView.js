import {TemplateView} from "../utils/TemplateView.js";
import {PreviewView} from "../preview/PreviewView.js";
import {copyButton} from "../utils/copy.js";

export class CreateLinkView extends TemplateView {
    render(t, vm) {
        const link = t.a({href: vm => vm.linkUrl}, vm => vm.linkUrl);
        return t.div({className: "CreateLinkView card"}, [
            t.h1("Create shareable links to Chat rooms, users or messages without being tied to any app"),
            t.form({action: "#", onSubmit: evt => this._onSubmit(evt)}, [
                t.div(t.input({
                    className: "fullwidth large",
                    type: "text",
                    name: "identifier",
                    required: true,
                    placeholder: "#room:example.com, @user:example.com",
                    onChange: evt => this._onIdentifierChange(evt)
                })),
                t.div(t.input({className: "primary fullwidth", type: "submit", value: "Create link"}))
            ]),
        ]);
    }

    _onSubmit(evt) {
        evt.preventDefault();
        const form = evt.target;
        const {identifier} = form.elements;
        this.value.createLink(identifier.value);
        identifier.value = "";
    }

    _onIdentifierChange(evt) {
        const inputField = evt.target;
        if (!this.value.validateIdentifier(inputField.value)) {
            inputField.setCustomValidity("That doesn't seem valid. Try #room:example.com, @user:example.com or +group:example.com.");
        } else {
            inputField.setCustomValidity("");
        }
    }
}
