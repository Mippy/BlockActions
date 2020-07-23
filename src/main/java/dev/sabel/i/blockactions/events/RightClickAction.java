package dev.sabel.i.blockactions.events;

import dev.sabel.i.blockactions.BlockActions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class RightClickAction implements Listener {

    private final BlockActions plugin;
    public RightClickAction(BlockActions p) { plugin = p; }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        Material m = e.getClickedBlock().getType();
        if (!plugin.bas.actions.containsKey(m)) return;

        Player p = e.getPlayer();
        String res = plugin.bas.actions.get(m);
        e.setCancelled(true);

        if (res.equals("")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("DeniedMsg")));
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            return;
        }
        boolean opsave = p.isOp();
        p.setOp(true);
        try {
            p.chat("/" + res);
        } catch (RuntimeException err) {
            p.sendMessage(ChatColor.RED + "There was an error running this block's action. Tell a server admin to check the logs if you see this message.");
            err.printStackTrace();
        } finally {
            p.setOp(opsave);
        }

    }

}
