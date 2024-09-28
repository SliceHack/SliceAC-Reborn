package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "Movement", description = "Speed in Air", maxViolations = 50)
public class MovementB extends Check {

    private double lastDeltaXZ;
    private int airTicks;

    public MovementB(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK})
    public void onPlayerUpdate(PacketEvent event) {
        // ignored 🤔😝😝😝
        if(player.isFlying()) {
            setDisabledTicks(20);
            return;
        }

        double deltaXZ = player.getMovementProcessor().deltaHypotXZ();
        double newDeltaXZ = deltaXZ * 0.98F;
        double motionXZ = deltaXZ - newDeltaXZ;

        double lastDeltaXZ = this.lastDeltaXZ * 0.98F;
        double lastMotionX = this.lastDeltaXZ - lastDeltaXZ;

        boolean isInAir = !player.onGround();

        double check = 0.01 + (player.getSpeedModifier() != 0 ? (player.getSpeedModifier() * 0.06) : 0);
        if(!isDisabled() && isInAir && airTicks < 10
                && motionXZ > check
                && lastMotionX > check) {
            flag("motionXZ=" + motionXZ + " airTicks=" + airTicks);
        }

        this.updateDisabledTicks();
        this.airTicks = isInAir ? this.airTicks + 1 : 0;
    }
}
