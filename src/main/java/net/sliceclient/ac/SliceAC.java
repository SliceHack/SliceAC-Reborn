package net.sliceclient.ac;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketEvent;
import lombok.Getter;
import net.sliceclient.ac.check.CheckManager;
import net.sliceclient.ac.check.listener.BrandListener;
import net.sliceclient.ac.packet.ACPacketType;
import net.sliceclient.ac.packet.PacketManager;
import net.sliceclient.ac.packet.event.PacketEventManager;
import net.sliceclient.ac.packet.event.PacketInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class SliceAC extends JavaPlugin implements Listener {

    private final Map<Player, String> brandQueue = new HashMap<>();

    private ProtocolManager protocolManager;
    private PacketManager packetManager;

    private boolean testServer;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        testServer = getConfig().getBoolean("test-server");

        packetManager = new PacketManager(this);
        packetManager.register();

        getServer().getPluginManager().registerEvents(this, this);
        PacketEventManager.register(this);

        registerPluginChannel("minecraft:brand", new BrandListener());
    }

    @Override
    public void onDisable() {
        CheckManager.disable();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CheckManager.register(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CheckManager.unregister(event.getPlayer());
    }

    private void registerPluginChannel(String channel, PluginMessageListener listener) {
        getServer().getMessenger().registerIncomingPluginChannel(this, channel, listener);
    }
}
