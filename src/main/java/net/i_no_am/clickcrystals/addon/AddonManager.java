package net.i_no_am.clickcrystals.addon;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.data.Config;
import io.github.itzispyder.clickcrystals.data.JsonSerializable;
import net.fabricmc.api.ClientModInitializer;
import net.i_no_am.clickcrystals.addon.client.Manager;
import net.i_no_am.clickcrystals.addon.client.data.Constants;
import net.i_no_am.clickcrystals.addon.command.*;
import net.i_no_am.clickcrystals.addon.listener.AddonListener;
import net.i_no_am.clickcrystals.addon.module.modules.exploit.DMSpammer;
import net.i_no_am.clickcrystals.addon.module.modules.exploit.GhostInteractions;
import net.i_no_am.clickcrystals.addon.module.modules.misc.*;
import net.i_no_am.clickcrystals.addon.module.modules.pvp.LootKeeper;
import net.i_no_am.clickcrystals.addon.module.modules.pvp.MiddleClickPing;
import net.i_no_am.clickcrystals.addon.module.modules.pvp.NoPotionsHud;
import net.i_no_am.clickcrystals.addon.module.modules.pvp.Prevent;
import net.i_no_am.clickcrystals.addon.module.modules.render.HitColor;
import net.i_no_am.clickcrystals.addon.utils.ClassUtils;
import net.i_no_am.clickcrystals.addon.utils.FileUtils;

public final class AddonManager implements ClientModInitializer, Global {
    public static Config config = JsonSerializable.load(Config.PATH_CONFIG, Config.class, new Config());

    /**
     * @author I-No-oNe
     * WOW, this is actually working (;
     **/

    @Override
    public void onInitializeClient() {
        /*-----------------------------------------------------------------------------------------*/
        // Initialize Listeners
        system.addListener(new AddonListener());
        /*-----------------------------------------------------------------------------------------*/
        // Initialize Modules
        system.addModule(new DiscordScreenDisabler());
        system.addModule(new InfiniteChat());
        system.addModule(new CapeDisabler());
        system.addModule(new MiddleClickPing());
        system.addModule(new LootKeeper());
        system.addModule(new NoPotionsHud());
        system.addModule(new HitColor());
        system.addModule(new DMSpammer());
        system.addModule(new UpsideDown());
        system.addModule(new Prevent());
        system.addModule(new AntiOffHand());
        system.addModule(new GhostInteractions());
        system.addModule(new SafeWalk());
        system.addModule(new NameChanger());
        if (ClassUtils.isClassLoaded("com.terraformersmc.modmenu.ModMenu"))
            system.addModule(new ModMenuDisabler());
        /*-----------------------------------------------------------------------------------------*/
        // Initialize Commands
        system.addCommand(new HwidCommand());
        system.addCommand(new AddonInfoCommand());
        system.addCommand(new CleanCommand());
        system.addCommand(new QuitCommand());
        system.addCommand(new NetherPortalCommand());
        system.addCommand(new FreeRAMCommand());
        /*-----------------------------------------------------------------------------------------*/
        // Loading Configs and other checks (Because We disable The Regular CC Config Loading)
        system.println("-> loading config...");
        config.loadEntireConfig();
        system.println("-> loading profiles...");
        system.profiles.init();
        FileUtils.init();
        system.printf("-> Checking if %s can use the addon...", mc.getSession().getUsername());
        system.printf("<- Profile set '%s'", system.profiles.profileConfig.getCurrentProfileName());
        system.printf("-> Fetch addon data from: '%s'", Constants.URL.API);
        system.printf("-> User State: '%s'", Manager.banData.getBan());
        system.printf("<- Is allow to use addon: %s", !Manager.banData.getBan().shouldDisplay());
        system.printf("Finish Loading No one's Addon!");
    }
}