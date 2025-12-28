package net.ritasister.dc;

import net.ritasister.dc.api.logging.JavaPluginLogger;
import net.ritasister.dc.api.logging.PluginLogger;
import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.plugin.DonateCaseBootstrap;
import net.ritasister.dc.plugin.DonateCasePlugin;
import net.ritasister.dc.util.utility.platform.PlatformDetector;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DCBootstrap implements DonateCaseBootstrap {

    private final JavaPlugin loader;

    private final DonateCasePlugin plugin;
    private final Instant startTime;

    private final PluginLogger logger;

    public DCBootstrap(@NonNull JavaPlugin loader, DonateCasePlugin plugin) {
        this.loader = loader;
        this.logger = new JavaPluginLogger(loader.getLogger());
        this.plugin = plugin;
        this.startTime = Instant.now();
    }

    public Server getServer() {
        return this.loader.getServer();
    }

    @Override
    public PluginLogger getPluginLogger() {
        return this.logger;
    }

    @Override
    public String getVersion() {
        return this.loader.getDescription().getVersion();
    }

    @Override
    public Instant getStartupTime() {
        return this.startTime;
    }

    @Override
    public Platform.Type getType() {
        final PlatformDetector detector = new PlatformDetector(getPluginLogger());
        final String detected = detector.detectPlatform(plugin.getBootstrap(), Platform.Type.UNKNOWN);

        return switch (detected) {
            case "Paper" -> Platform.Type.PAPER;
            case "Folia" -> Platform.Type.FOLIA;
            case "Spigot" -> Platform.Type.SPIGOT;
            case "Bukkit" -> Platform.Type.BUKKIT;
            default -> Platform.Type.UNKNOWN;
        };
    }

    @Override
    public String getServerBrand() {
        return getServer().getName();
    }

    @Override
    public String getServerVersion() {
        return getServer().getVersion() + " - " + getServer().getBukkitVersion();
    }

    @Override
    public String getMinecraftVersion() {
        return getServer().getMinecraftVersion();
    }

    @Override
    public @Nullable String getServerName() {
        return getServer().getName();
    }

    @Override
    public Optional<Player> getPlayer(UUID uniqueId) {
        return Optional.ofNullable(getServer().getPlayer(uniqueId));
    }

    public JavaPlugin getLoader() {
        return this.loader;
    }

}
