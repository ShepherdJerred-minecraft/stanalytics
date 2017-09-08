package com.shepherdjerred.stanalytics.commands.mainsubcommands;

import com.shepherdjerred.stanalytics.messages.commands.GenericMessages;
import org.bukkit.command.CommandSender;

public class HelpSubCommand {

    public static void Executor(CommandSender sender, String[] args) {

        if (!sender.hasPermission("stAnalytics.help")) {
            sender.sendMessage(GenericMessages.getNoPermsMessage());
            return;
        }

        sender.sendMessage("This command isn't ready yet");

    }

}
