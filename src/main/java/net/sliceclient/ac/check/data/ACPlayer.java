package net.sliceclient.ac.check.data;

import lombok.*;
import net.sliceclient.ac.check.CheckManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter @Setter
@RequiredArgsConstructor
public class ACPlayer {
    @NonNull private final Player player;
    private String brand = "Unknown Brand";
    private CheckManager checkManager;

    public boolean onGround() {
        double radius = .5f;
        Location location = player.getLocation();

        for (double x = location.getX() - radius; x <= location.getX() + radius; x += radius) {
            for (double z = location.getZ() - radius; z <= location.getZ() + radius; z += radius) {
                for (double y = location.getY(); y >= location.getY() - 1; y -= radius) {
                    Location loc = new Location(location.getWorld(), x, y, z);

                    if (!loc.getBlock().isPassable()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean hasElytra() {
        return player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.ELYTRA;
    }

    public boolean isFlying() {
        return player.isFlying() || player.isGliding();
    }
}
