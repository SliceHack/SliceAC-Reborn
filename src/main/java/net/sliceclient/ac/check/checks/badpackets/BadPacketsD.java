package net.sliceclient.ac.check.checks.badpackets;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "BadPackets", description = "Duplicated Aim", maxViolations = 5)
public class BadPacketsD extends Check {

    private int ticks; // sometimes mojang duplicates packets so it's gonna not be that good of a check
    private float lastYaw, lastPitch;
    private boolean lastPacketWasTeleport, shouldStartChecking;

    public BadPacketsD(ACPlayer player) {
        super(player);
    }

    // I know I should not be doing it this way, but I can't figure out how to do it properly and I ain't got that time for that
    @PacketInfo({
            ACPacketType.USE_ENTITY,
            ACPacketType.USE_ITEM
    })
    public void onUse(PacketEvent event) {
        setDisabledTicks(5);
    }

    @PacketInfo({
            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK,
            ACPacketType.TELEPORT_ACCEPT
    })
    public void onPacket(PacketEvent event) {
        if (event.getPacket().getType() == ACPacketType.TELEPORT_ACCEPT.packetType()) {
            lastPacketWasTeleport = true;
            setDisabledTicks(10);
            return;
        }

        if (!isDisabled() && lastPacketWasTeleport) lastPacketWasTeleport = false;
        if (lastPacketWasTeleport) return;

        float yaw = event.getPacket().getFloat().read(0);
        float pitch = event.getPacket().getFloat().read(1);

        boolean failed = shouldStartChecking && yaw == lastYaw && pitch == lastPitch;
        if (failed && ++ticks >= 2) {
            flag("yaw=" + yaw + " pitch=" + pitch + " lastYaw=" + lastYaw + " lastPitch=" + lastPitch + " ticks=" + ticks);
        } else if (!failed) ticks = 0;

        this.lastYaw = yaw;
        this.lastPitch = pitch;
        this.shouldStartChecking = true;

        if(isDisabled()) {
            shouldStartChecking = false;
            ticks = 0;
        }
    }

    @PacketInfo({
            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK,
    })
    public void onTick(PacketEvent event) {
        updateDisabledTicks();
    }

}
