package net.sliceclient.ac.check.checks.combat;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "Combat", description = "Checks for AutoBlock")
public class CombatA extends Check {

    private int lastReleaseBlockTick;
    private boolean lastBlockHit;

    public CombatA(ACPlayer player) {
        super(player);
    }

    @PacketInfo({
            ACPacketType.USE_ENTITY,

            ACPacketType.POSITION,
            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK,
    })
    public void onPacket(PacketEvent event) {
        if(event.getPacketType() == ACPacketType.USE_ENTITY.packetType()) {
            if(player.isBlocking() && lastBlockHit && lastReleaseBlockTick == 0) {
                flag("lastReleaseBlockTick=" + lastReleaseBlockTick);
            }

            this.lastBlockHit = player.isBlocking();
            return;
        }

        if(player.isBlocking()) lastReleaseBlockTick = 0;
        else lastReleaseBlockTick++;
    }


}
