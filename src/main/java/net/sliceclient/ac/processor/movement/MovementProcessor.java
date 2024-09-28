package net.sliceclient.ac.processor.movement;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.processor.ProcessedData;
import net.sliceclient.ac.processor.Processor;
import org.bukkit.Bukkit;

@Getter
@RequiredArgsConstructor
public class MovementProcessor implements Processor {

    @NonNull private final ACPlayer player;

    private long lastUpdate = System.currentTimeMillis();

    private double x, y, z, lastX, lastY, lastZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaHypotXZ;
    private float yaw, pitch, lastYaw, lastPitch, lastDeltaYaw, lastDeltaPitch;

    private boolean onGround;
    private int onGroundTicks, offGroundTicks;

    private boolean registered;

    @Override
    public void handle(ProcessedData data) {
        if (!(data instanceof MovementData movement))
            throw new IllegalArgumentException("Data provided is not MovementData");

        this.lastX = this.x;
        this.lastY = this.y;
        this.lastZ = this.z;
        this.x = movement.x;
        this.y = movement.y;
        this.z = movement.z;

        this.lastYaw = this.yaw;
        this.lastPitch = this.pitch;

        this.yaw = movement.yaw;
        this.pitch = movement.pitch;

        this.lastDeltaX = this.deltaX();
        this.lastDeltaY = this.deltaY();
        this.lastDeltaZ = this.deltaZ();
        this.lastDeltaHypotXZ = this.deltaHypotXZ();
        this.lastDeltaYaw = this.deltaYaw();
        this.lastDeltaPitch = this.deltaPitch();

        this.onGround = movement.onGround;

        // so we ain't updating to fast
        int ticks = (int) ((System.currentTimeMillis() - this.lastUpdate) / 50);
        if(ticks >= 1) {
            this.onGroundTicks = this.onGround ? this.onGroundTicks + 1 : 0;
            this.offGroundTicks = this.onGround ? 0 : this.offGroundTicks + 1;
        }

        this.lastUpdate = System.currentTimeMillis();
    }

    /** delta time 👅 **/
    public double deltaX() {
        return this.x - this.lastX;
    }

    public double deltaY() {
        return this.y - this.lastY;
    }

    public double deltaZ() {
        return this.z - this.lastZ;
    }

    public float deltaYaw() {
        return this.yaw - this.lastYaw;
    }

    public float deltaPitch() {
        return this.pitch - this.lastPitch;
    }

    public double deltaHypotXZ() {
        return Math.hypot(this.deltaX(), this.deltaZ());
    }

    @Override
    public ACPlayer player() {
        return player;
    }

    @Override
    public boolean registered() {
        return registered;
    }
}
