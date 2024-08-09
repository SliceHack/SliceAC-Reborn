package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedEnumEntityUseAction;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

@CheckInfo(name = "Movement", description = "Check's for incorrect block placement")
public class MovementC extends Check {

    public MovementC(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ ACPacketType.USE_ITEM} )
    public void onBlockPlace(PacketEvent e) {
        // what fields can I use in this?
//        WrappedEnumEntityUseAction action = e.getPacket().getEnumEntityUseActions().read(0);
//        BlockPosition blockPosition = e.getPacket().getBlockPositionModifier().read(0);
//        BlockFace blockFace = BlockFace.fromInt(e.getPacket().getIntegers().read(1));
//
//        flag("action=" + action + " blockPosition=" + blockPosition + " blockFace=" + blockFace);
    }

    private enum BlockFace {
        BOTTOM_FACE,
        TOP_FACE,
        NORTH_FACE,
        SOUTH_FACE,
        WEST_FACE,
        EAST_FACE;

        public static BlockFace fromInt(int i) {
            switch (i) {
                case 0:
                    return BOTTOM_FACE;
                case 1:
                    return TOP_FACE;
                case 2:
                    return NORTH_FACE;
                case 3:
                    return SOUTH_FACE;
                case 4:
                    return WEST_FACE;
                case 5:
                    return EAST_FACE;
                default:
                    return null;
            }
        }
    }

}
