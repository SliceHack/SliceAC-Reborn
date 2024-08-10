package net.sliceclient.ac.check;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.sliceclient.ac.SliceAC;
import net.sliceclient.ac.check.data.ACPlayer;
import net.sliceclient.ac.check.data.CheckInfo;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class Check {

    private final CheckInfo info = getClass().getAnnotation(CheckInfo.class);

    protected final ACPlayer player;
    protected final String name, type, description;
    protected final int maxViolations;

    @Setter
    private int disabledTicks;

    protected int violations;

    public Check(ACPlayer player) {
        if(info == null) {
            throw new IllegalArgumentException("CheckInfo annotation not found.");
        }

        this.name = info.name();
        this.description = info.description();
        this.maxViolations = info.maxViolations();
        this.player = player;
        this.type = createType(this, player.getCheckManager());
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

        boolean testServer = SliceAC.getPlugin(SliceAC.class).isTestServer();

        if(testServer) {
            player.getPlayer().sendMessage(component);
            return;
        }

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.isOp() || player.hasPermission("slice.alerts"))
                .forEach(player -> player.sendMessage(component));
    }

    private static String createType(Check check, CheckManager checkManager) {
        List<Check> copyedChecks = checkManager.getChecks();
        Check[] checks = copyedChecks.toArray(new Check[0]);

        // Create a new array with only the checks that have the same name as the input check
        List<Check> sameNameChecks = new ArrayList<>();
        for (Check check1 : checks) {
            if (check1.getName().equals(check.getName()) && !check1.equals(check)) {
                sameNameChecks.add(check1);
            }
        }

        sameNameChecks.sort(Comparator.comparing(Check::getType));

        int count = sameNameChecks.size();

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (count > alphabet.length()) {
            int quotient = (count - 1) / alphabet.length();
            int remainder = (count - 1) % alphabet.length();
            return alphabet.charAt(quotient) + String.valueOf(alphabet.charAt(remainder));
        } else {
            return String.valueOf(alphabet.charAt(count));
        }
    }

    public boolean isDisabled() {
        return disabledTicks > 0;
    }

    public void updateDisabledTicks() {
        if(disabledTicks > 0) {
            disabledTicks--;
        }
    }

    public void addChatMessage(String message) {
        Bukkit.broadcast(Component.text(message));
    }
}
