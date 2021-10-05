package net.ritasister.dc;

import org.bukkit.plugin.RegisteredServiceProvider;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.command.CommandExecutor;

import java.util.Map;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.entity.ArmorStand;
import java.util.List;
import net.milkbowl.vault.permission.Permission;
import net.ritasister.commands.MainCommand;
import net.ritasister.listener.EventsListener;
import net.ritasister.tools.CustomConfig;
import net.ritasister.tools.Languages;
import net.ritasister.tools.Tools;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    public static Permission permission = null;
    public static boolean Tconfig = true;
    public static boolean LevelGroup = true;
    public static List<ArmorStand> listAR = new ArrayList<ArmorStand>();
    public static HashMap<Player, Location> openCase = new HashMap<Player, Location>();
    public static HashMap<Location, Case> ActiveCase = new HashMap<Location, Case>();
    public static HashMap<String, Integer> levelGroup = new HashMap<String, Integer>();
    public static Tools t;
    public static Main plugin;
    public static FileConfiguration lang;
    public static FileConfiguration config;
    public static CustomConfig Ckeys;
    public static CustomConfig CCase;
    public static MySQL mysql;
    public static String[] title = new String[2];
    
    public void onEnable() {
        Main.t = new Tools();
        this.saveDefaultConfig();
        Main.config = this.getConfig();
        Bukkit.getPluginManager().registerEvents((Listener)new EventsListener(), (Plugin)this);
        Main.plugin = this;
        if (!new File(this.getDataFolder(), "lang/ru_RU.yml").exists()) {
            this.saveResource("lang/ru_RU.yml", false);
        }
        Main.Ckeys = new CustomConfig("Keys");
        Main.CCase = new CustomConfig("Cases");
        Main.lang = new Languages(Main.config.getString("DonatCase.Languages")).getLang();
        Main.Tconfig = Main.config.getString("DonatCase.TypeSave").equalsIgnoreCase("config");
        Main.title[0] = Main.config.getString("DonatCase.Title.Title");
        Main.title[1] = Main.config.getString("DonatCase.Title.SubTitle");
        Main.LevelGroup = Main.config.getBoolean("DonatCase.LevelGroup");
        this.setupPermissions();
        if (!Main.Tconfig) {
            final String host = Main.config.getString("DonatCase.MySql.Host");
            final String user = Main.config.getString("DonatCase.MySql.User");
            final String password = Main.config.getString("DonatCase.MySql.Password");
            new BukkitRunnable() {
                public void run() {
                    Main.mysql = new MySQL(host, user, password);
                    if (!Main.mysql.hasTable("donate_cases")) {
                        Main.mysql.createTable();
                    }
                }
            }.runTaskTimer((Plugin)this, 0L, 12000L);
        }
        final ConfigurationSection cslg;
        if ((cslg = Main.config.getConfigurationSection("DonatCase.LevelsGroup")) != null) {
            for (final Map.Entry<?, ?> s : cslg.getValues(false).entrySet()) {
                Main.levelGroup.put(((String)s.getKey()).toLowerCase(), (Integer)s.getValue());
            }
        }
        final ConfigurationSection cases_;
        if ((cases_ = Main.config.getConfigurationSection("DonatCase.Cases")) != null) {
            for (final String cn : cases_.getValues(false).keySet()) {
                final String title = Main.config.getString("DonatCase.Cases." + cn + ".Title");
                final Case c = new Case(cn, title);
                for (final String i : Main.config.getConfigurationSection("DonatCase.Cases." + cn + ".Items").getValues(false).keySet()) {
                    final int chance = Main.config.getInt("DonatCase.Cases." + cn + ".Items." + i + ".Chance");
                    final String id = Main.config.getString("DonatCase.Cases." + cn + ".Items." + i + ".Item.ID");
                    final String displayname = Main.config.getString("DonatCase.Cases." + cn + ".Items." + i + ".Item.DisplayName");
                    final String group = Main.config.getString("DonatCase.Cases." + cn + ".Items." + i + ".Group");
                    c.setCmds(Main.config.getStringList("DonatCase.Cases." + cn + ".Commands"));
                    c.addItem(new Case.ItemCase(i, chance, id, group, displayname));
                }
            }
        }
        final FileConfiguration fckeys = Main.Ckeys.getConfig();
        final ConfigurationSection csc;
        if (Main.Tconfig && (csc = fckeys.getConfigurationSection("DonatCase.Cases")) != null) {
            for (final String s2 : csc.getValues(false).keySet()) {
                if (Case.hasCaseByName(s2)) {
                    final Case c2 = Case.getCaseByName(s2);
                    final ConfigurationSection csk = fckeys.getConfigurationSection("DonatCase.Cases." + s2);
                    if (csk == null) {
                        continue;
                    }
                    for (final Map.Entry<?, ?> k : csk.getValues(false).entrySet()) {
                        c2.setKeys((String)k.getKey(), (int)k.getValue());
                    }
                }
            }
        }
        final FileConfiguration fccase;
        final ConfigurationSection cslc;
        if ((cslc = (fccase = Main.CCase.getConfig()).getConfigurationSection("DonatCase.Cases")) != null) {
            for (final String s3 : cslc.getValues(false).keySet()) {
                if (Case.hasCaseByName(s3)) {
                    final Case c3 = Case.getCaseByName(s3);
                    for (final String lc : fccase.getStringList("DonatCase.Cases." + s3 + ".Case")) {
                        c3.getLocation().add(Main.t.getLoc(lc));
                    }
                }
            }
        }
        this.getCommand("donatcase").setExecutor(new MainCommand("donatcase"));
    }
    
    public void onDisable() {
        for (final ArmorStand as : Main.listAR) {
            if (as != null) {
                as.remove();
            }
        }
        if (Main.mysql != null) {
            Main.mysql.close();
        }
    }
    
    private boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> permissionProvider = (RegisteredServiceProvider<Permission>)this.getServer().getServicesManager().getRegistration((Class)Permission.class);
        if (permissionProvider != null) {
            Main.permission = (Permission)permissionProvider.getProvider();
        }
        return Main.permission != null;
    }
}
