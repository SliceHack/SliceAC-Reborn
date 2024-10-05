package net.sliceclient.ac.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.SliceAC;
import net.sliceclient.ac.check.CheckManager;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.packet.event.PacketEventManager;
import net.sliceclient.ac.processor.movement.MovementData;
import net.sliceclient.ac.processor.movement.MovementProcessor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/** for handling packet events (because I had a bad name for it if anyone questions why I put this comment) **/
public class PacketManager {

    private final SliceAC plugin;

    public PacketManager(SliceAC plugin) {
        this.plugin = plugin;
    }

    public void register() {
        PacketType.Play.Client.getInstance().values()
                .stream().filter(PacketType::isSupported)
                .forEach(packetType -> plugin.getProtocolManager().addPacketListener(new PacketAdapter(plugin, packetType) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        handlePacket(packetType, event, false);
                    }

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        handlePacket(packetType, event, true);
                    }
                }));
    }

    private void handlePacket(PacketType type, PacketEvent event, boolean sending) {
        if(!sending && type == ACPacketType.POSITION.packetType()
                || type == ACPacketType.POSITION_LOOK.packetType()
        ) {
            CheckManager playerCheckManager = CheckManager.getCheckManager(event.getPlayer());
            if(playerCheckManager == null)return;

            ACPlayer player = playerCheckManager.getPlayer();
            if(player == null) return;

            double x = event.getPacket().getDoubles().read(0), y = event.getPacket().getDoubles().read(1), z = event.getPacket().getDoubles().read(2);
            float yaw = event.getPacket().getFloat().read(0), pitch = event.getPacket().getFloat().read(1);
            boolean onGround = event.getPacket().getBooleans().read(0);

            MovementData movementData = new MovementData(
                    MovementData.PositionType.valueOf(type.name()),
                    x, y, z, yaw, pitch, onGround, player.getPlayer().isSneaking(), player.getPlayer().isSprinting()
            );

            playerCheckManager.getMovementProcessor().handle(movementData);
        }

        PacketEventManager.handle(type, event);
    }

}
