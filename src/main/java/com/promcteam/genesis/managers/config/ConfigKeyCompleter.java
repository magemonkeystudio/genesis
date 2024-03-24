package com.promcteam.genesis.managers.config;

import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class ConfigKeyCompleter {
    public static void checkConfig() {
        InputStream is = ClassManager.manager.getPlugin().getResource("config.yml");
        assert is != null;
        File f = new File(ClassManager.manager.getPlugin().getDataFolder(), "config.yml");
        File f2;
        try {
            f2 = File.createTempFile("config-cache", "yml");//make cache
            copyInputStreamToFile(is, f2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addKeys(f, f2);
    }

    private static void checkEnUsLanguage() {
        InputStream is = ClassManager.manager.getPlugin().getResource("lang/en-us.yml");
        assert is != null;
        File f = new File(ClassManager.manager.getPlugin().getDataFolder(), "lang/en-us.yml");
        File f2;
        try {
            f2 = File.createTempFile("en-us-lang-cache", "yml");//make cache
            copyInputStreamToFile(is, f2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addKeys(f, f2);
    }

    private static void addKeys(File f, File f2) {
        FileConfiguration c        = YamlConfiguration.loadConfiguration(f);
        FileConfiguration c2       = YamlConfiguration.loadConfiguration(f2);
        Set<String>       key      = c.getKeys(true);
        Set<String>       key2     = c2.getKeys(true);
        Set<String>       willAdds = new HashSet<>(key2);
        willAdds.removeAll(key);
        for (String configKey : willAdds) {
            String value = c2.getString(configKey);
            c.set(configKey, value);
        }
        f2.delete();
    }

    public static void checkLanguages() {
        checkEnUsLanguage();
        checkZhCnLanguage();
    }

    private static void checkZhCnLanguage() {
        InputStream is = ClassManager.manager.getPlugin().getResource("lang/zh-cn.yml");
        assert is != null;
        File f = new File(ClassManager.manager.getPlugin().getDataFolder(), "lang/zh-cn.yml");
        File f2;
        try {
            f2 = File.createTempFile("zh-cn-lang-cache", "yml");//make cache
            copyInputStreamToFile(is, f2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        addKeys(f, f2);
    }

    private static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int    read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
