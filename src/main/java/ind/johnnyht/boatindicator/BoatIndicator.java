package ind.johnnyht.boatindicator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public final class BoatIndicator extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().isInsideVehicle() && event.getPlayer().getVehicle() instanceof Boat) {
            Player player = event.getPlayer();
            Location playerLocation = player.getLocation();

            // Calculate the direction the player is looking
            Location headLocation = playerLocation.clone().add(0, -1.25, 0); // Lower by two blocks

            // Create an ArmorStand to represent the player's head
            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            SkullMeta skull = (SkullMeta) item.getItemMeta();
            skull.setDisplayName(player.getName());
            ArmorStand headStand = headLocation.getWorld().spawn(headLocation, ArmorStand.class);
            headStand.setHelmet(getHead(player)); // Use player's head as helmet

            // Set the orientation of the ArmorStand
            headStand.setGravity(false); // Prevent it from falling
            headStand.setVisible(false); // Hide the ArmorStand

            // Schedule the removal of the ArmorStand after one tick
            getServer().getScheduler().runTaskLater(this, () -> {
                headStand.remove();
            }, 2);
        }
    }
    public static @Nullable ItemStack getHead(Player player) {
        int lifePlayer = (int) player.getHealth();
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(player.getName());
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("Custom head");
        skull.setLore(lore);
        skull.setOwner(player.getName());
        item.setItemMeta(skull);
        return item;
    }
}
