package net.sliceclient.ac.check.checks;

import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

@CheckInfo(name = "TestCheck", type = "A", description = "Test check")
public class TestCheck extends Check {

    public TestCheck(ACPlayer player) {
        super(player);
    }

    @PacketInfo({ACPacketType.POSITION, ACPacketType.POSITION_LOOK, ACPacketType.LOOK})
    public void onUpdate(PacketEvent event) {
        event.getPlayer().sendMessage(name + " " + description + " " + event.getPlayer() + " " + player.getPlayer().getName());
    }

}
