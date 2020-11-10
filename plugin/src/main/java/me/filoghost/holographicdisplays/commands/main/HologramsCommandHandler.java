/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.holographicdisplays.commands.main;

import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.commands.Colors;
import me.filoghost.holographicdisplays.commands.Strings;
import me.filoghost.holographicdisplays.commands.main.subs.AddlineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.AlignCommand;
import me.filoghost.holographicdisplays.commands.main.subs.CopyCommand;
import me.filoghost.holographicdisplays.commands.main.subs.CreateCommand;
import me.filoghost.holographicdisplays.commands.main.subs.DebugCommand;
import me.filoghost.holographicdisplays.commands.main.subs.DeleteCommand;
import me.filoghost.holographicdisplays.commands.main.subs.EditCommand;
import me.filoghost.holographicdisplays.commands.main.subs.HelpCommand;
import me.filoghost.holographicdisplays.commands.main.subs.InfoCommand;
import me.filoghost.holographicdisplays.commands.main.subs.InsertlineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ListCommand;
import me.filoghost.holographicdisplays.commands.main.subs.MovehereCommand;
import me.filoghost.holographicdisplays.commands.main.subs.NearCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ReadimageCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ReadtextCommand;
import me.filoghost.holographicdisplays.commands.main.subs.ReloadCommand;
import me.filoghost.holographicdisplays.commands.main.subs.RemovelineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.SetlineCommand;
import me.filoghost.holographicdisplays.commands.main.subs.TeleportCommand;
import me.filoghost.holographicdisplays.exception.CommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramsCommandHandler implements CommandExecutor {

    private List<HologramSubCommand> subCommands;
    private Map<Class<? extends HologramSubCommand>, HologramSubCommand> subCommandsByClass;

    public HologramsCommandHandler() {
        subCommands = new ArrayList<>();
        subCommandsByClass = new HashMap<>();
        
        registerSubCommand(new AddlineCommand());
        registerSubCommand(new CreateCommand());
        registerSubCommand(new DeleteCommand());
        registerSubCommand(new EditCommand());
        registerSubCommand(new ListCommand());
        registerSubCommand(new NearCommand());
        registerSubCommand(new TeleportCommand());
        registerSubCommand(new MovehereCommand());
        registerSubCommand(new AlignCommand());
        registerSubCommand(new CopyCommand());
        registerSubCommand(new ReloadCommand());
        
        registerSubCommand(new RemovelineCommand());
        registerSubCommand(new SetlineCommand());
        registerSubCommand(new InsertlineCommand());
        registerSubCommand(new ReadtextCommand());
        registerSubCommand(new ReadimageCommand());
        registerSubCommand(new InfoCommand());
        
        registerSubCommand(new DebugCommand());
        registerSubCommand(new HelpCommand());
    }
    
    public void registerSubCommand(HologramSubCommand subCommand) {
        subCommands.add(subCommand);
        subCommandsByClass.put(subCommand.getClass(), subCommand);
    }
    
    public List<HologramSubCommand> getSubCommands() {
        return new ArrayList<>(subCommands);
    }
    
    public HologramSubCommand getSubCommand(Class<? extends HologramSubCommand> subCommandClass) {
        return subCommandsByClass.get(subCommandClass);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (args.length == 0) {
            sender.sendMessage(Colors.PRIMARY_SHADOW + "Server is running " + Colors.PRIMARY + "Holographic Displays " + Colors.PRIMARY_SHADOW + "v" + HolographicDisplays.getInstance().getDescription().getVersion() + " by " + Colors.PRIMARY + "filoghost");
            if (sender.hasPermission(Strings.BASE_PERM + "help")) {
                sender.sendMessage(Colors.PRIMARY_SHADOW + "Commands: " + Colors.PRIMARY + "/" + label + " help");
            }
            return true;
        }
        
        for (HologramSubCommand subCommand : subCommands) {
            if (subCommand.isValidTrigger(args[0])) {
                
                if (!subCommand.hasPermission(sender)) {
                    sender.sendMessage(Colors.ERROR + "You don't have permission.");
                    return true;
                }
                
                if (args.length - 1 >= subCommand.getMinimumArguments()) {
                    try {
                        subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
                    } catch (CommandException e) {
                        sender.sendMessage(Colors.ERROR + e.getMessage());
                    }
                } else {
                    sender.sendMessage(Colors.ERROR + "Usage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
                }
                
                return true;
            }
        }
        
        sender.sendMessage(Colors.ERROR + "Unknown sub-command. Type \"/" + label + " help\" for a list of commands.");
        return true;
    }
}