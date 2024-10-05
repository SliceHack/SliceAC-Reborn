package net.sliceclient.ac.check.checks.badpackets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "BadPackets", description = "Checks for inventory selection while sprinting")
public class BadPacketsF extends Check {

    public BadPacketsF(ACPlayer player) {
        super(player);
    }

    @PacketInfo({
            ACPacketType.PONG
    })
    public void onWindowClick(PacketEvent event, PacketType type) {
        debug("PONG");
    }

}
