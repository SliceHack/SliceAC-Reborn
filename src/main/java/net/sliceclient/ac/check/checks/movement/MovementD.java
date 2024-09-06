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

    private double lastY, lastDeltaY;
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

        double y = event.getPacket().getDoubles().read(1);
        double deltaY = y - lastY;

        if(offGroundTicks >= 20 && deltaY == 0 && lastDeltaY == 0 && !isDisabled()) {
            flag("offGroundTicks=" + offGroundTicks + " deltaY=" + deltaY + " lastDeltaY=" + lastDeltaY);
        }

        this.updateDisabledTicks();
        this.offGroundTicks = player.onGround() ? 0 : this.offGroundTicks + 1;
        this.lastY = y;
        this.lastDeltaY = deltaY;
    }

}
