package net.ritasister.dc.tools;

import net.ritasister.dc.DonateCase;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.util.Objects;

public class Languages {
    public FileConfiguration lang;
    
    public Languages(final String lang) {
        final File path = new File(DonateCase.instance.getDataFolder(), "lang");
        File[] listFiles;
        for (int length = (Objects.requireNonNull(listFiles = path.listFiles())).length, i = 0; i < length; ++i) {
            final File l = listFiles[i];
            if (l.getName().toLowerCase().split("_")[0].equalsIgnoreCase(lang)) 
            {
                this.lang = YamlConfiguration.loadConfiguration(l);
            }
        }
    }
    
    public FileConfiguration getLang() 
    {
        return this.lang;
    }
}
