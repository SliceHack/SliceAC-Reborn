package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "Movement", description = "Checks for Strafe")
public class MovementC extends Check {

    private double lastStrafe, lastX, lastZ;

    public MovementC(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK})
    public void onPlayerUpdate(PacketEvent event) {
        // ignored 🤔😝😝😝
        if(player.isFlying()) {
            setDisabledTicks(20);
            return;
        }

        double x = event.getPacket().getDoubles().read(0);
        double z = event.getPacket().getDoubles().read(2);

        if(lastX == x && lastZ == z) {
            this.lastX = x;
            this.lastZ = z;
            return;
        }

        double deltaX = x - this.lastX;
        double deltaZ = z - this.lastZ;
        double strafe = Math.abs(Math.hypot(deltaX, deltaZ));

        double check = 0.46;
        if(strafe > check && lastStrafe > check && !isDisabled()) {
            flag("strafe=" + strafe + " lastStrafe=" + lastStrafe);
        }

        this.updateDisabledTicks();
        this.lastStrafe = strafe;
        this.lastX = x;
        this.lastZ = z;

        if(isDisabled()) {
            this.lastStrafe = 0;
            this.lastX = x;
            this.lastZ = z;
        }
    }
}
