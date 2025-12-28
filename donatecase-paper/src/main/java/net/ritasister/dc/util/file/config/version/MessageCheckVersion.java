package net.ritasister.dc.util.file.config.version;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.config.version.VersionChecker;
import net.ritasister.dc.config.ConfigType;
import net.ritasister.dc.util.file.config.field.ConfigFields;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class MessageCheckVersion implements VersionChecker<DonateCasePaperPlugin> {

    private final VersionUpdateService versionUpdateService;

    public MessageCheckVersion(VersionUpdateService versionUpdateService) {
        this.versionUpdateService = versionUpdateService;
    }

    @Override
    public boolean check(@NotNull DonateCasePaperPlugin plugin) {
        final File langFile = new File(plugin.getBootstrap().getLoader().getDataFolder(),
                "lang/" + ConfigFields.LANG.asString(plugin) + ".yml");

        try {
            versionUpdateService.checkAndUpdate(plugin, ConfigType.LANG, langFile, "lang/" + ConfigFields.LANG.asString(plugin) + ".yml");
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to check/update language file: " + e.getMessage());
            return false;
        }
    }
}
