package dc.old;

import net.ritasister.dc.MainCommand;
import net.ritasister.dc.cases.obj.Case;
import net.ritasister.dc.util.tools.Tools;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class DonateCase extends JavaPlugin {

	public static DonateCase instance;
	public static boolean Tconfig = true;
	public static boolean LevelGroup = true;
	public static List<ArmorStand> listAR = new ArrayList<>();
	public static HashMap<Player, Location> openCase = new HashMap<>();
	public static HashMap<Location, Case> ActiveCase = new HashMap<>();
	public static HashMap<String, Integer> levelGroup = new HashMap<>();
	public static Tools t;
	public static FileConfiguration lang;
	public static FileConfiguration config;
	public static String[] title = new String[2];

	public void onEnable() {
		this.saveDefaultConfig();
		DonateCase.config = this.getConfig();
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

}