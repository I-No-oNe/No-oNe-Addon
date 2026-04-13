package net.i_no_am.clickcrystals.addon.mixin;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Category;
import net.i_no_am.clickcrystals.addon.module.category.AddonCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(value = Categories.class, remap = false)
public class MixinCategories {

    @Shadow @Final private static LinkedHashMap<String, Category> categories;

    @Inject(method = "getCategories", at = @At("HEAD"), cancellable = true)
    private static void addAddonCategory(CallbackInfoReturnable<LinkedHashMap<String, Category>> cir) {
        LinkedHashMap<String, Category> updatedCategories = new LinkedHashMap<>();
        for (Map.Entry<String, Category> entry : categories.entrySet()) {
            if (entry.getKey().equals("Custom Made") || entry.getKey().equals("SCRIPTED"))
                updatedCategories.put("Addon", AddonCategory.ADDON);
            updatedCategories.put(entry.getKey(), entry.getValue());
        }
        if (!updatedCategories.containsKey("Addon")) updatedCategories.put("Addon", AddonCategory.ADDON);
        cir.setReturnValue(updatedCategories);
    }
}