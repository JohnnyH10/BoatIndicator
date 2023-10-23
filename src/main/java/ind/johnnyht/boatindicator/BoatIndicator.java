package ind.johnnyht.boatindicator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public final class BoatIndicator extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player && event.getVehicle() instanceof Boat) {
            Player player = (Player) event.getEntered();
            spawnAndAttachArmorStand(player);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();
            // Check if the Armor Stand is holding a player head item (customize this check)
            if (armorStand.getHelmet() != null && armorStand.getHelmet().getType() == Material.PLAYER_HEAD) {
                // Remove the Armor Stand when it's interacted with
                armorStand.remove();
            }
        }
    }

    private void spawnAndAttachArmorStand(Player player) {
        Location playerLocation = player.getLocation();

        ArmorStand armorStand = playerLocation.getWorld().spawn(playerLocation, ArmorStand.class);
        armorStand.setMarker(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);

        // Attach the armor stand to the player
        armorStand.addPassenger(player);

        // Schedule a task to constantly teleport the armor stand to the player
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.isDead()) {
                    armorStand.remove();
                    this.cancel();
                } else {
                    armorStand.teleport(player);
                }
            }
        }.runTaskTimer(this, 0, 1);

        // Schedule a task to remove the armor stand if the player logs out
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.isDead()) {
                    armorStand.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(this, 20, 20);
    }
}
