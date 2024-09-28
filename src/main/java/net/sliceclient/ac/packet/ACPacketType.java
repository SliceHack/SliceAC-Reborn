package net.sliceclient.ac.packet;

import com.comphenix.protocol.PacketType;

import java.lang.reflect.Field;

public enum ACPacketType {
    ALL, /** every packet **/

    TELEPORT_ACCEPT,
    TILE_NBT_QUERY,
    DIFFICULTY_CHANGE,
    CHAT_ACK,
    CHAT_COMMAND,
    CHAT,
    CHAT_SESSION_UPDATE,
    CLIENT_COMMAND,
    SETTINGS,
    TAB_COMPLETE,
    ENCHANT_ITEM,
    WINDOW_CLICK,
    CLOSE_WINDOW,
    CUSTOM_PAYLOAD,
    B_EDIT,
    ENTITY_NBT_QUERY,
    USE_ENTITY,
    JIGSAW_GENERATE,
    KEEP_ALIVE,
    DIFFICULTY_LOCK,
    POSITION,
    POSITION_LOOK,
    LOOK,
    GROUND,
    VEHICLE_MOVE,
    BOAT_MOVE,
    PICK_ITEM,
    AUTO_RECIPE,
    ABILITIES,
    BLOCK_DIG,
    ENTITY_ACTION,
    STEER_VEHICLE,
    PONG,
    RECIPE_SETTINGS,
    RECIPE_DISPLAYED,
    ITEM_NAME,
    RESOURCE_PACK_STATUS,
    ADVANCEMENTS,
    TR_SEL,
    BEACON,
    HELD_ITEM_SLOT,
    SET_COMMAND_BLOCK,
    SET_COMMAND_MINECART,
    SET_CREATIVE_SLOT,
    SET_JIGSAW,
    STRUCT,
    UPDATE_SIGN,
    ARM_ANIMATION,
    SPECTATE,
    USE_ITEM,
    USE_ITEM_ON,
    BLOCK_PLACE,
    @Deprecated TRANSACTION,
    @Deprecated FLYING,
    @Deprecated CHAT_PREVIEW;

    public static ACPacketType getFromPacketType(PacketType type) {
        try {
            for (Field field : ACPacketType.class.getDeclaredFields()) {
                if (field.getType() == ACPacketType.class && ((ACPacketType) field.get(null)).packetType() == type) {
                    return (ACPacketType) field.get(null);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get packet type for " + type, e);
        }
        return null;
    }

    public PacketType packetType() {
        try {
            Field field = PacketType.Play.Client.class.getDeclaredField(name());
            return (PacketType) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get packet type for " + name(), e);
        }
    }

}