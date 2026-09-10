package net.ritasister.dc.cases.manager;

import net.ritasister.dc.api.cases.service.CaseBroadcastService;
import net.ritasister.dc.api.cases.service.CaseRewardService;
import net.ritasister.dc.api.cases.service.CaseStateService;
import net.ritasister.dc.api.scheduler.AnimationTaskScheduler;
import net.ritasister.dc.cases.anim.StartAnimation;
import net.ritasister.dc.cases.item.ArmorStandFactory;
import net.ritasister.dc.cases.item.ItemRandomService;
import net.ritasister.dc.cases.obj.Case;
import net.ritasister.dc.util.schedulers.task.AnimationTaskFactory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AnimationManager {

    private final Map<Player, StartAnimation> activeAnimations = new HashMap<>();

    public boolean isPlayerInAnimation(Player player) {
        return activeAnimations.containsKey(player);
    }

    public void startAnimation(Player player, Location location, Case cases,
                               CaseStateService<Player, Location, Case> caseStateService,
                               CaseRewardService<Player, Case> rewardService,
                               CaseBroadcastService<Player, Case> broadcastService,
                               ArmorStandFactory armorStandFactory,
                               ItemRandomService randomService,
                               AnimationTaskFactory taskFactory,
                               AnimationTaskScheduler taskScheduler) {

        StartAnimation animation = new StartAnimation(
                caseStateService,
                rewardService,
                broadcastService,
                armorStandFactory,
                randomService,
                taskFactory,
                taskScheduler,
                player,
                location,
                cases,
                () -> activeAnimations.remove(player)
        );

        activeAnimations.put(player, animation);
    }
}
