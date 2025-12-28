package net.ritasister.dc.api.implementation;

import net.ritasister.dc.api.metadata.DonateCaseMetadata;
import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.plugin.DonateCasePlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class ApiPlatform implements Platform, DonateCaseMetadata {

    private final DonateCasePlugin plugin;

    public ApiPlatform(DonateCasePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getBootstrap().getVersion();
    }

    @Override
    public @NotNull String getApiVersion() {
        final String[] parts = this.plugin.getBootstrap().getVersion().split("\\.");
        return parts.length >= 2 ? parts[0] + '.' + parts[1] : this.plugin.getBootstrap().getVersion();
    }

    @Override
    public @NotNull Instant getStartTime() {
        return plugin.getBootstrap().getStartupTime();
    }

    @Override
    public Platform.@NotNull Type getType() {
        return this.plugin.getBootstrap().getType();
    }

}
