package com.shepherdjerred.stanalytics.messages.commands;

import com.shepherdjerred.stanalytics.messages.MessageHelper;

public class GenericMessages {

    public static String getNoPermsMessage() {

        return getMessagePrefix() + MessageHelper.colorMessagesString("generic.noPerms");

    }

    public static String getNoConsoleMessage() {

        return getMessagePrefix() + MessageHelper.colorMessagesString("generic.noConsole");

    }

    public static String getNoArgsMessage(String correctArgs) {

        return getMessagePrefix() + MessageHelper.colorMessagesString("generic.noArgs.correct").replace("%args%", correctArgs);

    }

    public static String getInvalidArgsMessage(String givenArg, String correctArgs) {

        return getMessagePrefix() + MessageHelper.colorMessagesString("generic.invalidArg.correct").replace("%arg%", givenArg).replace("%args%", correctArgs);

    }

    public static String getMessagePrefix() {

        return MessageHelper.colorMessagesString("prefix");

    }

    public static String getReloadSuccess() {

        return getMessagePrefix() + MessageHelper.colorMessagesString("commands.mainsubcommands.reload.success");

    }
}
