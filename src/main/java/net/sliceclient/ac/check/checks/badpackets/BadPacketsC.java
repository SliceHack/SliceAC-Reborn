package net.sliceclient.ac.check.checks.badpackets;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "BadPackets", description = "Checks for invalid swinging")
public class BadPacketsC extends Check {

    private boolean shouldBeChecking = false;
    private int lastAttackTick = -1, lastSwingTick = -1;
    private int lastLastAttackTick = -1, lastLastSwingTick = -1;

    public BadPacketsC(ACPlayer player) {
        super(player);
    }

    @PacketInfo({
            ACPacketType.ARM_ANIMATION,
            ACPacketType.USE_ENTITY,

            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK
    })
    public void onSwing(PacketEvent event) {
        if(event.getPacketType() == ACPacketType.ARM_ANIMATION.packetType()) {
            lastSwingTick = 0;
            return;
        }

        if(event.getPacketType() == ACPacketType.USE_ENTITY.packetType()) {
            lastAttackTick = 0;
            shouldBeChecking = event.getPacket().getEnumEntityUseActions().read(0).getAction() == EnumWrappers.EntityUseAction.ATTACK;
            return;
        }

        double difference = Math.abs(lastAttackTick - lastSwingTick);
        double lastDifference = Math.abs(lastLastAttackTick - lastLastSwingTick);

        if((difference != 0 && lastDifference != 0)
                && lastLastAttackTick != -1 && lastLastSwingTick != -1
                && lastAttackTick != -1 && lastSwingTick != -1
                && shouldBeChecking) {
            flag("difference=" + difference + " lastDifference=" + lastDifference + " lastLastAttackTick=" + lastLastAttackTick + " lastLastSwingTick=" + lastLastSwingTick);
        }

        this.shouldBeChecking = false;
        this.lastLastAttackTick = this.lastAttackTick;
        this.lastLastSwingTick = this.lastSwingTick;
        this.lastAttackTick++;
        this.lastSwingTick++;
    }



}
