package ind.johnnyht.boatindicator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static ind.johnnyht.boatindicator.BoatIndicator.PlayerMoveListener.getHead;

public final class BoatIndicator extends JavaPlugin implements Listener {
    private ArmorStand armorStand = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        if (armorStand != null) {
            armorStand.remove();
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && event.getVehicle() instanceof Boat) {
            Player player = (Player) event.getEntered();
            Boat boat = (Boat) event.getVehicle();

            // Create an invisible armor stand
            armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
            armorStand.setVisible(false);
            armorStand.setSmall(true);

            // Create an ItemStack that represents the player's head
            ItemStack headItem = getHead(player);

            // Set the armor stand's helmet to the player's head ItemStack
            armorStand.getEquipment().setHelmet(headItem);

            // Teleport the armor stand to the player's location
            armorStand.teleport(player.getLocation());

            // Watch for player movement
            Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(player, armorStand), this);
        }
    }

    class PlayerMoveListener implements Listener {
        private final Player player;
        private ArmorStand armorStand;

        public PlayerMoveListener(Player player, ArmorStand armorStand) {
            this.player = player;
            this.armorStand = armorStand;
        }

        @EventHandler
        public void onVehicleExit(VehicleExitEvent event) {
            if (event.getExited() instanceof Player && event.getVehicle() instanceof Boat) {
                if (armorStand != null) {
                    armorStand.remove();
                    armorStand = null;
                }
            }
        }

        public static ItemStack getHead(Player player) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
            SkullMeta skull = (SkullMeta) item.getItemMeta();
            skull.setDisplayName(player.getName());
            skull.setOwner(player.getName());
            item.setItemMeta(skull);
            return item;
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
            Player player = event.getPlayer();
            Location newLocation = event.getPlayer().getLocation();
            armorStand.teleport(newLocation);
            armorStand.teleport(newLocation.setDirection(player.getLocation().getDirection()));
    }
}
