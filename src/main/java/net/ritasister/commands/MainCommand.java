package net.ritasister.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.util.Set;

import org.bukkit.entity.Player;

import net.ritasister.commands.executor.DCCommand;
import net.ritasister.dc.Case;
import net.ritasister.dc.Main;

import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public class MainCommand extends DCCommand
{
	//private final List<String> subCommand = Arrays.asList("help", "create", "delete", "givekey", "setkey");
	
    public MainCommand(String name) 
    {
		super("donatcase");
	}
    
     public void run(CommandSender $s, Command $cmd, String label, String[] $args) throws Exception 
     {
        if ($s.hasPermission("DonatCase.Admin")) {
            if ($args.length >= 1) {
                if (!$args[0].equalsIgnoreCase("create") && (!$args[0].equalsIgnoreCase("delete") || !($s instanceof Player))) {
                    if ($args[0].equalsIgnoreCase("givekey")) {
                        if ($args.length >= 4) {
                            try {
                                final String player = $args[1];
                                final String nCase = $args[2];
                                final int keys = Integer.parseInt($args[3]);
                                if (Case.hasCaseByName(nCase)) 
                                {
                                    final Case ca = Case.getCaseByName(nCase);
                                    ca.addKeys(player, keys);
                                    Main.t.msg($s, Main.t.rt(Main.lang.getString("GiveKeys"), "%player:" + player, "%key:" + keys, "%case:" + nCase, "%ending:" + Main.t.getEnding(keys, "\u044f", "\u0435\u0439")));
                                }else{
                                    Main.t.msg($s, Main.t.rt(Main.lang.getString("CaseNotExist"), "%name:" + nCase));
                                }
                            }catch(Exception var11){
                                this.help($s, label);
                            }
                        }else{
                            this.help($s, label);
                        }
                    }else if ($args[0].equalsIgnoreCase("setkey")) {
                        if ($args.length >= 4) {
                            try {
                                final String player = $args[1];
                                final String nCase = $args[2];
                                final int keys = Integer.parseInt($args[3]);
                                if (Case.hasCaseByName(nCase)) {
                                    final Case ca = Case.getCaseByName(nCase);
                                    ca.setKeys(player, keys);
                                    Main.t.msg($s, Main.t.rt(Main.lang.getString("SetKeys"), "%player:" + player, "%key:" + keys, "%case:" + nCase, "%ending:" + Main.t.getEnding(keys, "\u044f", "\u0435\u0439")));
                                }
                                else {
                                    Main.t.msg($s, Main.t.rt(Main.lang.getString("CaseNotExist"), "%name:" + nCase));
                                }
                            }
                            catch (Exception var12) {
                                this.help($s, label);
                            }
                        }
                        else {
                            this.help($s, label);
                        }
                    }
                    else if ($args[0].equalsIgnoreCase("help")) {
                        for (final String line : Main.lang.getStringList("Help")) {
                            Main.t.msg_($s, Main.t.rt(line, "%cmd:" + label));
                        }
                    }
                    else {
                        this.help($s, label);
                    }
                }
                else {
                    final boolean cr = $args[0].substring(0, 1).equals("c");
                    final Player p = (Player)$s;
                    final Location l = p.getTargetBlock((Set)null, 5).getLocation();
                    if (cr) {
                        if ($args.length >= 2) {
                            final String name = $args[1];
                            if (Case.hasCaseByName(name)) {
                                final Case ca2 = Case.getCaseByName(name);
                                if (Case.hasCaseByLocation(l)) {
                                    Main.t.msg($s, Main.lang.getString("HasDonatCase"));
                                }
                                else {
                                    ca2.addLocation(l);
                                    Main.t.msg($s, Main.lang.getString("AddDonatCase"));
                                }
                            }
                            else {
                                Main.t.msg($s, Main.t.rt(Main.lang.getString("CaseNotExist"), "%name:" + name));
                            }
                        }
                        else {
                            this.help($s, label);
                        }
                    }
                    else if (Case.hasCaseByLocation(l)) {
                        final Case cs = Case.getCaseByLocation(l);
                        cs.removeLocation(l);
                        Main.t.msg($s, Main.lang.getString("RemoveDonatCase"));
                    }
                    else {
                        Main.t.msg($s, Main.lang.getString("BlockDontDonatCase"));
                    }
                }
            }
            else {
                this.help($s, label);
            }
        }
        else {
            Main.t.msg($s, Main.lang.getString("NoPermission"));
        }
        return;
    }
    
	@Override
	public List<String> onTabComplete(CommandSender $s, Player $p, Command $cmd, String $label, String[] $args) 
	{
        if ($args.length == 1 && $s.hasPermission("*")) 
        {
            ArrayList<String> list = new ArrayList<String>();
            final ArrayList<String> value = new ArrayList<String>();
            value.add("help");
            value.add("create");
            value.add("delete");
            value.add("givekey");
            value.add("setkey");
            if ($args[0].equals("")) {
                list = value;
            }
            else {
                for (final String tmp : value) {
                    if (tmp.startsWith($args[0].toLowerCase())) {
                        list.add(tmp);
                    }
                }
            }
            Collections.sort(list);
            System.out.println(list);
            return list;
        }
        if ($args.length == 2 && $s.hasPermission("*") && $args[0].equalsIgnoreCase("create")) {
            ArrayList<String> list = new ArrayList<String>();
            final ArrayList<String> value = new ArrayList<String>();
            final ConfigurationSection section = Main.config.getConfigurationSection("DonatCase.Cases");
            for (final String tmp2 : section.getKeys(false)) {
                value.add(tmp2.toLowerCase());
            }
            if ($args[1].equals("")) {
                list = value;
            }
            else {
                for (final String tmp2 : value) {
                    if (tmp2.startsWith($args[1].toLowerCase())) {
                        list.add(tmp2.toLowerCase());
                    }
                }
            }
            Collections.sort(list);
            return list;
        }
        if ($args.length == 3 && $s.hasPermission("*") && ($args[0].equalsIgnoreCase("givekey") || $args[0].equalsIgnoreCase("setkey"))) {
            ArrayList<String> list = new ArrayList<String>();
            final ArrayList<String> value = new ArrayList<String>();
            final ConfigurationSection section = Main.config.getConfigurationSection("DonatCase.Cases");
            for (final String tmp2 : section.getKeys(false)) {
                value.add(tmp2.toLowerCase());
            }
            if ($args[2].equals("")) {
                list = value;
            }else{
                for (final String tmp2 : value) {
                    if (tmp2.startsWith($args[2].toLowerCase())) {
                        list.add(tmp2.toLowerCase());
                    }
                }
            }
            Collections.sort(list);
            return list;
        }
        return new ArrayList<>();
    }
    
    
    public void help(final CommandSender s, final String cmd) {
        Bukkit.getServer().dispatchCommand(s, Main.t.rt("%cmd help", "%cmd:" + cmd));
    }
}
