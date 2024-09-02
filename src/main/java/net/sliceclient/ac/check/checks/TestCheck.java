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
            ACPacketType.ABILITIES
    })
    public void onUpdate(PacketEvent event) {
        Object packetHandle = event.getPacket().getHandle();
        Field[] methods = packetHandle.getClass().getDeclaredFields();
        Method[] declaredMethods = packetHandle.getClass().getDeclaredMethods();

        try {
            debug("--------------------");
            debug("Fields:");
            for (Field field : methods) {
                field.setAccessible(true);
                debug(" - " + field.getName() + "=" + field.get(packetHandle) + " (" + field.getType().getName() + ")");
            }
            debug("      ");

            debug("Methods:");
            for (Method method : declaredMethods) {
                method.setAccessible(true);
                debug(" - " + method.getName() + "=" + method.getReturnType().getName());
            }
            debug("--------------------");
        } catch (Exception e) {
            debug("Error: " + e.getMessage());
        }
    }

}
