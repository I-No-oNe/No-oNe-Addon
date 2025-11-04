package net.i_no_am.clickcrystals.addon.client.data;

import net.i_no_am.clickcrystals.addon.utils.ClassUtils;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPluginManager implements IMixinConfigPlugin {

    private static final boolean HAS_MODMENU = ClassUtils.isClassLoaded("com.terraformersmc.modmenu.ModMenu");

    @Override
    public void onLoad(String mixinPackage) {
        // Optionally log for debugging
    }

    @Override
    public String getRefMapperConfig() {
        return null; // No refmap
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Only apply mixins related to ModMenu if it’s present
        if (mixinClassName.contains("modmenu")) {
            return HAS_MODMENU;
        }
        // Always apply other mixins
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // Not used
    }

    @Override
    public List<String> getMixins() {
        return null; // Use the default mixin list from mixins.json
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // Not used
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // Not used
    }
}
