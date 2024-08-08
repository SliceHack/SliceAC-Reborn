package net.sliceclient.ac.packet.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.PacketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public class PacketEventManager {

    private static final Logger logger = LogManager.getLogger(PacketEventManager.class);

    private final static List<RegisteredPacketEvent> packetListeners = new ArrayList<>();

    public static void register(Object listener) {
        register(listener, null);
    }

    public static void register(Object listener, Player player) {
        for(Method method : listener.getClass().getDeclaredMethods()) {
            if(!method.isAnnotationPresent(PacketInfo.class)) continue; // not listening for packets

            List<PacketType> types = new ArrayList<>();
            ACPacketType[] packetType = method.getAnnotation(PacketInfo.class).value();

            for(ACPacketType type : packetType) {
                if(type == ACPacketType.ALL) {
                    types.addAll(PacketType.Play.Client.getInstance().values());
                    break;
                }

                types.add(type.packetType());
            }

            for(PacketType type : types) {
                logger.info("Registering packet listener for {}", type);

                packetListeners.add(new RegisteredPacketEvent(type, method, listener, player));
            }
        }
    }

    public static void unregister(Player player) {
        packetListeners.removeIf(packetEvent -> packetEvent.getPlayer().equals(player));
    }

    public static void handle(PacketType type, PacketEvent event) {
        packetListeners.stream()
                .filter(packetEvent -> packetEvent.getType().equals(type))
                .filter(packetEvent -> packetEvent.getPlayer() == null || packetEvent.getPlayer().equals(event.getPlayer()))
                .forEach(packetEvent -> {
                    int args = packetEvent.getMethod().getParameterCount();

                    switch (args) {
                        case 0: packetEvent.invoke(); break;
                        case 1: packetEvent.invoke(event); break;
                        case 2: packetEvent.invoke(event, type); break;
                        default: logger.error("Invalid number of arguments for packet event method");
                    }
                });
    }

    @AllArgsConstructor @Getter
    private static class RegisteredPacketEvent {
        private final PacketType type;
        private final Method method;
        private final Object listener;
        private final Player player;

        public void invoke(Object... args) {
            try {
                method.invoke(listener, args);
            } catch (Exception e) {
                logger.error("Error while invoking packet event", e);
            }
        }
    }
}
