package net.sliceclient.ac.check.checks.badpackets;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "BadPackets", description = "Invalid Pitch", maxViolations = 2)
public class BadPacketsB extends Check {

    public BadPacketsB(ACPlayer player) {
        super(player);
    }

    @PacketInfo({
            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK
    })
    public void onPacket(PacketEvent event) {
        float pitch = event.getPacket().getFloat().read(1);

        if(Math.abs(pitch) > 90) {
            flag("pitch=" + pitch);
        }
    }

}
