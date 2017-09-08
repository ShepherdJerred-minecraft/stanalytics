
package com.shepherdjerred.stanalytics.commands.mainsubcommands;

import com.shepherdjerred.stanalytics.files.ConfigHelper;
import com.shepherdjerred.stanalytics.messages.commands.GenericMessages;
import com.shepherdjerred.stanalytics.mysql.HikariManager;
import com.shepherdjerred.stanalytics.mysql.TableManager;
import org.bukkit.command.CommandSender;


public class ReloadSubCommand {

    public static void Executor(CommandSender sender, String[] args) {

        if (!sender.hasPermission("stAnalytics.reload")) {

            sender.sendMessage(GenericMessages.getNoPermsMessage());
            return;

        }

        ConfigHelper.loadConfigs();

        HikariManager.getInstance().setupPool();

        TableManager.checkTables();

        sender.sendMessage(GenericMessages.getReloadSuccess());

    }

}
