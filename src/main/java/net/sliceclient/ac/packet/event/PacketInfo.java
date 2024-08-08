package net.sliceclient.ac.packet.event;

import net.sliceclient.ac.packet.ACPacketType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketInfo {
    ACPacketType[] value();
}
