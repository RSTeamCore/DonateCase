package net.ritasister.tools;

import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

import net.ritasister.dc.Main;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class CustomConfig
{
    FileConfiguration config;
    File file;
    
    public CustomConfig(final String name) {
        this.file = new File(Main.plugin.getDataFolder(), String.valueOf(String.valueOf(String.valueOf(name))) + ".yml");
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public void save() {
        try {
            this.config.save(this.file);
        }
        catch (IOException var2) {
            var2.printStackTrace();
        }
    }
}
