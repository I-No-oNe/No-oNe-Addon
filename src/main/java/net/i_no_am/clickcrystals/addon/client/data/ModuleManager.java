package net.i_no_am.clickcrystals.addon.client.data;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import io.github.itzispyder.clickcrystals.modules.Module;
import net.i_no_am.clickcrystals.addon.module.AddonListenerModule;
import net.i_no_am.clickcrystals.addon.module.AddonModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager implements Global {
    public List<Command> getAddonCommands() {
        return new ArrayList<>(system.commands().values().stream()
                .filter(command -> command.getClass().getPackage().getName().startsWith("net.i_no_am.clickcrystals.addon.command"))
                .toList());
    }

    public List<Module> getAddonModule() {
        return new ArrayList<>(system.collectModules().stream()
                .filter(module -> module instanceof AddonListenerModule || module instanceof AddonModule)
                .toList());
    }
}
