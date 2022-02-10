import { AsyncStoreWithClient } from "./AsyncStoreWithClient";
import defaultDispatcher from "../dispatcher/dispatcher";
import { ActionPayload } from "../dispatcher/payloads";
import Modal, { IHandle, IModal } from "../Modal";
import ModalWidgetDialog from "../components/views/dialogs/ModalWidgetDialog";
import { WidgetMessagingStore } from "./widgets/WidgetMessagingStore";
import { IModalWidgetOpenRequestData, IModalWidgetReturnData, Widget } from "matrix-widget-api";

interface IState {
    modal?: IModal<any>;
    openedFromId?: string;
}

export class ModalWidgetStore extends AsyncStoreWithClient<IState> {
    private static internalInstance = new ModalWidgetStore();
    private modalInstance: IHandle<void[]> = null;
    private openSourceWidgetId: string = null;

    private constructor() {
        super(defaultDispatcher, {});
    }

    public static get instance(): ModalWidgetStore {
        return ModalWidgetStore.internalInstance;
    }

    protected async onAction(payload: ActionPayload): Promise<any> {
        // nothing
    }

    public canOpenModalWidget = () => {
        return !this.modalInstance;
    };

    public openModalWidget = (
        requestData: IModalWidgetOpenRequestData,
        sourceWidget: Widget,
        widgetRoomId?: string,
    ) => {
        if (this.modalInstance) return;
        this.openSourceWidgetId = sourceWidget.id;
        this.modalInstance = Modal.createTrackedDialog('Modal Widget', '', ModalWidgetDialog, {
            widgetDefinition: { ...requestData },
            widgetRoomId,
            sourceWidgetId: sourceWidget.id,
            onFinished: (success: boolean, data?: IModalWidgetReturnData) => {
                if (!success) {
                    this.closeModalWidget(sourceWidget, { "m.exited": true });
                } else {
                    this.closeModalWidget(sourceWidget, data);
                }

                this.openSourceWidgetId = null;
                this.modalInstance = null;
            },
        }, null, /* priority = */ false, /* static = */ true);
    };

    public closeModalWidget = (sourceWidget: Widget, data?: IModalWidgetReturnData) => {
        if (!this.modalInstance) return;
        if (this.openSourceWidgetId === sourceWidget.id) {
            this.openSourceWidgetId = null;
            this.modalInstance.close();
            this.modalInstance = null;

            const sourceMessaging = WidgetMessagingStore.instance.getMessaging(sourceWidget);
            if (!sourceMessaging) {
                console.error("No source widget messaging for modal widget");
                return;
            }
            sourceMessaging.notifyModalWidgetClose(data);
        }
    };
}

window.mxModalWidgetStore = ModalWidgetStore.instance;
