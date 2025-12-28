package net.ritasister.dc.util.file.config.provider;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.util.file.config.files.Config;
import org.jspecify.annotations.NonNull;

public class ConfigProvider implements net.ritasister.dc.api.config.provider.ConfigProvider<DonateCasePaperPlugin, Config> {

    private Config config;

    @Override
    public void init(@NonNull DonateCasePaperPlugin plugin) {
        this.config = new Config(plugin);
    }

    @Override
    public Config get() {
        if (config == null) {
            throw new IllegalStateException("config provider is not initialized yet! Call init(plugin) first.");
        }
        return config;
    }
}
