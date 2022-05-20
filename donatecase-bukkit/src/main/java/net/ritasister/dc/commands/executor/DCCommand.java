package net.ritasister.dc.commands.executor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class DCCommand implements CommandExecutor, TabExecutor {

    private Player p;
    private final transient String name;
    
    public DCCommand(String name) 
    {
        this.name = name;
    }

	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        try{
			run(sender,cmd,label,args);
		}catch(Exception e){
			e.printStackTrace();
		}
        return true;
    }
	
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		return onTabComplete(sender, p, cmd, label, args);
	}

	public abstract void run(CommandSender s, Command cmd, String label, String[] args) throws Exception;
	
	public abstract List<String> onTabComplete(CommandSender s, Player p, Command cmd, String label, String[] args);
}