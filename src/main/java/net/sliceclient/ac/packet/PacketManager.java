package net.sliceclient.ac.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.SliceAC;
import net.sliceclient.ac.check.CheckManager;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.packet.event.PacketEventManager;
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
        if(sending && type == PacketType.Play.Server.DAMAGE_EVENT) {
            CheckManager playerCheckManager = CheckManager.getCheckManager(event.getPlayer());
            if(playerCheckManager == null) return;

            ACPlayer player = playerCheckManager.getPlayer();
            if(player == null) return;

            player.hurtTicks = 0; // just damaged
        }

        if(!sending && type == ACPacketType.POSITION.packetType()
                || type == ACPacketType.LOOK.packetType()
                || type == ACPacketType.POSITION_LOOK.packetType()
                || type == ACPacketType.GROUND.packetType()
        ) {
            CheckManager playerCheckManager = CheckManager.getCheckManager(event.getPlayer());
            if(playerCheckManager == null)return;

            ACPlayer player = playerCheckManager.getPlayer();
            if(player == null) return;

            player.hurtTicks++; // count how long ago the player was hurt
        }

        PacketEventManager.handle(type, event);
    }

}
