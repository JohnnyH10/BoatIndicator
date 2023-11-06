package ind.johnnyht.boatindicator;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class BoatIndicator extends JavaPlugin implements Listener {

    private final Map<UUID, ArmorStand> armorStand = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        if (armorStand != null) {
            for(ArmorStand stand: armorStand.values()){
                stand.remove();
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && event.getVehicle() instanceof Boat) {
            Player player = (Player) event.getEntered();
            ArmorStand armorStand1 = player.getWorld().spawn(player.getLocation(), ArmorStand.class);

            // Create an invisible armor stand
            armorStand1.setVisible(false);
            armorStand1.setSmall(true);

            // Create an ItemStack that represents the player's head
            ItemStack headItem = getHead(player);

            // Set the armor stand's helmet to the player's head ItemStack
            armorStand1.getEquipment().setHelmet(headItem);

            // Teleport the armor stand to the player's location
            armorStand1.teleport(player.getLocation());

            armorStand.put(player.getUniqueId(),armorStand1);
        }
    }


        @EventHandler
        public void onVehicleExit(VehicleExitEvent event) {
            if (event.getExited() instanceof Player player && event.getVehicle() instanceof Boat) {
                ArmorStand stand = armorStand.get(player.getUniqueId());
                if (stand != null) {
                    stand.remove();
                    stand = null;
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


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

            Player player = event.getPlayer();
            Location newLocation = event.getPlayer().getLocation();
            ArmorStand stand = armorStand.get(player.getUniqueId());
            stand.teleport(newLocation);
            stand.teleport(newLocation.setDirection(player.getLocation().getDirection()));
    }
}
