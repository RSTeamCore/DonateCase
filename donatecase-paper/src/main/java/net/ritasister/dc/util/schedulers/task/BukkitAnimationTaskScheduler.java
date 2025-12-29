package net.ritasister.dc.util.schedulers.task;

import net.ritasister.dc.api.scheduler.AnimationTaskScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitAnimationTaskScheduler implements AnimationTaskScheduler {

    private final Plugin plugin;
    private BukkitTask task;

    public BukkitAnimationTaskScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void scheduleRepeatingTask(long delay, long period, Runnable taskLogic) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, taskLogic, delay, period);
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel();
        }
    }
}

