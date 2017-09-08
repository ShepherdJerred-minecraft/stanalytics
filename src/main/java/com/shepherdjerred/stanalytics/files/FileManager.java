
package com.shepherdjerred.stanalytics.files;

import com.shepherdjerred.stanalytics.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;


public class FileManager {

    private static FileManager instance;
    public FileConfiguration messages, storage;
    File messagesFile, storageFile;

    public FileManager() {
        instance = this;
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    // Load/reload files
    @SuppressWarnings("deprecation")
    public void loadFiles() {

        messagesFile = new File(Main.getInstance().getDataFolder(), "messages.yml");

        storageFile = new File(Main.getInstance().getDataFolder(), "storage.yml");

        if (!messagesFile.exists()) {

            messagesFile.getParentFile().mkdirs();
            copy(Main.getInstance().getResource("messages.yml"), messagesFile);

        }

        if (!storageFile.exists()) {

            storageFile.getParentFile().mkdirs();
            copy(Main.getInstance().getResource("storage.yml"), storageFile);

        }

        messages = new YamlConfiguration();
        storage = new YamlConfiguration();

        try {

            messages.load(messagesFile);
            storage.load(storageFile);

            messages.setDefaults(YamlConfiguration.loadConfiguration(Main.getInstance().getResource("messages.yml")));
            messages.options().copyDefaults(true);
            saveFiles(FileName.MESSAGES);

            storage.setDefaults(YamlConfiguration.loadConfiguration(Main.getInstance().getResource("storage.yml")));
            storage.options().copyDefaults(true);
            saveFiles(FileName.STORAGE);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // Save files
    public void saveFiles(FileName file) {
        try {

            if (file == FileName.MESSAGES) {

                messages.save(messagesFile);

                return;

            }

            if (file == FileName.STORAGE) {

                storage.save(storageFile);

                return;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Copy default files
    public void copy(InputStream in, File file) {

        try {

            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;

            while ((len = in.read(buf)) > 0) {

                out.write(buf, 0, len);

            }

            out.close();
            in.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public enum FileName {

        MESSAGES, STORAGE

    }

}