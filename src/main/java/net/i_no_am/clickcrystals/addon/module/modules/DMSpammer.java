package net.i_no_am.clickcrystals.addon.module.modules;

import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import io.github.itzispyder.clickcrystals.util.minecraft.ChatUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.Randomizer;
import io.github.itzispyder.clickcrystals.util.misc.Timer;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.*;
import java.util.stream.Collectors;

public class DMSpammer extends AddonListenerModule {

    public DMSpammer() {
        super("dm-spammer", "Spams private messages to selected players.");
    }

    private final SettingSection scGeneral = getGeneralSection();

    public final ModuleSetting<String> targets = scGeneral.add(createStringSetting()
            .name("targets")
            .description("Comma-separated list of usernames to DM.")
            .def("player1,player2")
            .build()
    );

    public final ModuleSetting<String> ignores = scGeneral.add(createStringSetting()
            .name("ignored")
            .description("Comma-separated list of usernames to ignore.")
            .def("alex,steve")
            .build()
    );

    public final ModuleSetting<String> message = scGeneral.add(createStringSetting()
            .name("message")
            .description("Message to send.")
            .def("Hello there!")
            .build()
    );

    public final ModuleSetting<Integer> delayTicks = scGeneral.add(createIntSetting()
            .name("delay-ticks")
            .description("Average delay between each DM (in ticks, 20 ticks = 1 second).")
            .min(1)
            .max(200)
            .def(40)
            .build()
    );

    private long nextAllowedTime = 0;
    private final Randomizer random = new Randomizer();
    private final Map<String, Timer> cooldowns = new HashMap<>();
    private boolean warnedNoPlayers = false;

    @EventHandler
    private void onTick(ClientTickEndEvent e) {
        if (!isEnabled()) return;

        ClientPlayerEntity player = PlayerUtils.player();
        if (player == null || player.networkHandler == null) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime < nextAllowedTime) return;

        List<String> targetList = Arrays.stream(targets.getVal().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .toList();

        List<String> ignoreList = Arrays.stream(ignores.getVal().split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();

        if (targetList.isEmpty()) return;

        List<String> onlinePlayers = player.networkHandler.getPlayerList().stream()
                .map(entry -> entry.getProfile().getName().toLowerCase())
                .filter(name -> !ignoreList.contains(name) && targetList.contains(name))
                .collect(Collectors.toList());

        if (onlinePlayers.isEmpty()) {
            if (!warnedNoPlayers) {
                ChatUtils.sendPrefixMessage("§cNo target players are online or found!");
                warnedNoPlayers = true;
            }
            return;
        }

        Collections.shuffle(onlinePlayers);
        warnedNoPlayers = false; // Reset warning since there are valid players

        for (String target : onlinePlayers) {
            if (cooldowns.containsKey(target)) {
                Timer.End end = cooldowns.get(target).end();
                long cooldownMs = delayTicks.getVal() * 2 * 50L;
                if (end.timePassed() < cooldownMs) continue;
            }

            ChatUtils.sendChatCommand("msg " + target + " " + message.getVal());

            cooldowns.put(target, Timer.start());

            int baseTicks = delayTicks.getVal();
            double multiplier = 0.8 + (random.getRandomDouble(0.1, 10) * 0.4); // ~0.8x–1.2x
            long randomDelayMs = (long) (baseTicks * 50 * multiplier);
            nextAllowedTime = currentTime + randomDelayMs;

            break;
        }
    }
}