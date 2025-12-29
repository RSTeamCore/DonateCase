package net.ritasister.dc.cases.item;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ArmorStandFactory {

    public ArmorStand spawnArmorStand(@NonNull Player player, @NonNull Location location) {
        final ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

        as.setGravity(false);
        as.setSmall(true);
        as.setVisible(false);
        as.setCustomNameVisible(true);

        // Можно, например, привязать ArmorStand к игроку через metadata
        as.setMetadata("owner", new FixedMetadataValue(player.getServer().getPluginManager().getPlugin("YourPluginName"), player.getUniqueId()));

        return as;
    }
}
