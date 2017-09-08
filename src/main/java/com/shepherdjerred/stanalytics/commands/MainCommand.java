
package com.shepherdjerred.stanalytics.commands;

import com.shepherdjerred.stanalytics.commands.mainsubcommands.HelpSubCommand;
import com.shepherdjerred.stanalytics.commands.mainsubcommands.ReloadSubCommand;
import com.shepherdjerred.stanalytics.messages.commands.GenericMessages;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class MainCommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(GenericMessages.getNoArgsMessage("<help, set>"));
            return true;
        }

        if (!EnumUtils.isValidEnum(SubCommands.class, args[0].toUpperCase())) {
            sender.sendMessage(GenericMessages.getInvalidArgsMessage(args[0], "<help, set>"));
            return true;
        }

        switch (SubCommands.valueOf(args[0].toUpperCase())) {
            case HELP:
                HelpSubCommand.Executor(sender, args);
                return true;
            case RELOAD:
                ReloadSubCommand.Executor(sender, args);
                return true;
        }

        return true;

    }

    private enum SubCommands {

        RELOAD, HELP

    }

}