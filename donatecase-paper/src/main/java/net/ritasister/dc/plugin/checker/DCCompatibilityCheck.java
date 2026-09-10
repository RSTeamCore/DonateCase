package net.ritasister.dc.plugin.checker;

import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.plugin.loader.PluginDisabler;
import net.ritasister.dc.util.utility.platform.PlatformDetector;
import net.ritasister.dc.util.utility.version.MinecraftVersionChecker;

public class DCCompatibilityCheck {

    private final MinecraftVersionChecker versionChecker;
    private final PlatformDetector platformDetector;
    private final PluginDisabler pluginDisabler;

    public DCCompatibilityCheck(MinecraftVersionChecker versionChecker,
                                PlatformDetector platformDetector,
                                PluginDisabler pluginDisabler) {
        this.versionChecker = versionChecker;
        this.platformDetector = platformDetector;
        this.pluginDisabler = pluginDisabler;
    }

    public boolean performCompatibilityChecks() {
        if (!versionChecker.check(pluginDisabler.getPlugin())) {
            pluginDisabler.disableWithMessage("Incompatible Minecraft version");
            return false;
        }

        final String platform = platformDetector.detectPlatform(pluginDisabler.getPlugin().getBootstrap(), pluginDisabler.getPlugin().getType());

        if (Platform.Type.UNKNOWN.getPlatformName().equals(platform)) {
            pluginDisabler.disableWithMessage("Unsupported server platform: " + platform);
            return false;
        }

        return true;
    }
}
