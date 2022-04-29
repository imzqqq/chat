import {TemplateView} from "./utils/TemplateView.js";
import {OpenLinkView} from "./open/OpenLinkView.js";
import {CreateLinkView} from "./create/CreateLinkView.js";
import {LoadServerPolicyView} from "./policy/LoadServerPolicyView.js";

export class RootView extends TemplateView {
    render(t, vm) {
        return t.div({className: "RootView"}, [
            t.p(t.img({className: "center", src: "images/brand.svg"})),
            t.mapView(vm => vm.openLinkViewModel, vm => vm ? new OpenLinkView(vm) : null),
            t.mapView(vm => vm.createLinkViewModel, vm => vm ? new CreateLinkView(vm) : null),
            t.mapView(vm => vm.loadServerPolicyViewModel, vm => vm ? new LoadServerPolicyView(vm) : null),
            t.div({className: "footer"}, [
                t.p(["This invite uses ", externalLink(t, "https://chat.imzqqq.top", "Chat"), ", an open network for secure, decentralized communication."]),
                t.ul({className: "links"}, [
                    t.li(externalLink(t, "https://github.com/imzqqq", "GitHub")),
                    t.li({className: {hidden: vm => !vm.hasPreferences}},
                        t.button({className: "text", onClick: () => vm.clearPreferences()}, "Clear preferences")),
                ])
            ])
        ]);
    }
}

function externalLink(t, href, label) {
    return t.a({href, target: "_blank", rel: "noopener noreferrer"}, label);
}
