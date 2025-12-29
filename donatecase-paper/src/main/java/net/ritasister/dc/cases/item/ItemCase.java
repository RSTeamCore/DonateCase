package net.ritasister.dc.cases.item;

import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import net.ritasister.dc.api.cases.CaseItem;
import org.bukkit.inventory.meta.ItemMeta;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record ItemCase(
        String name,
        String group,
        String displayName,
        String materialId,
        int chance)
        implements CaseItem<ItemStack> {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getMaterialId() {
        return materialId;
    }

    @Override
    public int getChance() {
        return chance;
    }

    @Override
    public @NonNull ItemStack toItemStack() {
        final Material material = Material.matchMaterial(materialId);
        if (material == null) {
            return new ItemStack(Material.BARRIER);
        }

        final ItemStack item = new ItemStack(material);
        final ItemMeta meta = item.getItemMeta();

        if (displayName != null) {
            meta.setDisplayName(displayName.replace("&", "§"));
        }

        item.setItemMeta(meta);
        return item;
    }
}
