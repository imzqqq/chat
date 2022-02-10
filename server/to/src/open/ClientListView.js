import {TemplateView} from "../utils/TemplateView.js";
import {ClientView} from "./ClientView.js";

export class ClientListView extends TemplateView {
    render(t, vm) {
        return t.mapView(vm => vm.clientViewModel, () => {
            if (vm.clientViewModel) {
                return new ContinueWithClientView(vm);
            } else {
                return new AllClientsView(vm);
            }
        });
    }
}

class AllClientsView extends TemplateView {
    render(t, vm) {
        return t.div({className: "ClientListView"}, [
            t.h2("Choose an app to continue"),
            t.map(vm => vm.clientList, (clientList, t) => {
                return t.div({className: "list"}, clientList.map(clientViewModel => {
                    return t.view(new ClientView(clientViewModel));
                }));
            }),
            t.div(t.label([
                t.input({
                    type: "checkbox",
                    checked: vm.showUnsupportedPlatforms,
                    onChange: evt => vm.showUnsupportedPlatforms = evt.target.checked,
                }),
                "Show apps not available on my platform"
            ])),
            t.div(t.label({className: "filterOption"}, [
                t.input({
                    type: "checkbox",
                    checked: vm.showExperimental,
                    onChange: evt => vm.showExperimental = evt.target.checked,
                }),
                "Show experimental apps"
            ])),
        ]);
    }
}

class ContinueWithClientView extends TemplateView {
    render(t, vm) {
        return t.div({className: "ClientListView"}, [
            t.div({className: "list"}, t.view(new ClientView(vm.clientViewModel)))
        ]);
    }
}
