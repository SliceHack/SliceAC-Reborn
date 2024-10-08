package net.sliceclient.ac.check.checks.badpackets;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "BadPackets", description = "Checks for invalid sneaking packet")
public class BadPacketsE extends Check {

    private boolean lastSneakingValue;

    public BadPacketsE(ACPlayer player) {
        super(player);
    }

    @PacketInfo({
            ACPacketType.ENTITY_ACTION,
            ACPacketType.TELEPORT_ACCEPT
    })
    public void onPacket(PacketEvent event) {
        if(event.getPacketType() == ACPacketType.TELEPORT_ACCEPT.packetType()) {
            setDisabledTicks(20);
            return;
        }

        if(isDisabled()) return;

        EnumWrappers.PlayerAction playerAction = event.getPacket().getPlayerActions().read(0);

        if (playerAction == EnumWrappers.PlayerAction.START_SNEAKING) {
            if(lastSneakingValue) {
                flag("lastSneaking=false");
            }

            lastSneakingValue = true;
        }

        if(playerAction == EnumWrappers.PlayerAction.STOP_SNEAKING) {
            if(!lastSneakingValue) {
                flag("lastSneaking=true");
            }

            lastSneakingValue = false;
        }
    }

    @PacketInfo({
            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK,
            ACPacketType.POSITION
    })
    public void onTick(PacketEvent event) {
        this.updateDisabledTicks();
    }

}
