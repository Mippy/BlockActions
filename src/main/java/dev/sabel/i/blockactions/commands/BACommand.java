package dev.sabel.i.blockactions.commands;

import dev.sabel.i.blockactions.BlockActions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class BACommand implements CommandExecutor {

    private BlockActions plugin;

    public BACommand(BlockActions p) { plugin = p; }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean c = !(sender instanceof Player);
        Player p = null;
        if (!c) {
            p = (Player) sender;
            if (!p.hasPermission("blockactions.manage")) {
                p.sendMessage(ChatColor.RED + "You don't have permission to use this!");
                return true;
            }
        }
        Logger log = plugin.getLogger();

        if (args.length == 0 || (args.length == 1 && !args[0].equalsIgnoreCase("list"))) {
            sendHelp(c, p, log);
            return true;
        }

        if (args.length == 1) {
            if (plugin.bas.actions.size() == 0) {
                if (c) {
                    log.info("No Block Actions are set-up!");
                    return true;
                }
                p.sendMessage(ChatColor.RED + "No Block Actions are set-up!");
                return true;
            }
            ArrayList<String> l = new ArrayList<>();
            ArrayList<String> cl = new ArrayList<>();
            l.add(ChatColor.AQUA + "List of Block Actions:");
            cl.add("List of Block Actions:");
            plugin.bas.actions.forEach((k, v) -> {
                String mname = k.toString();
                if (v.equals("")) {
                    l.add(ChatColor.RED + mname + " - " + "Disabled!");
                    cl.add(mname + " - " + "Disabled!");
                    return;
                }
                l.add(ChatColor.YELLOW + mname + " - /" + v);
                cl.add(mname + " - /" + v);
            });
            if (c) {
                cl.forEach(log::info);
                return true;
            }
            l.forEach(p::sendMessage);
            return true;
        }

        if (args.length == 2) {
            if (!args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("disable")) {
                sendHelp(c, p, log);
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("remove")) {
            Material mat = Material.getMaterial(args[1]);
            String msg;
            if (mat == null) {
                msg = "Invalid Material! Try again.";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
                return true;
            }

            if (!plugin.bas.actions.containsKey(mat)) {
                msg = "That material did not have an action!";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
            }

            plugin.bas.actions.remove(mat);
            plugin.bas.save();
            msg = "Block Action Removed!";
            if (c) {
                log.info(msg);
                return true;
            }
            p.sendMessage(ChatColor.GREEN + msg);
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            Material mat = Material.getMaterial(args[1]);
            String msg;
            if (mat == null) {
                msg = "Invalid Material! Try again.";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
                return true;
            }
            if (!mat.isBlock()) {
                msg = "That material is not a block!";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
                return true;
            }
            if (plugin.bas.actions.containsKey(mat)) {
                msg = "That block already has an action! Use /ba remove " + args[1] + " first!";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
                return true;
            }

            plugin.bas.actions.put(mat, "");
            plugin.bas.save();
            msg = "Block Successfully Disabled!";
            if (c) {
                log.info(msg);
                return true;
            }
            p.sendMessage(ChatColor.GREEN + msg);
            return true;
        }

        if (args.length < 3) {
            sendHelp(c, p, log);
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            Material mat = Material.getMaterial(args[1]);
            String msg;
            if (mat == null) {
                msg = "Invalid Material! Try again.";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
                return true;
            }
            if (!mat.isBlock()) {
                msg = "That material is not a block!";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
                return true;
            }
            if (plugin.bas.actions.containsKey(mat)) {
                msg = "That block already has an action! Use /ba remove " + args[1] + " first!";
                if (c) {
                    log.info(msg);
                    return true;
                }
                p.sendMessage(ChatColor.RED + msg);
                return true;
            }
            ArrayList<String> argal = new ArrayList<>(args.length);
            if (args[2].equalsIgnoreCase("op") || args[2].equalsIgnoreCase("deop")) {
                if (c) {
                    log.info("You don't want to do this. Blocked.");
                    return true;
                }
                p.sendMessage(ChatColor.RED + "You don't want to do this. Blocked.");
                return true;
            }
            int count = 0;
            for (String a : args) {
                count++;
                if (count < 3) continue;
                if (count != args.length) argal.add(a + " ");
                if (count == args.length) argal.add(a);
            }
            String cmd = "";
            for (String x : argal) cmd += x;

            plugin.bas.actions.put(mat, cmd);
            plugin.bas.save();
            msg = "Block Action Added!";
            if (c) {
                log.info(msg);
                return true;
            }
            p.sendMessage(ChatColor.GREEN + msg);
            return true;
        }
        sendHelp(c, p, log);
        return true;
    }

    private static void sendHelp(boolean c, Player p, Logger log) {
        String[] l = {"/ba add <Material> <Command>", "/ba disable <Material>", "/ba remove <Material>", "/ba list"};
        if (c) {
            for (String li : l) log.info(li);
            return;
        }
        for (String li : l) p.sendMessage(ChatColor.RED + li);
    }

}
