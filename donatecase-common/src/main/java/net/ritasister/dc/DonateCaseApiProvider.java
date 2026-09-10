package net.ritasister.dc;

import net.ritasister.dc.api.DonateCase;
import net.ritasister.dc.api.DonateCaseProvider;
import net.ritasister.dc.api.implementation.ApiPlatform;
import net.ritasister.dc.api.logging.PluginLogger;
import net.ritasister.dc.api.metadata.DonateCaseMetadata;
import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.plugin.DonateCasePlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class DonateCaseApiProvider implements DonateCase {

    private final DonateCasePlugin plugin;

    private final ApiPlatform platform;

    public DonateCaseApiProvider(DonateCasePlugin plugin) {
        this.plugin = plugin;

        this.platform = new ApiPlatform(plugin);
    }

    public void ensureApiWasLoadedByPlugin() {
        final DonateCasePlugin worldGuardRegionProtectPlugin = this.plugin;
        final ClassLoader pluginClassLoader = worldGuardRegionProtectPlugin.getClass().getClassLoader();

        for (Class<?> apiClass : new Class[]{DonateCase.class, DonateCaseProvider.class}) {
            final ClassLoader apiClassLoader = apiClass.getClassLoader();

            if (!apiClassLoader.equals(pluginClassLoader)) {
                String guilty = "unknown";
                try {
                    guilty = worldGuardRegionProtectPlugin.identifyClassLoader(apiClassLoader);
                } catch (Exception ignored) {}

                final PluginLogger logger = this.plugin.getLogger();
                logger.warn("It seems that the WorldGuardRegionProtect API has been (class)loaded by a plugin other than " +
                        "WorldGuardRegionProtect!");
                logger.warn("The API was loaded by " + apiClassLoader + " (" + guilty + ") and the " +
                        "WorldGuardRegionProtect plugin was loaded by " + pluginClassLoader.toString() + ".");
                logger.warn("This indicates that the other plugin has incorrectly \"shaded\" the " +
                        "WorldGuardRegionProtect API into its jar file. This can cause errors at runtime and should be fixed.");
                return;
            }
        }
    }

    @Override
    public @NonNull DonateCaseMetadata getMetaData() {
        return this.platform;
    }

    @Override
    public @NotNull Platform getPlatform() {
        return this.platform;
    }

}
