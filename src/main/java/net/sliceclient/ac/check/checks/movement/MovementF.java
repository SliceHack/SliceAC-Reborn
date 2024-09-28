package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "Movement", description = "Flight", maxViolations = 100)
public class MovementF extends Check {

    private int failed, lastReward;

    public MovementF(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK, ACPacketType.LOOK, ACPacketType.FLYING, ACPacketType.GROUND})
    public void onMove(PacketEvent event) {
        if(player.isFlying() || player.isNearPassable(false)) {
            setDisabledTicks(20);
        }

        double expectedDeltaY = player.getMovementProcessor().getOffGroundTicks() == 0 ? 0 : (player.getMovementProcessor().getLastDeltaY() - 0.08) * 0.98;
        boolean invalid = (player.getMovementProcessor().getOffGroundTicks() > 15 && Math.abs(player.getMovementProcessor().deltaY() - expectedDeltaY) > (player.nearLiquid() ? 0.5 : 0.05));

        if (invalid && !isDisabled() && ++failed >= 5) {
            flag("deltaY=" + player.getMovementProcessor().deltaY() + " expectedDeltaY=" + expectedDeltaY + " offGroundTicks=" + player.getMovementProcessor().getOffGroundTicks());
            this.lastReward = 0;
        }

        if(this.lastReward >= 5) {
            this.failed = 0;
            this.lastReward = 0;
        }

        this.updateDisabledTicks();
        this.lastReward++;
    }
}
