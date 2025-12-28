package net.ritasister.dc.util.utility.updater;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.util.schedulers.FoliaRunnable;
import net.ritasister.dc.util.utility.platform.PlatformDetector;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class UpdateChecker {

    private final DonateCasePaperPlugin donateCasePaperPlugin;
    private final int resourceId;

    public UpdateChecker(DonateCasePaperPlugin donateCasePaperPlugin, int resourceId) {
        this.donateCasePaperPlugin = donateCasePaperPlugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        final String platformName = PlatformDetector.getPlatformName();

        if (platformName.equals(Platform.Type.BUKKIT.getPlatformName())
                || platformName.equals(Platform.Type.SPIGOT.getPlatformName())) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this.donateCasePaperPlugin.getBootstrap().getLoader(), t ->
                    checkUpdate(consumer, platformName), 0, 6 * 60 * 60 * 20);
        } else if (platformName.equals(Platform.Type.PAPER.getPlatformName())) {
            Bukkit.getAsyncScheduler().runAtFixedRate(this.donateCasePaperPlugin.getBootstrap().getLoader(), t ->
                    checkUpdate(consumer, Platform.Type.PAPER.getPlatformName()), 0, 6, TimeUnit.HOURS);
        } else if (platformName.equals(Platform.Type.FOLIA.getPlatformName())) {
            new FoliaRunnable(Bukkit.getAsyncScheduler(), TimeUnit.HOURS) {
                @Override
                public void run() {
                    checkUpdate(consumer, Platform.Type.FOLIA.getPlatformName());
                }
            }.runAtFixedRate(donateCasePaperPlugin.getBootstrap().getLoader(), 0, 6);
        }
    }

    private void checkUpdate(final Consumer<String> consumer, String platformName) {
        donateCasePaperPlugin.getLogger().info(String.format("Checking for updates using %s schedulers.", platformName));
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
             Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            this.donateCasePaperPlugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
        }
    }

}
