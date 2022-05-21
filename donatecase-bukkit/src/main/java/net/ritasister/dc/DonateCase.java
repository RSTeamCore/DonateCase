package net.ritasister.dc;

import net.milkbowl.vault.permission.Permission;
import net.ritasister.dc.commands.MainCommand;
import net.ritasister.dc.listener.EventsListener;
import net.ritasister.dc.tools.CustomConfig;
import net.ritasister.dc.tools.Languages;
import net.ritasister.dc.tools.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DonateCase extends JavaPlugin {

	public static DonateCase instance;
	public static Permission permission = null;
	public static boolean Tconfig = true;
	public static boolean LevelGroup = true;
	public static List<ArmorStand> listAR = new ArrayList<>();
	public static HashMap<Player, Location> openCase = new HashMap<>();
	public static HashMap<Location, Case> ActiveCase = new HashMap<>();
	public static HashMap<String, Integer> levelGroup = new HashMap<>();
	public static Tools t;
	public static FileConfiguration lang;
	public static FileConfiguration config;
	public static CustomConfig Ckeys;
	public static CustomConfig CCase;
	//public static Storage mysql;
	public static String[] title = new String[2];
	private final PluginManager pluginManager = getServer().getPluginManager();
	private final String pluginVersion = getDescription().getVersion();

	//DataBase
	public StorageDataSource dbLogsSource;
	public ConcurrentHashMap<String, StorageDataBase> dbLogs = new ConcurrentHashMap<>();

	public DonateCase() {
		DonateCase.instance = this;
	}

	/**
	 * @param nickName get player name from storage.
	 *
	 * @return getDataStorage.
	 */
	@NotNull
	public StorageDataBase getDataStorage(String nickName) {
		return this.dbLogs.get(nickName);
	}

	/**
	 *
	 * @return getDataSource.
	 */
	@NotNull
	public StorageDataSource getDataSource() {
		return this.dbLogsSource;
	}

	public void onEnable() {
		this.checkVersion();
		this.checkUpdate();
		this.loadMetrics();
		DonateCase.t = new Tools();
		this.saveDefaultConfig();
		DonateCase.config = this.getConfig();
		Bukkit.getPluginManager().registerEvents(new EventsListener(), this);
		if (!new File(this.getDataFolder(), "lang/ru_RU.yml").exists()) {
			this.saveResource("lang/ru_RU.yml", false);
		}
		DonateCase.Ckeys = new CustomConfig("Keys");
		DonateCase.CCase = new CustomConfig("Cases");
		DonateCase.lang = new Languages(DonateCase.config.getString("DonatCase.Languages")).getLang();
		DonateCase.Tconfig = Objects.requireNonNull(DonateCase.config.getString("DonatCase.TypeSave")).equalsIgnoreCase("config");
		DonateCase.title[0] = DonateCase.config.getString("DonatCase.Title.Title");
		DonateCase.title[1] = DonateCase.config.getString("DonatCase.Title.SubTitle");
		DonateCase.LevelGroup = DonateCase.config.getBoolean("DonatCase.LevelGroup");
		this.setupPermissions();
		this.loadDataBase();
		final ConfigurationSection cslg;
		if ((cslg = DonateCase.config.getConfigurationSection("DonatCase.LevelsGroup")) != null) {
			for (final Map.Entry<?, ?> s : cslg.getValues(false).entrySet()) {
				DonateCase.levelGroup.put(((String)s.getKey()).toLowerCase(), (Integer)s.getValue());
			}
		}
		final ConfigurationSection cases_;
		if ((cases_ = DonateCase.config.getConfigurationSection("DonatCase.Cases")) != null) {
			for (final String cn : cases_.getValues(false).keySet()) {
				final String title = DonateCase.config.getString("DonatCase.Cases." + cn + ".Title");
				final Case c = new Case(this, cn, title);
				for (final String i : DonateCase.config.getConfigurationSection("DonatCase.Cases." + cn + ".Items").getValues(false).keySet())
				{
					final int chance = DonateCase.config.getInt("DonatCase.Cases." + cn + ".Items." + i + ".Chance");
					final String id = DonateCase.config.getString("DonatCase.Cases." + cn + ".Items." + i + ".Item.ID");
					final String displayname = DonateCase.config.getString("DonatCase.Cases." + cn + ".Items." + i + ".Item.DisplayName");
					final String group = DonateCase.config.getString("DonatCase.Cases." + cn + ".Items." + i + ".Group");
					c.setCmds(DonateCase.config.getStringList("DonatCase.Cases." + cn + ".Commands"));
					assert group != null;
					assert id != null;
					c.addItem(new Case.ItemCase(i, chance, id, group, displayname));
				}
			}
		}
		final FileConfiguration fckeys = DonateCase.Ckeys.getConfig();
		final ConfigurationSection csc;
		if (DonateCase.Tconfig && (csc = fckeys.getConfigurationSection("DonatCase.Cases")) != null) {
			for (final String s2 : csc.getValues(false).keySet()) {
				if (Case.hasCaseByName(s2)) {
					final Case c2 = Case.getCaseByName(s2);
					final ConfigurationSection csk = fckeys.getConfigurationSection("DonatCase.Cases." + s2);
					if (csk == null) {
						continue;
					}
					for (final Map.Entry<?, ?> k : csk.getValues(false).entrySet()) {
						assert c2 != null;
						c2.setKeys((String)k.getKey(), (int)k.getValue());
					}
				}
			}
		}
		final FileConfiguration fccase;
		final ConfigurationSection cslc;
		if ((cslc = (fccase = DonateCase.CCase.getConfig()).getConfigurationSection("DonatCase.Cases")) != null) {
			for (final String s3 : cslc.getValues(false).keySet()) {
				if (Case.hasCaseByName(s3)) {
					final Case c3 = Case.getCaseByName(s3);
					for (final String lc : fccase.getStringList("DonatCase.Cases." + s3 + ".Case")) {
						assert c3 != null;
						c3.getLocation().add(DonateCase.t.getLoc(lc));
					}
				}
			}
		}
		Objects.requireNonNull(this.getCommand("donatcase")).setExecutor(new MainCommand("donatcase"));
	}

	public void loadDataBase() {
		final long duration_time_start = System.currentTimeMillis();
		this.dbLogsSource = new Storage(this);
		this.dbLogs.clear();
		if (dbLogsSource.load()) {
			this.getLogger().info("[DataBase] The player base is loaded.");
			this.postEnable();
			this.getLogger().info("[DataBase] Startup duration: {TIME} мс.".replace("{TIME}", String.valueOf(System.currentTimeMillis() - duration_time_start)));
		}
	}

	public void postEnable() {
		this.getServer().getScheduler().cancelTasks(this);
		this.dbLogsSource.loadAsync();
		this.getLogger().info("[DataBase] The base is loaded asynchronously.");
	}

	public void onDisable() {
		for (final ArmorStand as : DonateCase.listAR) {
			if (as != null) {
				as.remove();
			}
		}
		if (dbLogsSource != null) {
			dbLogsSource.close();
		}
	}
	private void checkVersion() {
		final String javaVersion = System.getProperty("java.version");
		final int dotIndex = javaVersion.indexOf('.');
		final int endIndex = dotIndex == -1 ? javaVersion.length() : dotIndex;
		final String version = javaVersion.substring(0, endIndex);
		final int javaVersionNum;
		try{
			javaVersionNum = Integer.parseInt(version);
		}catch(final NumberFormatException e){
			Logger.getLogger(Level.WARNING + "Failed to determine Java version; Could not parse {}".replace("{}", version) + e);
			Logger.getLogger(Level.WARNING + javaVersion);
			return;
		}
		String serverVersion;
		try{
			serverVersion = DonateCase.instance.getServer().getClass().getPackage().getName().split("\\.")[3];
		}catch(ArrayIndexOutOfBoundsException whatVersionAreYouUsingException){
			return;
		}
		Logger.getLogger(Level.INFO + "&6You are running is &ejava &6version: &e<javaVersion>".replace("<javaVersion>", String.valueOf(javaVersionNum)));
		Logger.getLogger(Level.INFO + "&6Your &eserver &6is running version: &e<serverVersion>".replace("<serverVersion>", String.valueOf(serverVersion)));
	}
	private void checkUpdate() {
		new UpdateChecker(this, 102109).getVersion(version -> {
			if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
				Logger.getLogger(Level.INFO + "&6==============================================");
				Logger.getLogger(Level.INFO + "Current version: &b<pl_ver>".replace("<pl_ver>", pluginVersion));
				Logger.getLogger(Level.INFO + "This is latest version plugin.");
				Logger.getLogger(Level.INFO + "&6==============================================");
			}else{
				Logger.getLogger(Level.INFO + "&6==============================================");
				Logger.getLogger(Level.INFO + "&eThere is a new version update available.");
				Logger.getLogger(Level.INFO + "&cCurrent version: &4<pl_ver>".replace("<pl_ver>", pluginVersion));
				Logger.getLogger(Level.INFO + "&3New version: &b<new_pl_ver>".replace("<new_pl_ver>", version));
				Logger.getLogger(Level.INFO + "&ePlease download new version here:");
				Logger.getLogger(Level.INFO + "&ehttps://www.spigotmc.org/resources/this-1-13-1-18.102109/");
				Logger.getLogger(Level.INFO + "&6==============================================");
			}
		});
	}
	private void loadMetrics() {
		int pluginId = 12963;
		new Metrics(this, pluginId);
	}

	private boolean setupPermissions() {
		final RegisteredServiceProvider<Permission> permissionProvider = this.getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			DonateCase.permission = permissionProvider.getProvider();
		}
		return DonateCase.permission != null;
	}
}