import {TemplateView} from "../utils/TemplateView.js";

export class LoadServerPolicyView extends TemplateView {
    render(t, vm) {
        return t.div({className: "LoadServerPolicyView card"}, [
            t.div({className: {spinner: true, hidden: vm => !vm.loading}}),
            t.h2(vm => vm.message)
        ]);
    }
}
