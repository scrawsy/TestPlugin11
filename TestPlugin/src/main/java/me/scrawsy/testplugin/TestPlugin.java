package me.scrawsy.testplugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() != Material.GOLDEN_APPLE) return;

        if (player.getInventory().contains(Material.GOLDEN_APPLE, 32)) {
            event.setCancelled(true);
            // Eject excess golden apples
            ejectExcessApples(player);
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem().getItemStack();

        if (item.getType() == Material.GOLDEN_APPLE && player.getInventory().contains(Material.GOLDEN_APPLE, 31)) {
            event.setCancelled(true);
            // Eject excess golden apples
            ejectExcessApples(player);
        }
    }

    private void ejectExcessApples(Player player) {
        int excess = player.getInventory().all(Material.GOLDEN_APPLE).values().stream()
                .mapToInt(ItemStack::getAmount)
                .sum() - 32;

        if (excess > 0) {
            player.getInventory().remove(Material.GOLDEN_APPLE);
            ItemStack excessItem = new ItemStack(Material.GOLDEN_APPLE, excess);
            player.getWorld().dropItem(player.getLocation(), excessItem);
        }
    }
}



