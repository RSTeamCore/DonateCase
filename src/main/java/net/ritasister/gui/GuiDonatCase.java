package net.ritasister.gui;

import org.bukkit.inventory.ItemStack;

import net.ritasister.dc.Case;
import net.ritasister.dc.Main;

import org.bukkit.inventory.Inventory;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuiDonatCase
{
    public GuiDonatCase(final Player p, final Case c) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 45, c.getTitle());
        final ItemStack f = Main.t.createItem(Material.WHITE_STAINED_GLASS_PANE, 1, 1, " ");
        for (int a = 0; a < 2; ++a) {
            for (int i = 1; i <= 9; ++i) {
                inv.setItem(Main.t.c(i, (a == 0) ? 1 : 5), f);
            }
            for (int var7 = 2; var7 <= 4; ++var7) {
                inv.setItem(Main.t.c((a == 0) ? 1 : 9, var7), f);
            }
        }
        inv.setItem(
        		Main.t.c(5, 3), 
        		Main.t.createItem(Material.TRIPWIRE_HOOK, 
        		Main.t.rc(
        				Main.lang.getString("Key.DisplayName")
        				.replace("<key>", String.valueOf(c.getKeys(p.getName())))), 
        		Main.t.rt(
        				Main.lang.getStringList("Key.Lore"), "%case:" 
        				+ c.getName(), "%key:" 
        				+ c.getKeys(p.getName()))));
        p.openInventory(inv);
    }
}