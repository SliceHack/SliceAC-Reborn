package net.sliceclient.ac.check;

import lombok.Getter;
import net.sliceclient.ac.SliceAC;
import net.sliceclient.ac.check.checks.badpackets.BadPacketsA;
import net.sliceclient.ac.check.checks.badpackets.BadPacketsB;
import net.sliceclient.ac.check.checks.badpackets.BadPacketsC;
import net.sliceclient.ac.check.checks.combat.CombatA;
import net.sliceclient.ac.check.checks.movement.MovementA;
import net.sliceclient.ac.check.checks.movement.MovementB;
import net.sliceclient.ac.check.checks.movement.MovementC;
import net.sliceclient.ac.check.checks.movement.MovementD;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.packet.event.PacketEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class CheckManager {

    public static Logger logger = LogManager.getLogger(CheckManager.class);

    private final static Map<ACPlayer, CheckManager> managerMap = new HashMap<>();

    private final Class<?>[] classes = new Class[]{
            BadPacketsA.class,
            BadPacketsB.class,
            BadPacketsC.class,
            MovementA.class,
            MovementB.class,
            MovementC.class,
            MovementD.class,
            CombatA.class
    };

    private final List<Check> checks = new ArrayList<>();
    private final ACPlayer player;

    public CheckManager(ACPlayer player) {
        this.player = player;
        player.setCheckManager(this);

        Arrays.stream(classes).forEach(clazz -> {
            try {
                Check check = (Check) clazz.getConstructor(ACPlayer.class).newInstance(player);

                checks.add(check);
                PacketEventManager.register(check, player.getPlayer());
            } catch (Exception e) {
                logger.error("Failed to register check {}", clazz.getSimpleName(), e);
            }
        });

        String brand = SliceAC.getPlugin(SliceAC.class).getBrandQueue().get(player.getPlayer());
        if(brand == null) {
            this.player.setBrand("Unknown Brand");
            return;
        }

        String str = String.format("§c§lSlice §8» §7%s (§c%s§7)", player.getPlayer().getName(), brand);
        this.player.setBrand(brand);

        SliceAC.getPlugin(SliceAC.class).getBrandQueue().remove(player.getPlayer());

        if(SliceAC.getPlugin(SliceAC.class).isTestServer()) {
            player.getPlayer().sendMessage(str);
            return;
        }

        Bukkit.getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.isOp() || onlinePlayer.hasPermission("slice.alerts"))
                .forEach(onlinePlayer -> onlinePlayer.sendMessage(str));
    }

    public static void disable() {
        new HashMap<>(managerMap).keySet().forEach(acPlayer -> unregister(acPlayer.getPlayer()));
    }

    public static void register(Player player) {
        ACPlayer acPlayer = new ACPlayer(player);

        managerMap.put(acPlayer, new CheckManager(acPlayer));
    }

    public static void unregister(Player player) {
        ACPlayer acPlayer = new ACPlayer(player);

        CheckManager manager = managerMap.get(acPlayer);

        if(manager == null) return;

        PacketEventManager.unregister(player);
        managerMap.remove(acPlayer);
    }

    public static CheckManager getCheckManager(Player player) {
        return managerMap.get(new ACPlayer(player));
    }
}
