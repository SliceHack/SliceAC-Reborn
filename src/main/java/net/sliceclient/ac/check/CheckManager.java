package net.sliceclient.ac.check;

import lombok.Getter;
import net.sliceclient.ac.check.checks.TestCheck;
import net.sliceclient.ac.check.checks.badpackets.BadPacketsA;
import net.sliceclient.ac.check.checks.badpackets.BadPacketsB;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.packet.event.PacketEventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class CheckManager {

    public static Logger logger = LogManager.getLogger(CheckManager.class);

    private final static Map<ACPlayer, CheckManager> managerMap = new HashMap<>();

    private final Class<?>[] classes = new Class[]{
            BadPacketsA.class,
            BadPacketsB.class
    };

    private final List<Check> checks = new ArrayList<>();

    public CheckManager(ACPlayer player) {
        Arrays.stream(classes).forEach(clazz -> {
            try {
                Check check = (Check) clazz.getConstructor(ACPlayer.class).newInstance(player);

                checks.add(check);
                PacketEventManager.register(check, player.getPlayer());
            } catch (Exception e) {
                logger.error("Failed to register check {}", clazz.getSimpleName());
            }
        });
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
}
