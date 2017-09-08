package com.shepherdjerred.stanalytics.messages;

import com.shepherdjerred.stanalytics.Main;
import com.shepherdjerred.stanalytics.files.FileManager;
import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageHelper {

    /**
     * Translates chat codes into a colored string
     *
     * @param input The string to be colored
     * @return A formatted, colored string
     */
    @NotNull
    public static String colorString(@NotNull String input) {
        Validate.notNull(input);
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    @NotNull
    public static String colorConfigString(@NotNull String path) {
        Validate.notNull(path);

        if (Main.getInstance().getConfig().getString(path) == null)
            return "CANNOT FIND PATH " + path + " IN CONFIG.YML";

        return colorString(Main.getInstance().getConfig().getString(path));
    }

    @NotNull
    public static List<String> colorConfigListStrings(@NotNull String path) {
        Validate.notNull(path);

        if (Main.getInstance().getConfig().getStringList(path) == null)
            return Arrays.asList("CANNOT FIND PATH " + path + " IN CONFIG.YML");

        List<String> output = new ArrayList<>();
        Main.getInstance().getConfig().getStringList(path).forEach(s -> output.add(colorString(s)));
        return output;
    }

    @NotNull
    public static String colorMessagesString(@NotNull String path) {
        Validate.notNull(path);

        if (FileManager.getInstance().messages.getString(path) == null)
            return "CANNOT FIND PATH " + path + " IN MESSAGES.YML";

        return colorString(FileManager.getInstance().messages.getString(path));
    }

    @NotNull
    public static List<String> colorMessagesListStrings(@NotNull String path) {
        Validate.notNull(path);

        if (FileManager.getInstance().messages.getStringList(path) == null)
            return Arrays.asList("CANNOT FIND PATH " + path + " IN MESSAGES.YML");

        List<String> output = new ArrayList<>();
        Main.getInstance().getConfig().getStringList(path).forEach(s -> output.add(colorString(s)));
        return output;
    }

}
