package net.ritasister.dc.cases.service;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.cases.service.FireworkService;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NonNull;

import java.util.Random;

public class FireworkServiceImpl implements FireworkService<Location> {

    private final DonateCasePaperPlugin plugin;

    public FireworkServiceImpl(DonateCasePaperPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void launchFirework(@NonNull Location location) {
        final Random random = new Random();
        final Firework fireworkq = (Firework) location.getWorld().spawnEntity(location.subtract(new Vector(0.0, 0.5, 0.0)), EntityType.FIREWORK_ROCKET);
        final FireworkMeta meta = fireworkq.getFireworkMeta();
        final Color[] colors = {
                Color.RED, Color.AQUA,
                Color.GREEN, Color.ORANGE,
                Color.LIME, Color.BLUE,
                Color.MAROON, Color.WHITE
        };

        meta.addEffect(FireworkEffect.builder()
                .flicker(false)
                .with(FireworkEffect.Type.BALL)
                .trail(false)
                .withColor(new Color[] {
                        colors[random.nextInt(colors.length)],
                        colors[random.nextInt(colors.length)],
                        colors[random.nextInt(colors.length)]
                })
                .build());
        fireworkq.setFireworkMeta(meta);
        fireworkq.setMetadata("case", new FixedMetadataValue(plugin.getBootstrap().getLoader(), "case"));
        fireworkq.detonate();
    }
}
