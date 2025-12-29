package net.ritasister.dc.cases.item;

import net.ritasister.dc.api.cases.items.ItemUtils;
import net.ritasister.dc.util.tools.Tools;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemUtilsImpl implements ItemUtils<ItemCase, Material, ItemStack> {

    private final Tools tools;

    public ItemUtilsImpl(Tools tools) {
        this.tools = tools;
    }

    public ItemStack createItem(final Material material) {
        return this.createItem(material, 0, 1, null, null);
    }

    public ItemStack createItem(final Material material, final List<String> lore) {
        return this.createItem(material, 0, 1, "", lore);
    }

    public ItemStack createItem(final Material material, final int amount) {
        return this.createItem(material, 0, amount, "", null);
    }

    public ItemStack createItem(final Material material, final int amount, final int data) {
        return this.createItem(material, data, amount, "", null);
    }

    public ItemStack createItem(final Material material, final String dn, final List<String> lore) {
        return this.createItem(material, 0, 1, dn, lore);
    }

    public ItemStack createItem(final Material ma, final String dn) {
        return this.createItem(ma, 0, 1, dn, null);
    }

    @Override
    public ItemStack createItem(Material material, int amount, int data, String dn, final List<String> lore) {
        final ItemStack item = new ItemStack(material, amount);
        final ItemMeta meta = item.getItemMeta();

        if (dn != null) {
            meta.setDisplayName(dn);
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ItemCase getRandomGroup(@NonNull List<ItemCase> groups) {
        final int total = groups.stream().mapToInt(ItemCase::chance).sum();

        if (total <= 0) {
            return null;
        }

        final int rand = ThreadLocalRandom.current().nextInt(total);
        int current = 0;

        for (ItemCase ic : groups) {
            current += ic.chance();
            if (rand < current) return ic;
        }
        return null;
    }
}
