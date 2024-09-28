package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

/** experimental */
@CheckInfo(name = "Movement", description = "0 Motion Flight")
public class MovementD extends Check {

    private int offGroundTicks;

    public MovementD(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK, ACPacketType.LOOK})
    public void onPacket(PacketEvent event) {
        if(player.isFlying()) {
            this.setDisabledTicks(10);
            return;
        }

        double deltaY = player.getMovementProcessor().deltaY();

        if(offGroundTicks >= 20 && deltaY == 0 && player.getMovementProcessor().getLastDeltaY() == 0 && !isDisabled()) {
            flag("offGroundTicks=" + offGroundTicks + " deltaY=" + deltaY + " lastDeltaY=" + player.getMovementProcessor().getLastDeltaY());
        }

        this.updateDisabledTicks();
        this.offGroundTicks = player.onGround() ? 0 : this.offGroundTicks + 1;
    }

}
