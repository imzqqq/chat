/*
Copyright 2019, 2020 The Matrix.org Foundation C.I.C.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import { Mjolnir, STATE_CHECKING_PERMISSIONS, STATE_NOT_STARTED, STATE_RUNNING, STATE_SYNCING } from "../Mjolnir";
import { RichReply } from "matrix-bot-sdk";

// !mjolnir
export async function execStatusCommand(roomId: string, event: any, mjolnir: Mjolnir) {
    let html = "";
    let text = "";

    const state = mjolnir.state;

    switch (state) {
        case STATE_NOT_STARTED:
            html += "<b>Running: </b>❌ (not started)<br/>";
            text += "Running: ❌ (not started)\n";
            break;
        case STATE_CHECKING_PERMISSIONS:
            html += "<b>Running: </b>❌ (checking own permissions)<br/>";
            text += "Running: ❌ (checking own permissions)\n";
            break;
        case STATE_SYNCING:
            html += "<b>Running: </b>❌ (syncing lists)<br/>";
            text += "Running: ❌ (syncing lists)\n";
            break;
        case STATE_RUNNING:
            html += "<b>Running: </b>✅<br/>";
            text += "Running: ✅\n";
            break;
        default:
            html += "<b>Running: </b>❌ (unknown state)<br/>";
            text += "Running: ❌ (unknown state)\n";
            break;
    }

    html += `<b>Protected rooms: </b> ${Object.keys(mjolnir.protectedRooms).length}<br/>`;
    text += `Protected rooms: ${Object.keys(mjolnir.protectedRooms).length}\n`;

    // Append list information
    html += "<b>Subscribed ban lists:</b><br><ul>";
    text += "Subscribed ban lists:\n";
    for (const list of mjolnir.lists) {
        const ruleInfo = `rules: ${list.serverRules.length} servers, ${list.userRules.length} users, ${list.roomRules.length} rooms`;
        html += `<li><a href="${list.roomRef}">${list.roomId}</a> (${ruleInfo})</li>`;
        text += `* ${list.roomRef} (${ruleInfo})\n`;
    }
    if (mjolnir.lists.length === 0) {
        html += "<li><i>None</i></li>";
        text += "* None\n";
    }
    html += "</ul>";

    const reply = RichReply.createFor(roomId, event, text, html);
    reply["msgtype"] = "m.notice";
    return mjolnir.client.sendMessage(roomId, reply);
}
