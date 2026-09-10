
package net.ritasister.dc.handler;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.handler.Handler;
import net.ritasister.dc.util.schedulers.FoliaRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class TaskHandler implements Handler<Void> {

    private final DonateCasePaperPlugin plugin;

    public TaskHandler(DonateCasePaperPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle() {
        final List<FoliaRunnable> allTasks = List.of();

        allTasks.forEach(task -> {
            this.plugin.getLogger().info("Registered task: " + task.getClass().getSimpleName());
            this.plugin.getTaskMap().put(task.getClass(), task);
        });

        final String registeredTasks = allTasks.stream()
                .map(task -> task.getClass().getSimpleName())
                .collect(Collectors.joining(", "));
        this.plugin.getLogger().info(String.format("All tasks registered successfully! List of available registered tasks: %s", registeredTasks));
        this.plugin.getLogger().info("Finished registering tasks.");

    }

}
