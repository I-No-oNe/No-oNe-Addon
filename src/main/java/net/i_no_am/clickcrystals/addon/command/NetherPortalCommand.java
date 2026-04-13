package net.i_no_am.clickcrystals.addon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.itzispyder.clickcrystals.client.commands.Command;
import net.i_no_am.clickcrystals.addon.utils.BlockUtils;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class NetherPortalCommand extends Command {

    int x, y, z;

    public NetherPortalCommand() {
        super("find-portal", "Locates nearby Nether portals", ",find-portal");
    }

    @Override
    public void build(LiteralArgumentBuilder<ClientSuggestionProvider> builder) {
        builder.executes(_ -> {
            BlockPos portalPos = BlockUtils.findBlockPos(Blocks.NETHER_PORTAL);
            if (portalPos == null) error("No Nether portals are nearby.");

            x = portalPos.getX();
            y = portalPos.getY();
            z = portalPos.getZ();

            if (x == 0 && y == 0 && z == 0) error("No Nether portals are nearby.");
            else info("Found Nether portal at: X: %d, Y: %d, Z: %d".formatted(x, y, z));
            return SINGLE_SUCCESS;
        });
    }
}
