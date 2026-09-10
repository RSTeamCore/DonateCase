package net.ritasister.dc.util.file.config.loader;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.config.provider.ConfigProvider;
import net.ritasister.dc.api.config.provider.MessageProvider;
import net.ritasister.dc.api.config.version.VersionChecker;
import net.ritasister.dc.util.file.config.files.Config;
import net.ritasister.dc.util.file.config.files.Messages;

public final class ConfigLoader {

    private final ConfigProvider<DonateCasePaperPlugin, Config> configProvider;
    private final MessageProvider<DonateCasePaperPlugin, Messages> messageProvider;
    private final VersionChecker<DonateCasePaperPlugin> configVersionChecker;
    private final VersionChecker<DonateCasePaperPlugin> langVersionChecker;

    public ConfigLoader(
            ConfigProvider<DonateCasePaperPlugin, Config> configProvider,
            MessageProvider<DonateCasePaperPlugin, Messages> messageProvider,
            VersionChecker<DonateCasePaperPlugin> configVersionChecker,
            VersionChecker<DonateCasePaperPlugin> langVersionChecker
    ) {
        this.configProvider = configProvider;
        this.messageProvider = messageProvider;
        this.configVersionChecker = configVersionChecker;
        this.langVersionChecker = langVersionChecker;
    }

    public void loadFiles(DonateCasePaperPlugin plugin) {
        configVersionChecker.check(plugin);
        langVersionChecker.check(plugin);

        configProvider.init(plugin);
        messageProvider.init(plugin);
    }

    public long reload(DonateCasePaperPlugin plugin) {
        final long start = System.currentTimeMillis();

        configVersionChecker.check(plugin);
        langVersionChecker.check(plugin);

        configProvider.init(plugin);
        messageProvider.init(plugin);

        return System.currentTimeMillis() - start;
    }

    public Config getConfig() {
        return configProvider.get();
    }
}
