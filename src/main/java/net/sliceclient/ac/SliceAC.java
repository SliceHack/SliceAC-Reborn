package net.sliceclient.ac;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import lombok.Getter;
import net.sliceclient.ac.check.CheckManager;
import net.sliceclient.ac.packet.PacketManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SliceAC extends JavaPlugin implements Listener {

    private ProtocolManager protocolManager;
    private PacketManager packetManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        packetManager = new PacketManager(this);
        packetManager.register();

        getServer().getPluginManager().registerEvents(this, this);
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

}
