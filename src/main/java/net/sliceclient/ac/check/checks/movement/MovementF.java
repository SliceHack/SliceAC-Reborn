package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "Movement", description = "Flight", maxViolations = 100)
public class MovementF extends Check {

    private double lastY, lastDeltaY;
    private int failed, offGroundTicks, lastReward;
    private boolean wasOnGround;

    public MovementF(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK, ACPacketType.LOOK, ACPacketType.FLYING, ACPacketType.GROUND})
    public void onMove(PacketEvent event) {
        if(player.isFlying() || player.isNearPassable(false)) {
            setDisabledTicks(20);
        }

        double y = event.getPacket().getDoubles().read(1);
        double deltaY = y - this.lastY;

        double expectedDeltaY = offGroundTicks == 0 ? 0 : (this.lastDeltaY - 0.08) * 0.98;
        boolean invalid = (offGroundTicks > 5 && Math.abs(deltaY - expectedDeltaY) > (player.nearLiquid() ? 0.5 : 0.05));

        if (invalid && !isDisabled() && ++failed >= 2) {
            flag("deltaY=" + deltaY + " expectedDeltaY=" + expectedDeltaY + " offGroundTicks=" + offGroundTicks);
            this.lastReward = 0;
        }

        if(this.lastReward >= 10) {
            this.failed = 0;
            this.lastReward = 0;
        }

        if (player.onGround() && !wasOnGround) this.offGroundTicks = 0;
        else if (!player.onGround()) this.offGroundTicks++;

        this.updateDisabledTicks();
        this.lastReward++;
        this.wasOnGround = player.onGround();
        this.lastY = y;
        this.lastDeltaY = deltaY;
    }
}
