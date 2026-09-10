package net.ritasister.dc;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.ritasister.dc.api.DonateCase;
import net.ritasister.dc.api.cases.service.CaseBroadcastService;
import net.ritasister.dc.api.cases.service.CaseRewardService;
import net.ritasister.dc.api.cases.service.CaseStateService;
import net.ritasister.dc.api.config.provider.ConfigProvider;
import net.ritasister.dc.api.config.provider.MessageProvider;
import net.ritasister.dc.api.config.version.ConfigVersionReader;
import net.ritasister.dc.api.config.version.VersionChecker;
import net.ritasister.dc.api.handler.Handler;
import net.ritasister.dc.api.metadata.DonateCaseMetadata;
import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.cases.group.GroupServiceImpl;
import net.ritasister.dc.cases.item.ArmorStandFactory;
import net.ritasister.dc.cases.item.ItemRandomService;
import net.ritasister.dc.cases.item.ItemUtilsImpl;
import net.ritasister.dc.cases.manager.AnimationManager;
import net.ritasister.dc.cases.manager.CaseKeyManager;
import net.ritasister.dc.cases.manager.CaseManager;
import net.ritasister.dc.cases.manager.LocationManager;
import net.ritasister.dc.cases.obj.Case;
import net.ritasister.dc.cases.obj.CaseRepository;
import net.ritasister.dc.cases.service.CaseBroadcastServiceImpl;
import net.ritasister.dc.cases.service.CaseRewardServiceImpl;
import net.ritasister.dc.cases.service.CaseStateServiceImpl;
import net.ritasister.dc.config.ConfigType;
import net.ritasister.dc.handler.CommandHandler;
import net.ritasister.dc.handler.ListenerHandler;
import net.ritasister.dc.handler.TaskHandler;
import net.ritasister.dc.plugin.AbstractDonateCasePlugin;
import net.ritasister.dc.plugin.checker.DCCompatibilityCheck;
import net.ritasister.dc.plugin.loader.DCLoaderHandlers;
import net.ritasister.dc.plugin.loader.LoadLuckPerms;
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
import net.ritasister.dc.util.tools.Tools;
import net.ritasister.dc.util.utility.platform.PlatformDetector;
import net.ritasister.dc.util.utility.updater.UpdateDownloaderGitHub;
import net.ritasister.dc.util.utility.updater.UpdateNotify;
import net.ritasister.dc.util.utility.version.MinecraftVersionChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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

import static net.ritasister.dc.util.utility.UtilityClass.isClassPresent;

public class DonateCasePaperPlugin extends AbstractDonateCasePlugin {

    private final DCBootstrap bootstrap;

    private PlatformDetector platformDetector;

    private BukkitAudiences adventure;
    private DonateCaseMetadata donateCaseMetadata;

    private LoadLuckPerms luckPerms;

    private Map<Class<? extends Listener>, Listener> listenerHandlerMap = new HashMap<>();
    private Map<String, CommandExecutor> commandMap;
    private Map<Class<? extends FoliaRunnable>, FoliaRunnable> taskMap = new HashMap<>();

    private UpdateDownloaderGitHub downloader;
    private UpdateNotify updateNotify;

    private MinecraftVersionChecker versionCheck;

    private ConfigLoader configLoader;
    private ConfigProvider<DonateCasePaperPlugin, Config> configProvider;
    private MessageProvider<DonateCasePaperPlugin, Messages> messageProvider;

    private CaseRepository caseRepository;
    private CaseManager caseManager;
    private GroupServiceImpl groupService;
    private Tools tools;
    private ItemUtilsImpl itemUtils;

    private Case cases;
    private CaseStateService<Player, Location, Case> caseStateService;
    private CaseRewardService<Player, Case> rewardService;
    private CaseBroadcastService<Player, Case> broadcastService;
    private ArmorStandFactory armorStandFactory;
    private ItemRandomService randomService;
    private AnimationManager animationManager;
    private LocationManager locationManager;
    private CaseKeyManager keyManager;

    public DonateCasePaperPlugin(DonateCasePaperBase donateCasePaperBase) {
        this.bootstrap = new DCBootstrap(donateCasePaperBase,this);
    }

    public void onEnable() {
        this.load();
        this.adventure = BukkitAudiences.create(this.bootstrap.getLoader());
        this.initializeFields();
        this.startAnimationServices();

        if (compatibleChecking()) {
            return;
        }

        this.initializeMetrics();
        this.loadAnotherClassAndMethods();
        this.logStartupTime();
    }

    private void startAnimationServices() {
        caseStateService = new CaseStateServiceImpl();
        rewardService = new CaseRewardServiceImpl();
        broadcastService = new CaseBroadcastServiceImpl();
        armorStandFactory = new ArmorStandFactory();
        randomService = new ItemRandomService();
    }

    private boolean compatibleChecking() {
        final MinecraftVersionChecker versionChecker = this.versionCheck;
        platformDetector = new PlatformDetector(this.getLogger());
        final PluginDisabler pluginDisabler = new PluginDisabler(this);
        final DCCompatibilityCheck compatibilityCheck = new DCCompatibilityCheck(versionChecker, platformDetector, pluginDisabler);

        return !compatibilityCheck.performCompatibilityChecks();
    }

    private void initializeFields() {
        Case myCase = new Case("gold_case", "§6Золотой кейс", locationManager, keyManager);
        myCase.setCommands(List.of("say {player} выиграл {item}!"));
        myCase.addLocation(new Location(Bukkit.getWorld("world"), 100, 65, 100));

        this.keyManager = myCase.getCaseKeyManager();
        this.locationManager = myCase.getLocationManager();
        this.animationManager = new AnimationManager();
        this.tools = new Tools();
        this.itemUtils = new ItemUtilsImpl();
        this.caseRepository = new CaseRepository(this, new ArrayList<>());
        this.caseManager = new CaseManager();
        this.groupService = new GroupServiceImpl(this);

        this.versionCheck = new MinecraftVersionChecker(this.bootstrap);

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
        final int pluginId = 12963;
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
        luckPerms = new LoadLuckPerms(this);
        luckPerms.hookLuckPerms();

        final ListenerHandler listenerHandler = new ListenerHandler(this);
        listenerHandler.handle(bootstrap.getLoader().getServer().getPluginManager());

        final List<Handler<?>> handlers = List.of(
                new CommandHandler(this),
                listenerHandler,
                new TaskHandler(this)
        );

        new DCLoaderHandlers(handlers).loadHandler(this);
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

    public LoadLuckPerms getLuckPerms() {
        return luckPerms;
    }

    @Override
    public DonateCaseMetadata getMetaData() {
        return donateCaseMetadata;
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

    public GroupServiceImpl getGroupService() {
        return groupService;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public CaseRepository getCaseRepository() {
        return caseRepository;
    }

    public Tools getTools() {
        return tools;
    }

    public ItemUtilsImpl getItemUtils() {
        return itemUtils;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public CaseStateService<Player, Location, Case> getCaseStateService() {
        return caseStateService;
    }

    public CaseRewardService<Player, Case> getCaseRewardService() {
        return rewardService;
    }

    public CaseBroadcastService<Player, Case> getCaseBroadcastService() {
        return broadcastService;
    }

    public ArmorStandFactory getArmorStandFactory() {
        return armorStandFactory;
    }

    public ItemRandomService getItemRandomService() {
        return randomService;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public CaseKeyManager getKeyManager() {
        return keyManager;
    }
}
