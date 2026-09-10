package net.ritasister.dc.util.schedulers.task;

import net.ritasister.dc.api.scheduler.AnimationTaskScheduler;

public class AnimationTask {

    private final AnimationTaskScheduler scheduler;
    private final Runnable taskLogic;
    private int tick = 0;
    private boolean cancelled = false;

    public AnimationTask(AnimationTaskScheduler scheduler, Runnable taskLogic) {
        this.scheduler = scheduler;
        this.taskLogic = taskLogic;
    }

    public void start(long initialDelay, long period) {
        scheduler.scheduleRepeatingTask(initialDelay, period, () -> {
            if (cancelled) return;
            tick++;
            taskLogic.run();
        });
    }

    public void cancel() {
        cancelled = true;
        scheduler.cancel();
    }

    public int getTick() {
        return tick;
    }
}
