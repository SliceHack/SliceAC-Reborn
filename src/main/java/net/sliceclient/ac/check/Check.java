package net.sliceclient.ac.check;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;

@Getter
public class Check {

    private final CheckInfo info = getClass().getAnnotation(CheckInfo.class);

    protected final ACPlayer player;
    protected final String name, description, type;
    protected final int maxViolations;

    protected int violations;

    public Check(ACPlayer player) {
        if(info == null) {
            throw new IllegalArgumentException("CheckInfo annotation not found.");
        }

        this.name = info.name();
        this.description = info.description();
        this.type = info.type();
        this.maxViolations = info.maxViolations();
        this.player = player;
    }

    public void flag(String message) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("§c§lSlice §8» §c")
                .append(player.getPlayer().getName()).append("§7 failed §c").append(name).append(" §7(§c").append(type)
                .append("§7) §7|").append(" §c").append("x").append(++violations);

        if(maxViolations != -1) {
            stringBuilder.append("§8/§c").append(maxViolations);
        }

        Component component = Component.text(stringBuilder.toString()).hoverEvent(Component.text(message));

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.isOp() || player.hasPermission("slice.alerts"))
                .forEach(player -> player.sendMessage(component));
    }

}
