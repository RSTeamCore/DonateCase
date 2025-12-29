package net.ritasister.dc.listener;

import dc.old.DonateCase;
import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.platform.Platform;
import net.ritasister.dc.api.scheduler.AnimationTaskScheduler;
import net.ritasister.dc.cases.obj.Case;
import net.ritasister.dc.util.schedulers.task.AnimationTaskFactory;
import net.ritasister.dc.util.schedulers.task.BukkitAnimationTaskScheduler;
import net.ritasister.dc.util.schedulers.task.FoliaAnimationTaskScheduler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.ritasister.dc.cases.gui.GuiDonatCase;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.entity.Firework;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EventsListener implements Listener {

    private final DonateCasePaperPlugin plugin;

    public EventsListener(DonateCasePaperPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(final @NotNull EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player && event.getDamager().hasMetadata("case")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void InventoryClick(final @NotNull InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        final Player player = (Player) e.getWhoClicked();
        final String playerName = player.getName();
        final String title = e.getView().getTitle();

        if (!plugin.getCaseRepository().hasCaseByTitle(title)) {
            return;
        }

        e.setCancelled(true);

        if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY &&
                e.getInventory().getType() == InventoryType.CHEST &&
                e.getRawSlot() == DonateCase.t.c(5, 3)) {

            final Case cases = plugin.getCaseRepository().getCaseByTitle(title);

            if (cases.getCaseKeyManager().getKeys(playerName) >= 1) {

                if (plugin.getAnimationManager().isPlayerInAnimation(player)) {
                    player.closeInventory();
                    DonateCase.t.msg(player, DonateCase.lang.getString("HaveOpenCase"));
                    return;
                }

                final Location block = DonateCase.openCase.get(player);

                cases.getCaseKeyManager().removeKeys(playerName, 1);
                player.closeInventory();

                AnimationTaskFactory taskFactory = new AnimationTaskFactory(plugin);
                AnimationTaskScheduler taskScheduler = plugin.getBootstrap().getType() == Platform.Type.PAPER
                        ? new BukkitAnimationTaskScheduler(plugin.getBootstrap().getLoader())
                        : new FoliaAnimationTaskScheduler(plugin.getBootstrap().getLoader());

                plugin.getAnimationManager().startAnimation(
                        player,
                        block,
                        cases,
                        plugin.getCaseStateService(),
                        plugin.getCaseRewardService(),
                        plugin.getCaseBroadcastService(),
                        plugin.getArmorStandFactory(),
                        plugin.getItemRandomService(),
                        taskFactory,
                        taskScheduler
                );

            } else {
                player.closeInventory();
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.4f);
                DonateCase.t.msg(player, DonateCase.lang.getString("NoKey"));
            }
        }
    }

    @EventHandler
    public void PlayerInteractEntity(final @NotNull PlayerInteractAtEntityEvent e) {
        final Entity entity = e.getRightClicked();
        if (entity.getType() == EntityType.ARMOR_STAND && DonateCase.listAR.contains(entity)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(final @NotNull PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Player player = e.getPlayer();
        final Location location = Objects.requireNonNull(e.getClickedBlock()).getLocation();

        if (!plugin.getCaseRepository().hasCaseByLocation(location)) {
            return;
        }

        e.setCancelled(true);

        if (plugin.getAnimationManager().isPlayerInAnimation(player)) {
            DonateCase.t.msg(player, DonateCase.lang.getString("HaveOpenCase"));
            return;
        }

        final Case caseAtLocation = plugin.getCaseRepository().getCaseByLocation(location);

        new GuiDonatCase(plugin, player, caseAtLocation);

        final AnimationTaskFactory taskFactory = new AnimationTaskFactory(plugin);

        final AnimationTaskScheduler taskScheduler;
        switch (plugin.getBootstrap().getType()) {
            case PAPER -> taskScheduler = new BukkitAnimationTaskScheduler(plugin.getBootstrap().getLoader());
            case FOLIA -> taskScheduler = new FoliaAnimationTaskScheduler(plugin.getBootstrap().getLoader());
            default -> throw new UnsupportedOperationException("Unsupported platform");
        }

        plugin.getAnimationManager().startAnimation(
                player,
                location,
                caseAtLocation,
                plugin.getCaseStateService(),
                plugin.getCaseRewardService(),
                plugin.getCaseBroadcastService(),
                plugin.getArmorStandFactory(),
                plugin.getItemRandomService(),
                taskFactory,
                taskScheduler
        );
    }

    @EventHandler
    public void InventoryClose(final @NotNull InventoryCloseEvent e) {
        final Player p = (Player)e.getPlayer();
        if (plugin.getCaseRepository().hasCaseByTitle(e.getView().getTitle())) {
            DonateCase.openCase.remove(p);
        }
    }

    @EventHandler
    public void BlockBreak(final @NotNull BlockBreakEvent e) {
        final Location loc = e.getBlock().getLocation();
        if (plugin.getCaseRepository().hasCaseByLocation(loc)) {
            e.setCancelled(true);
            DonateCase.t.msg(e.getPlayer(), DonateCase.lang.getString("DestoryDonatCase"));
        }
    }
}