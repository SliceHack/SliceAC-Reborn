package net.sliceclient.ac.check.checks.movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@CheckInfo(name = "Movement", description = "NoSlowDown", maxViolations = 40)
public class MovementE extends Check {

    private double lastX, lastY, lastZ, lastDeltaY;
    private boolean runCheck, usingItem;
    private int failed;

    public MovementE(ACPlayer player) {
        super(player);
    }

    @PacketInfo({
            ACPacketType.POSITION_LOOK,
            ACPacketType.LOOK,
            ACPacketType.POSITION,
            ACPacketType.BLOCK_DIG,
            ACPacketType.USE_ITEM
    })
    public void onPacket(PacketEvent event, PacketType type) {
        if (type == ACPacketType.USE_ITEM.packetType()) {

            EnumWrappers.Hand hand = event.getPacket().getHands().read(0);
            ItemStack itemStack = hand == EnumWrappers.Hand.MAIN_HAND ? player.getPlayer().getInventory().getItemInMainHand() : player.getPlayer().getInventory().getItemInOffHand();

            this.usingItem = canBeUsed(itemStack);
            return;
        }

        if (type == ACPacketType.BLOCK_DIG.packetType()) {
            EnumWrappers.PlayerDigType digType = event.getPacket().getPlayerDigTypes().read(0);

            if (digType == EnumWrappers.PlayerDigType.RELEASE_USE_ITEM) {
                this.usingItem = false;
                this.failed = 0;
                this.runCheck = false; // cause they let go of the item
            }
            return;
        }

        if(player.isFlying()) {
            setDisabledTicks(20);
            return;
        }

        double x = event.getPacket().getDoubles().read(0);
        double y = event.getPacket().getDoubles().read(1);
        double z = event.getPacket().getDoubles().read(2);

        double deltaX = x - this.lastX;
        double deltaY = y - this.lastY;
        double deltaZ = z - this.lastZ;

        double deltaXZ = Math.hypot(deltaX, deltaZ);

        double yaw = event.getPacket().getFloat().read(0);
        double speed = (Math.abs(Math.sin(Math.toRadians(yaw))) + Math.abs(Math.cos(Math.toRadians(yaw))))
                * (0.1 + ((Math.abs(deltaY) > 0 || Math.abs(lastDeltaY) > 0) ? 0.25 : 0.1));

        double finalSpeed = speed * (player.getSpeedModifier() != 0 ? (player.getSpeedModifier() * 0.25) : 1);

        if(usingItem && runCheck && deltaXZ > finalSpeed && !isDisabled() && ++failed > 2) {
            flag("deltaXZ=" + deltaXZ + " usingItem=" + usingItem + " ticks=" + failed + " predictedSpeed=" + finalSpeed);
        }

        this.updateDisabledTicks();
        this.runCheck = true;
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
        this.lastDeltaY = deltaY;
    }

    public boolean canBeUsed(ItemStack itemStack) {
        return (itemStack.getType().isEdible() && (player.getPlayer().getFoodLevel() < 20 || player.getPlayer().getGameMode() == GameMode.CREATIVE))

                || (itemStack.getType() == Material.SHIELD || (itemStack.getType() == Material.BOW && hasArrows(player)));
    }

    public boolean hasArrows(ACPlayer player) {
        return player.getPlayer().getInventory().contains(Material.ARROW);
    }


}
