package net.sliceclient.ac.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.SliceAC;
import net.sliceclient.ac.packet.event.PacketEventManager;

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
        PacketEventManager.handle(type, event);
    }

}
