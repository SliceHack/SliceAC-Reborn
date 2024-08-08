package net.sliceclient.ac.check.checks.badpackets;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "BadPackets", description = "Checks for invalid HotBar selections")
public class BadPacketsA extends Check {

    private int lastSlot = -1;

    public BadPacketsA(ACPlayer player) {
        super(player);
    }

    @PacketInfo(ACPacketType.HELD_ITEM_SLOT)
    public void onPacket(PacketEvent event) {
        int slot = event.getPacket().getIntegers().read(0);

        if(slot == this.lastSlot) {
            flag("slot=" + slot + " lastSlot=" + lastSlot);
        }

        this.lastSlot = slot;
    }
}
