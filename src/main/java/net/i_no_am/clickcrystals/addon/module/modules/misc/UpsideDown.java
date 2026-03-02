package net.i_no_am.clickcrystals.addon.module.modules.misc;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class UpsideDown extends AddonListenerModule {
    public UpsideDown() {
        super("upside-down", "Renders all players and entities upside down");
    }

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<IgnoreType> ignoreType = scGeneral.add(createEnumSetting(IgnoreType.class)
            .name("ignore-type")
            .description("Make it so the selected entity won't be affected by this module")
            .def(IgnoreType.NONE)
            .build()
    );

    public enum IgnoreType {
        SELF, PLAYERS, ENTITIES, NONE
    }

    public boolean apply(boolean original, LivingEntity entity) {
        if (isEnabled())
            return !shouldIgnore(entity);
        return original;
    }

    boolean shouldIgnore(Entity entity) {
        return switch (ignoreType.getVal()) {
            case SELF -> entity instanceof ClientPlayerEntity;
            case PLAYERS -> entity instanceof PlayerEntity;
            case ENTITIES -> !(entity instanceof PlayerEntity || entity instanceof ClientPlayerEntity);
            case NONE -> false;
        };
    }
}