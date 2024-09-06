package net.sliceclient.ac.check.data;

import lombok.*;
import net.sliceclient.ac.check.CheckManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

@Getter @Setter
@RequiredArgsConstructor
public class ACPlayer {
    @NonNull private final Player player;
    public int hurtTicks = -1; // -1 means the player was never damaged
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

    public boolean nearLiquid() {
        double radius = 1.5f;
        Location location = player.getLocation();

        for (double x = location.getX() - radius; x <= location.getX() + radius; x += radius) {
            for (double z = location.getZ() - radius; z <= location.getZ() + radius; z += radius) {
                for (double y = location.getY(); y >= location.getY() - 1; y -= radius) {
                    Location loc = new Location(location.getWorld(), x, y, z);

                    if (loc.getBlock().isLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public boolean isNearPassable(boolean liquid) {
        double radius = 1.5f;
        Location location = player.getLocation();

        for (double x = location.getX() - radius; x <= location.getX() + radius; x += radius) {
            for (double z = location.getZ() - radius; z <= location.getZ() + radius; z += radius) {
                for (double y = location.getY(); y >= location.getY() - 1; y -= radius) {
                    Location loc = new Location(location.getWorld(), x, y, z);

                    boolean liquidCheck = !liquid || loc.getBlock().isLiquid();
                    boolean isAir = location.getBlock().getBlockData().getMaterial().isAir();
                    if (loc.getBlock().isPassable() && liquidCheck && !isAir) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean nearIce() {
        double radius = 1.5f;
        Location location = player.getLocation();
        if(location.getBlock().getType().name().contains("ICE")) return true;
        else if(location.clone().add(0, -1, 0).getBlock().getType().name().contains("ICE")) return true;

        for (double x = location.getX() - radius; x <= location.getX() + radius; x += radius) {
            for (double z = location.getZ() - radius; z <= location.getZ() + radius; z += radius) {
                for (double y = location.getY(); y >= location.getY() - 1; y -= radius) {
                    Location loc = new Location(location.getWorld(), x, y, z);

                    if (loc.getBlock().getType().name().contains("ICE")) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isBlocking() {
        return player.isBlocking();
    }

    public boolean isFlying() {
        return player.isFlying() || player.isGliding();
    }

    public int getSpeedModifier() {
        return player.hasPotionEffect(PotionEffectType.SPEED) ? Objects.requireNonNull(player.getPotionEffect(PotionEffectType.SPEED)).getAmplifier() : 0;
    }

}
