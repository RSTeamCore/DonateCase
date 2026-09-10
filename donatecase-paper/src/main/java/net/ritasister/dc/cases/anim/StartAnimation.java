package net.ritasister.dc.cases.anim;

import net.ritasister.dc.api.cases.CaseItem;
import net.ritasister.dc.api.cases.Cases;
import net.ritasister.dc.api.cases.service.CaseBroadcastService;
import net.ritasister.dc.api.cases.service.CaseRewardService;
import net.ritasister.dc.api.cases.service.CaseStateService;
import net.ritasister.dc.api.scheduler.AnimationTaskScheduler;
import net.ritasister.dc.cases.item.ArmorStandFactory;
import net.ritasister.dc.cases.item.ItemCase;
import net.ritasister.dc.cases.item.ItemRandomService;
import net.ritasister.dc.cases.obj.Case;
import net.ritasister.dc.util.schedulers.task.AnimationTask;
import net.ritasister.dc.util.schedulers.task.AnimationTaskFactory;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

public class StartAnimation {

    private final CaseStateService<Player, Location, Case> caseStateService;
    private final CaseRewardService<Player, Case> rewardService;
    private final CaseBroadcastService<Player, Case> broadcastService;
    private final ArmorStandFactory armorStandFactory;
    private final ItemRandomService randomService;
    private final AnimationTaskFactory taskFactory;
    private final AnimationTaskScheduler taskScheduler;

    private final Player player;
    private final Location location;
    private final Case cases;
    private final Runnable onFinish;

    public StartAnimation(
            CaseStateService<Player, Location, Case> caseStateService,
            CaseRewardService<Player, Case> rewardService,
            CaseBroadcastService<Player, Case> broadcastService,
            ArmorStandFactory armorStandFactory,
            ItemRandomService randomService,
            AnimationTaskFactory taskFactory, AnimationTaskScheduler taskScheduler,
            Player player,
            @NonNull Location location,
            Case cases,
            Runnable onFinish
    ) {
        this.caseStateService = caseStateService;
        this.rewardService = rewardService;
        this.broadcastService = broadcastService;
        this.armorStandFactory = armorStandFactory;
        this.randomService = randomService;

        this.taskFactory = taskFactory;
        this.taskScheduler = taskScheduler;
        this.player = player;
        this.location = location.clone().add(0.5, -0.1, 0.5);
        this.location.setYaw(-70f);
        this.cases = cases;
        this.onFinish = onFinish;

        startAnimation();
    }

    private void startAnimation() {
        caseStateService.markOpened(player, location, cases);

        ArmorStand armorStand = armorStandFactory.spawnArmorStand(player, location);

        AnimationTask task = taskFactory.createTask(() -> runAnimationTick(armorStand));

        task.start(0, 2);
    }

    private int tick = 0;
    private Location currentLoc;

    private void runAnimationTick(ArmorStand armorStand) {
        if (tick == 0) currentLoc = armorStand.getLocation().clone();

        if (tick <= 15) {
            CaseItem<?> randomItem = randomService.getRandomItem(cases.getItems());
            updateArmorStand(armorStand, (ItemCase) randomItem);
            spawnParticles(armorStand);
        }

        if (tick == 16) {
            CaseItem<?> winItem = randomService.getRandomItem(cases.getItems());
            giveWinItem(armorStand, (ItemCase) winItem);
        }

        if (tick >= 40) {
            cleanup(armorStand);
            taskScheduler.cancel();
        }

        rotateArmorStand(armorStand);
        currentLoc.add(0, 0.14, 0);
        tick++;
    }


    private void updateArmorStand(@NonNull ArmorStand armorStand, @NonNull ItemCase itemCase) {
        ItemStack itemStack = itemCase.toItemStack();
        armorStand.setHelmet(itemStack);
        armorStand.setCustomName(itemStack.getItemMeta().getDisplayName());
    }

    private void spawnParticles(@NonNull ArmorStand armorStand) {
        armorStand.getWorld().spawnParticle(Particle.FIREWORK, armorStand.getLocation().clone().add(0, 0.4, 0), 1);
    }

    private void giveWinItem(@NonNull ArmorStand armorStand, @NonNull ItemCase winItem) {
        ItemStack itemStack = winItem.toItemStack();
        armorStand.setHelmet(itemStack);
        armorStand.setCustomName(itemStack.getItemMeta().getDisplayName());

        rewardService.applyReward(player, cases, winItem);
        broadcastService.broadcastWin(player, cases, winItem);
    }

    private void rotateArmorStand(@NonNull ArmorStand armorStand) {
        Location loc = armorStand.getLocation().clone();
        loc.setYaw(loc.getYaw() + 20f);
        armorStand.teleport(loc);
    }

    private void cleanup(@NonNull ArmorStand armorStand) {
        armorStand.remove();
        caseStateService.clear(player, location);

        if (onFinish != null) {
            onFinish.run();
        }
    }

}
