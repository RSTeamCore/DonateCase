package net.ritasister.dc;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.server.permissions.PermissionCheck;
import net.ritasister.dc.api.DonateCase;
import net.ritasister.dc.api.config.provider.ConfigProvider;
import net.ritasister.dc.api.config.provider.MessageProvider;
import net.ritasister.dc.api.config.version.ConfigVersionReader;
import net.ritasister.dc.api.config.version.VersionChecker;
import net.ritasister.dc.api.metadata.DonateCaseMetadata;
import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.config.ConfigType;
import net.ritasister.dc.plugin.AbstractDonateCasePlugin;
import net.ritasister.dc.plugin.checker.DCCompatibilityCheck;
import net.ritasister.dc.plugin.loader.PluginDisabler;
import net.ritasister.dc.util.file.UpdateFile;
import net.ritasister.dc.util.file.config.files.Config;
import net.ritasister.dc.util.file.config.files.Messages;
import net.ritasister.dc.util.file.config.loader.ConfigLoader;
import net.ritasister.dc.util.file.config.provider.MessagesProvider;
import net.ritasister.dc.util.file.config.provider.ZonedDateProvider;
import net.ritasister.dc.util.file.config.version.ConfigCheckVersion;
import net.ritasister.dc.util.file.config.version.MessageCheckVersion;
import net.ritasister.dc.util.file.config.version.VersionUpdateService;
import net.ritasister.dc.util.schedulers.FoliaRunnable;
import net.ritasister.dc.util.utility.platform.PlatformDetector;
import net.ritasister.dc.util.utility.updater.UpdateDownloaderGitHub;
import net.ritasister.dc.util.utility.updater.UpdateNotify;
import net.ritasister.dc.util.utility.version.MinecraftVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.ritasister.dc.util.utility.UtilityClass.isClassPresent;

public class DonateCasePaperPlugin extends AbstractDonateCasePlugin {

    private final DCBootstrap bootstrap;

    private PlatformDetector platformDetector;

    private BukkitAudiences adventure;
    private DonateCaseMetadata wgrpMetadata;
    private List<UUID> spyLog;

    private Map<Class<? extends Listener>, Listener> listenerHandlerMap = new HashMap<>();
    private Map<String, CommandExecutor> commandMap;
    private Map<Class<? extends FoliaRunnable>, FoliaRunnable> taskMap = new HashMap<>();

    private UpdateDownloaderGitHub downloader;
    private UpdateNotify updateNotify;

    private MinecraftVersionChecker versionCheck;

    private ConfigLoader configLoader;
    private ConfigProvider<DonateCasePaperPlugin, Config> configProvider;
    private MessageProvider<DonateCasePaperPlugin, Messages> messageProvider;

    public DonateCasePaperPlugin(DonateCasePaperBase donateCasePaperBase) {
        this.bootstrap = new DCBootstrap(donateCasePaperBase,this);
    }

    public void onEnable() {
        this.load();
        this.adventure = BukkitAudiences.create(this.bootstrap.getLoader());
        this.initializeFields();

        if (compatibleChecking()) {
            return;
        }

        this.initializeMetrics();
        this.loadAnotherClassAndMethods();
        this.logStartupTime();
    }

    private boolean compatibleChecking() {
        final MinecraftVersionChecker versionChecker = this.versionCheck;
        platformDetector = new PlatformDetector(this.getLogger());
        final PluginDisabler pluginDisabler = new PluginDisabler(this);
        final DCCompatibilityCheck compatibilityCheck = new DCCompatibilityCheck(versionChecker, platformDetector, pluginDisabler);

        return !compatibilityCheck.performCompatibilityChecks();
    }

    private void initializeFields() {

        this.versionCheck = new MinecraftVersionChecker(this.bootstrap);
        this.spyLog = new ArrayList<>();

        configProvider = new net.ritasister.dc.util.file.config.provider.ConfigProvider();
        messageProvider = new MessagesProvider();

        configLoader = configLoader();
        configLoader.loadFiles(this);

        this.listenerHandlerMap = new HashMap<>();
        this.commandMap = new HashMap<>();
        this.taskMap = new HashMap<>();

        this.downloader = new UpdateDownloaderGitHub(this);
        this.updateNotify = new UpdateNotify(this);
    }

    private @NonNull ConfigLoader configLoader() {
        final VersionUpdateService versionUpdateService = getVersionUpdateService();

        final VersionChecker<DonateCasePaperPlugin> configCheckVersion = new ConfigCheckVersion(versionUpdateService);
        final VersionChecker<DonateCasePaperPlugin> langCheckVersion = new MessageCheckVersion(versionUpdateService);

        return new ConfigLoader(configProvider, messageProvider, configCheckVersion, langCheckVersion);
    }

    private static @NonNull VersionUpdateService getVersionUpdateService() {
        final ConfigVersionReader<ConfigType, YamlConfiguration> versionReader = new net.ritasister.dc.util.file.config.version.ConfigVersionReaderImpl();
        final ZonedDateProvider dateProvider = new ZonedDateProvider();

        final UpdateFile updateFile = new UpdateFile(dateProvider);

        return new VersionUpdateService(versionReader, updateFile);
    }

    private void initializeMetrics() {
        final int pluginId = 12975;
        new Metrics(this.bootstrap.getLoader(), pluginId);
    }

    private void logStartupTime() {
        final Duration timeTaken = Duration.between(bootstrap.getStartupTime(), Instant.now());
        getLogger().info("Successfully enabled. (took " + timeTaken.toMillis() + "ms)");
    }

    public void onDisable() {
        try {
            unLoad();
        } catch (Exception exception) {
            this.getLogger().severe("Failed to unload resources: ", exception);
        }
        configProvider.get().saveConfigFiles();
        this.getLogger().info("Saved complete. Good luck and thanks for using WorldGuardRegionProtect!");
    }

    @Override
    protected void registerApiOnPlatform(final DonateCase api) {
        this.bootstrap.getServer().getServicesManager().register(DonateCase.class, api, this.bootstrap.getLoader(), ServicePriority.Normal);
    }

    private void loadAnotherClassAndMethods() {
        final LoadPlaceholderAPI loadPlaceholderAPI = new LoadPlaceholderAPI(this);
        loadPlaceholderAPI.loadPlugin();

        final List<FoliaRunnable> tasks = List.of();

        final ListenerHandler listenerHandler = new ListenerHandler(this);
        listenerHandler.handle(bootstrap.getLoader().getServer().getPluginManager());

        final List<Handler<?>> handlers = List.of(
                new CommandHandler(this),
                listenerHandler,
                new TaskHandler(this, tasks)
        );

        new WGRPLoaderHandlers(handlers).loadHandler(this);

        this.regionAdapter = new RegionAdapterManagerPaper();
        this.toolsAdapter = new ToolsAdapterManagerPaper();

        playerUtilWE = new UtilWEImpl(this);
        checkIntersection = playerUtilWE.setUpWorldGuardVersionSeven();
    }

    public List<UUID> getSpyLog() {
        return spyLog;
    }

    public RSApiImpl getRsApi() {
        return rsApi;
    }

    public CheckIntersection getCheckIntersection() {
        return checkIntersection;
    }

    public void messageToCommandSender(final @NotNull CommandSender commandSender, final String message) {
        final Audience audience = adventure.sender(commandSender);
        final var miniMessage = MiniMessage.miniMessage();
        final Component parsed = miniMessage.deserialize(message);
        audience.sendMessage(parsed);
    }

    @Override
    public DCBootstrap getBootstrap() {
        return this.bootstrap;
    }

    @Override
    public Platform.Type getType() {
        if (isClassPresent("io.papermc.paper.threadedregions.RegionizedServer")) {
            return Platform.Type.FOLIA;
        } else if (isClassPresent("com.destroystokyo.paper.ParticleBuilder")) {
            return Platform.Type.PAPER;
        } else if (isClassPresent("org.spigotmc.SpigotConfig")) {
            return Platform.Type.SPIGOT;
        } else {
            return Platform.Type.BUKKIT;
        }
    }

    public PlatformDetector getPlatformDetector() {
        return platformDetector;
    }

    public UpdateDownloaderGitHub getDownloader() {
        return downloader;
    }

    @Override
    public PermissionCheck getPermissionCheck() {
        return playerPermissions;
    }

    @Override
    public DonateCaseMetadata getMetaData() {
        return wgrpMetadata;
    }

    public Map<Class<? extends Listener>, Listener> getListenerHandlerMap() {
        return listenerHandlerMap;
    }

    public <T extends Listener> T getListener(@NotNull Class<T> listenerClass) {
        return listenerClass.cast(listenerHandlerMap.get(listenerClass));
    }

    public Map<String, CommandExecutor> getCommandMap() {
        return commandMap;
    }

    public Map<Class<? extends FoliaRunnable>, FoliaRunnable> getTaskMap() {
        return taskMap;
    }

    public MinecraftVersionChecker getVersionCheck() {
        return versionCheck;
    }

    public UpdateNotify getUpdateNotify() {
        return this.updateNotify;
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public ConfigProvider<DonateCasePaperPlugin, Config> getConfigProvider() {
        return configProvider;
    }

    public MessageProvider<DonateCasePaperPlugin, Messages> getMessageProvider() {
        return messageProvider;
    }
}
