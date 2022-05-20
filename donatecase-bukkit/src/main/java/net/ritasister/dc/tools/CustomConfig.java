package net.ritasister.dc.tools;

import net.ritasister.dc.DonateCase;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CustomConfig {
    FileConfiguration config;
    File file;
    
    public CustomConfig(final String name) {
        this.file = new File(DonateCase.instance.getDataFolder(), name + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public FileConfiguration getConfig() 
    {
        return this.config;
    }
    
    public void save() {
        try{
            this.config.save(this.file);
        }catch (IOException var2){
            var2.printStackTrace();
        }
    }
}
