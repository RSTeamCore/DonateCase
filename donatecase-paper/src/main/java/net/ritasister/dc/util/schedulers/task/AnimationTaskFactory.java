package net.ritasister.dc.util.schedulers.task;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.scheduler.AnimationTaskScheduler;

public class AnimationTaskFactory {

    private final DonateCasePaperPlugin plugin;

    public AnimationTaskFactory(DonateCasePaperPlugin plugin) {
        this.plugin = plugin;
    }

    public AnimationTask createTask(Runnable taskLogic) {
        AnimationTaskScheduler scheduler;

        switch (plugin.getBootstrap().getType()) {
            case PAPER -> scheduler = new BukkitAnimationTaskScheduler(plugin.getBootstrap().getLoader());
            case FOLIA -> scheduler = new FoliaAnimationTaskScheduler(plugin.getBootstrap().getLoader());
            default -> throw new UnsupportedOperationException("Unsupported platform");
        }

        return new AnimationTask(scheduler, taskLogic);
    }
}
