package net.ritasister.dc.api.scheduler;

public interface AnimationTaskScheduler {

    void scheduleRepeatingTask(long delay, long period, Runnable task);

    void cancel();
}
