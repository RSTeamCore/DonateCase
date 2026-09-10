package net.ritasister.dc.util.file.config.version;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.config.version.VersionChecker;
import net.ritasister.dc.config.ConfigType;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class ConfigCheckVersion implements VersionChecker<DonateCasePaperPlugin> {

    private final VersionUpdateService versionUpdateService;

    public ConfigCheckVersion(VersionUpdateService versionUpdateService) {
        this.versionUpdateService = versionUpdateService;
    }

    @Override
    public boolean check(@NotNull DonateCasePaperPlugin plugin) {
        final File configFile = new File(plugin.getBootstrap().getLoader().getDataFolder(), "config.yml");

        try {
            versionUpdateService.checkAndUpdate(plugin, ConfigType.CONFIG, configFile, "config.yml");
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to check/update config.yml: " + e.getMessage());
            return false;
        }
    }
}
