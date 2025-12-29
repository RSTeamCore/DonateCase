package net.ritasister.dc.handler;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.handler.Handler;
import org.bukkit.command.CommandExecutor;

import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler implements Handler<Void> {

    private final DonateCasePaperPlugin donateCasePaperPlugin;

    public CommandHandler(DonateCasePaperPlugin donateCasePaperPlugin) {
        this.donateCasePaperPlugin = donateCasePaperPlugin;
    }

    @Override
    public void handle() {
        final List<CommandExecutor> allCommands = List.of(
                //new CommandWGRP(wgrpPlugin)
        );

        allCommands.forEach(command ->
                this.donateCasePaperPlugin.getLogger().info("Registered command: " + command.getClass().getSimpleName()));
        this.donateCasePaperPlugin.getLogger().info(String.format("All commands registered successfully! List of available registered commands: %s",
                allCommands.stream()
                        .map(command -> command.getClass().getSimpleName())
                        .collect(Collectors.joining(", "))));
    }
}
