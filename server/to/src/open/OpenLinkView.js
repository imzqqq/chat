import {TemplateView} from "../utils/TemplateView.js";
import {ClientListView} from "./ClientListView.js";
import {PreviewView} from "../preview/PreviewView.js";
import {ServerConsentView} from "./ServerConsentView.js";

export class OpenLinkView extends TemplateView {
    render(t, vm) {
        return t.div({className: "OpenLinkView card"}, [
            t.mapView(vm => vm.previewViewModel, previewVM => previewVM ?
                new ShowLinkView(vm) :
                new ServerConsentView(vm.serverConsentViewModel)
            ),
        ]);
    }
}

class ShowLinkView extends TemplateView {
    render(t, vm) {
        return t.div([
            t.view(new PreviewView(vm.previewViewModel)),
            t.view(new ClientListView(vm.clientsViewModel)),
            t.p({className: {caption: true, hidden: vm => !vm.previewDomain}}, [
                vm => vm.previewFailed ? `${vm.previewDomain} has not returned a preview.` : `Preview provided by ${vm.previewDomain}`,
                " · ",
                t.button({className: "text", onClick: () => vm.changeServer()}, "Change"),
            ]),
        ]);
    }
}
