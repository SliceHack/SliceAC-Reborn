package net.sliceclient.ac.processor.movement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.processor.ProcessedData;

@AllArgsConstructor
public class MovementData extends ProcessedData {
    public final PositionType type;
    public double x, y, z;
    public float yaw, pitch;
    public boolean onGround, sprinting, sneaking;

    public enum PositionType {
        POSITION, POSITION_LOOK, LOOK, GROUND
    }

}
