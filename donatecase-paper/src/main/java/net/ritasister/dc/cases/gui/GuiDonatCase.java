package net.ritasister.dc.cases.gui;

import dc.old.DonateCase;
import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.cases.obj.Case;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class GuiDonatCase {

    public GuiDonatCase(final @NonNull DonateCasePaperPlugin plugin, final Player p, final @NonNull Case cases) {
        final Inventory inv = Bukkit.createInventory(null, 45, cases.getTitle());
        final ItemStack f = plugin.getItemUtils().createItem(Material.WHITE_STAINED_GLASS_PANE, 1, 1);
        for (int a = 0; a < 2; ++a) {
            for (int i = 1; i <= 9; ++i) {
                inv.setItem(plugin.getTools().c(i, (a == 0) ? 1 : 5), f);
            }
            for (int var7 = 2; var7 <= 4; ++var7) {
                inv.setItem(plugin.getTools().c((a == 0) ? 1 : 9, var7), f);
            }
        }
        inv.setItem(
                plugin.getTools().c(5, 3),
                plugin.getItemUtils().createItem(Material.TRIPWIRE_HOOK,
                        plugin.getTools().rc(DonateCase.lang.getString("Key.DisplayName"))
        				.replace("<key>", String.valueOf(plugin.getKeyManager().getKeys(p.getName())))),

                DonateCase.t.rt(
        				DonateCase.lang.getStringList("Key.Lore"), "%case:" 
        				+ cases.getName(), "%key:"
        				+ cases.getCaseKeyManager().getKeys(p.getName())));
        p.openInventory(inv);
    }
}
