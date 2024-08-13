package net.sliceclient.ac.check.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.SliceAC;
import net.sliceclient.ac.check.CheckManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class BrandListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes) {
        ByteArrayDataInput dataInput = ByteStreams.newDataInput(bytes);
        String brand = dataInput.readLine();

        assert brand != null;
        SliceAC.getPlugin(SliceAC.class).getBrandQueue().put(player, brand.substring(1));
    }
    
}
