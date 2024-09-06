package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "Movement", description = "GroundSpoof", maxViolations = 10)
public class MovementA extends Check {

    private int fakeOnGroundTicks;

    public MovementA(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK})
    public void onPacket(PacketEvent event) {
        boolean onGround = event.getPacket().getBooleans().read(0);

        if(onGround && !player.onGround()) fakeOnGroundTicks++;
        else fakeOnGroundTicks = 0;

        if(fakeOnGroundTicks > 1) {
            flag("fakeOnGroundTicks=" + fakeOnGroundTicks + " onGround=" + player.onGround() + " groundState=true");
        }

    }


}
