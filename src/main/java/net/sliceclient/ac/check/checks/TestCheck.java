package net.sliceclient.ac.check.checks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import net.sliceclient.ac.check.Check;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.event.PacketInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@CheckInfo(name = "TestCheck", description = "Test check")
public class TestCheck extends Check {

    public TestCheck(ACPlayer player) {
        super(player);
    }

    @PacketInfo({
            ACPacketType.USE_ITEM_ON
    })
    public void onUpdate(PacketEvent event) {
    }

}
