package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

@CheckInfo(name = "Movement", description = "Checks for Speed in air")
public class MovementB extends Check {

    private double lastDeltaXZ, lastX, lastZ;
    private boolean runCheck;
    private int airTicks;

    public MovementB(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK})
    public void onPlayerUpdate(PacketEvent event) {
        // ignored 🤔😝😝😝
        if(event.getPlayer().isFlying()) {
            runCheck = false;
            return;
        }

        double x = event.getPacket().getDoubles().read(0);
        double z = event.getPacket().getDoubles().read(2);
        double deltaXZ = Math.hypot(x - this.lastX, z - this.lastZ);

        double newDeltaXZ = deltaXZ * 0.98F;
        double motionXZ = deltaXZ - newDeltaXZ;

        double lastDeltaXZ = this.lastDeltaXZ * 0.98F;
        double lastMotionX = this.lastDeltaXZ - lastDeltaXZ;

        boolean isInAir = !player.onGround();

        if(runCheck && isInAir && airTicks < 10
                && motionXZ > 0.01
                && lastMotionX > 0.01) {
            flag("motionXZ=" + motionXZ + " airTicks=" + airTicks);
        }

        this.runCheck = true;
        this.lastDeltaXZ = deltaXZ;
        this.lastX = x;
        this.lastZ = z;
        this.airTicks = isInAir ? this.airTicks + 1 : 0;
    }
}
