package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

@CheckInfo(name = "Movement", description = "Strafe", maxViolations = 50)
public class MovementC extends Check {

    private double lastStrafe;
    private int failed, lastReward;

    public MovementC(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK})
    public void onPlayerUpdate(PacketEvent event) {
        // ignored 🤔😝😝😝
        if(player.isFlying() || player.nearIce() || (player.hurtTicks <= 5 && player.hurtTicks != -1)) {
            setDisabledTicks(20);
            return;
        }

        @NotNull Vector velocity = event.getPlayer().getVelocity();
        Vector3f vector3f = new Vector3f((float) velocity.getX(), (float) velocity.getY(), (float) velocity.getZ());

        double strafe = player.getMovementProcessor().deltaHypotXZ();

        double check = 0.46 + (player.getSpeedModifier() * 0.06);
        double velocityDelta = Math.hypot(vector3f.x(), vector3f.z());
        double finalCheck = vector3f.x() != 0.0f && vector3f.z() != 0.0f ? check + (velocityDelta * 0.4) : check;

        if(strafe > finalCheck && lastStrafe > finalCheck && !isDisabled() && ++failed >= 2) {
            flag("strafe=" + strafe + " lastStrafe=" + lastStrafe + " check=" + check + " finalCheck=" + finalCheck);
            this.lastReward = 0;
        }

        if(this.lastReward >= 10) {
            this.failed--;
            this.lastReward = 0;
        }

        if(this.failed == 0) this.lastReward = 0;

        this.updateDisabledTicks();
        this.lastReward++;
        this.lastStrafe = strafe;

        if(isDisabled()) {
            this.lastStrafe = 0;
        }
    }
}
