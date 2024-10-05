package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

@CheckInfo(name = "Movement", description = "Jump Check", maxViolations = 5)
public class MovementG extends Check {

    private int failed;

    public MovementG(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK})
    public void onJump(PacketEvent event) {
        if(player.isFlying()) {
            setDisabledTicks(20);
            return;
        }

        double motionY = player.getMovementProcessor().deltaY();

        double predictedMotion = 0.42f;
        if(player.getPlayer().hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
            int amplifier = Objects.requireNonNull(player.getPlayer().getPotionEffect(PotionEffectType.JUMP_BOOST)).getAmplifier();

            predictedMotion += (amplifier + 1) * 0.1f; // in minecraft src LivingEntity#getJumpBoostPower
        }

        if(motionY > predictedMotion && !isDisabled() && ++failed > 1) {
            flag("motionY=" + motionY + " predictedMotion=" + predictedMotion);
        }

        this.updateDisabledTicks();
    }

}
