package net.sliceclient.ac.check.checks.badpackets;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "BadPackets", description = "Invalid Sneaking", maxViolations = 2)
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
            if(this.lastSneakingValue) {
                flag("lastSneaking=false");
            }

            this.lastSneakingValue = true;
        }

        if(playerAction == EnumWrappers.PlayerAction.STOP_SNEAKING) {
            if(!this.lastSneakingValue) {
                flag("lastSneaking=true");
            }

            this.lastSneakingValue = false;
        }
    }

    @PacketInfo({
            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK,
            ACPacketType.POSITION
    })
    public void onTick(PacketEvent event) {
        this.updateDisabledTicks();

        if(event.getPlayer().isDead()) {
            setDisabledTicks(20);
        }
    }

}
