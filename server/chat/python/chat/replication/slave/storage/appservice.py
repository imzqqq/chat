from chat.storage.databases.main.appservice import (
    ApplicationServiceTransactionWorkerStore,
    ApplicationServiceWorkerStore,
)


class SlavedApplicationServiceStore(
    ApplicationServiceTransactionWorkerStore, ApplicationServiceWorkerStore
):
    pass
