package net.ritasister.dc.gui;

import net.ritasister.dc.Case;
import net.ritasister.dc.DonateCase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class GuiDonatCase {
    public GuiDonatCase(final Player p, final Case c) {
        final Inventory inv = Bukkit.createInventory(null, 45, c.getTitle());
        final ItemStack f = DonateCase.t.createItem(Material.WHITE_STAINED_GLASS_PANE, 1, 1, " ");
        for (int a = 0; a < 2; ++a) {
            for (int i = 1; i <= 9; ++i) {
                inv.setItem(DonateCase.t.c(i, (a == 0) ? 1 : 5), f);
            }
            for (int var7 = 2; var7 <= 4; ++var7) {
                inv.setItem(DonateCase.t.c((a == 0) ? 1 : 9, var7), f);
            }
        }
        inv.setItem(
        		DonateCase.t.c(5, 3), 
        		DonateCase.t.createItem(Material.TRIPWIRE_HOOK, 
        		DonateCase.t.rc(
        				Objects.requireNonNull(DonateCase.lang.getString("Key.DisplayName"))
        				.replace("<key>", String.valueOf(c.getKeys(p.getName())))), 
        		DonateCase.t.rt(
        				DonateCase.lang.getStringList("Key.Lore"), "%case:" 
        				+ c.getName(), "%key:" 
        				+ c.getKeys(p.getName()))));
        p.openInventory(inv);
    }
}