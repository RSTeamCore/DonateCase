package net.ritasister.dc.util.file.config.provider;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.config.provider.MessageProvider;
import net.ritasister.dc.util.file.config.field.ConfigFields;
import net.ritasister.dc.util.file.config.files.Messages;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class MessagesProvider implements MessageProvider<DonateCasePaperPlugin, Messages> {

    private Messages messages;

    @Override
    public void init(@NotNull final DonateCasePaperPlugin plugin) {
        final String lang = ConfigFields.LANG.asString(plugin);
        final File file = new File(plugin.getBootstrap().getLoader().getDataFolder(), "lang/" + lang + ".yml");

        if (!file.exists()) {
            plugin.getBootstrap().getLoader().saveResource("lang/" + lang + ".yml", false);
        }

        this.messages = new Messages(YamlConfiguration.loadConfiguration(file));
        plugin.getLogger().info("Messages loaded for language: " + lang);
    }

    @Override
    public Messages get() {
        return messages;
    }
}
