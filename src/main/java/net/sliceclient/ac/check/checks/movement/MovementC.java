package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "Movement", description = "Check's for incorrect block placement")
public class MovementC extends Check {

    public MovementC(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ ACPacketType.BLOCK_PLACE} )
    public void onBlockPlace(PacketEvent event) {
        float cursorX = event.getPacket().getFloat().read(0);
        float cursorY = event.getPacket().getFloat().read(1);
        float cursorZ = event.getPacket().getFloat().read(2);

        String blockFace = event.getPacket().getEnumEntityUseActions().read(1).toString();

        flag("Cursor (" + cursorX + ", " + cursorY + ", " + cursorZ +") BlockFace" + " (" + blockFace + ")");
    }

    private enum BlockFace {
        BOTTOM_FACE,
        TOP_FACE,
        NORTH_FACE,
        SOUTH_FACE,
        WEST_FACE,
        EAST_FACE
    }

}
