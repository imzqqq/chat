import React from 'react';
import { _t } from "../../../languageHandler";
import { CommandCategories, Commands } from "../../../SlashCommands";
import * as sdk from "../../../index";

export default ({ onFinished }) => {
    const InfoDialog = sdk.getComponent('dialogs.InfoDialog');

    const categories = {};
    Commands.forEach(cmd => {
        if (!cmd.isEnabled()) return;
        if (!categories[cmd.category]) {
            categories[cmd.category] = [];
        }
        categories[cmd.category].push(cmd);
    });

    const body = Object.values(CommandCategories).filter(c => categories[c]).map((category) => {
        const rows = [
            <tr key={"_category_" + category} className="mx_SlashCommandHelpDialog_headerRow">
                <td colSpan={3}>
                    <h2>{ _t(category) }</h2>
                </td>
            </tr>,
        ];

        categories[category].forEach(cmd => {
            rows.push(<tr key={cmd.command}>
                <td><strong>{ cmd.getCommand() }</strong></td>
                <td>{ cmd.args }</td>
                <td>{ cmd.description }</td>
            </tr>);
        });

        return rows;
    });

    return <InfoDialog
        className="mx_SlashCommandHelpDialog"
        title={_t("Command Help")}
        description={<table>
            <tbody>
                { body }
            </tbody>
        </table>}
        hasCloseButton={true}
        onFinished={onFinished} />;
};
