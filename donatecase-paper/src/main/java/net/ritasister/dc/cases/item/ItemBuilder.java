package net.ritasister.dc.cases.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder implements net.ritasister.dc.api.cases.ItemBuilder<ItemStack, Material> {

    @Override
    public ItemStack build(org.bukkit.Material material, int amount, int data, String displayName, List<String> lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        if (displayName != null) {
            meta.setDisplayName(displayName.replace("&", "§"));
        }

        if (lore != null) {
            meta.setLore(lore.stream().map(l -> l.replace("&", "§")).toList());
        }

        item.setItemMeta(meta);
        return item;
    }
}
