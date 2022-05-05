package io.kamax.mxisd.http.undertow.handler.internal;

import com.google.gson.JsonObject;
import io.kamax.mxisd.http.undertow.handler.BasicHttpHandler;
import io.kamax.mxisd.invitation.InvitationManager;
import io.undertow.server.HttpServerExchange;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InternalInviteManagerHandler extends BasicHttpHandler {

    public static final String PATH = "/_ma1sd/internal/admin/inv_manager";

    private final InvitationManager invitationManager;
    private final ExecutorService executors = Executors.newFixedThreadPool(1);

    public InternalInviteManagerHandler(InvitationManager invitationManager) {
        this.invitationManager = invitationManager;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        executors.submit(invitationManager::doMaintenance);

        JsonObject obj = new JsonObject();
        obj.addProperty("result", "ok");
        respond(exchange, obj);
    }
}
