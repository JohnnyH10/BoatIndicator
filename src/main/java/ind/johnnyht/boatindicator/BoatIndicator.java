package ind.johnnyht.boatindicator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
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
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().isInsideVehicle() && event.getPlayer().getVehicle() instanceof Boat) {
            Player player = event.getPlayer();
            Location playerLocation = player.getLocation();

            // Calculate the direction the player is looking
            Vector direction = playerLocation.getDirection();

            // Summon the player's head as a block and set its direction
            Location headLocation = playerLocation.clone().add(0, 1, 0);
            FallingBlock headBlock = headLocation.getWorld().spawnFallingBlock(headLocation, Material.PLAYER_HEAD.createBlockData());

            // Adjust the block's orientation based on the player's viewing direction
            headBlock.setVelocity(direction.multiply(1.0)); // You may need to tweak the multiplier to get the desired orientation

            // Schedule the removal of the head block after one tick
            getServer().getScheduler().runTaskLater(this, () -> {
                headBlock.remove();
            }, 1);
        }
    }
}
