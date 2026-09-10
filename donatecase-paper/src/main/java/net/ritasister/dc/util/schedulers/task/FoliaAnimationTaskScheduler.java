package net.ritasister.dc.util.schedulers.task;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.ritasister.dc.api.scheduler.AnimationTaskScheduler;
import net.ritasister.dc.util.schedulers.FoliaRunnable;
import org.bukkit.plugin.Plugin;

public class FoliaAnimationTaskScheduler implements AnimationTaskScheduler {

    private final Plugin plugin;
    private ScheduledTask task;

    public FoliaAnimationTaskScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void scheduleRepeatingTask(long delay, long period, Runnable taskLogic) {
        task = new FoliaRunnable(plugin.getServer().getAsyncScheduler(), null) {
            @Override
            public void run() {
                taskLogic.run();
            }
        }.runAtFixedRate(plugin, delay, period);
    }

    @Override
    public void cancel() {
        if (task != null) task.cancel();
    }
}
