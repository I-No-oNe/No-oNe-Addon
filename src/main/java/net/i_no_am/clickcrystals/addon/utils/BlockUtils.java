package net.i_no_am.clickcrystals.addon.utils;

import io.github.itzispyder.clickcrystals.Global;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class BlockUtils implements Global {

    /**
     * Finds the first BlockPos of the given block type within spherical render distance.
     */
    public static BlockPos findBlockPos(Block block) {
        int chunkCount = mc.levelRenderer.countRenderedSections();
        int radius = Math.min((int) Math.floor(Math.sqrt(chunkCount) * 16), 3000);

        BlockPos playerPos = mc.player.blockPosition();

        for (BlockPos pos : getBlocksInSphere(playerPos, radius)) {
            if (mc.level.getBlockState(pos).getBlock() == block) {
                return pos;
            }
        }

        return BlockPos.ZERO;
    }

    /**
     * Returns an Iterable that yields BlockPos within a spherical radius around the center.
     */
    private static Iterable<BlockPos> getBlocksInSphere(BlockPos center, int radius) {
        return () -> new Iterator<>() {
            private final int rSquared = radius * radius;
            private int x = -radius, y = -radius, z = -radius;
            private BlockPos nextPos = null;

            @Override
            public boolean hasNext() {
                if (nextPos != null) return true;

                while (y <= radius) {
                    double distSq = x * x + y * y + z * z;
                    if (distSq <= rSquared) {
                        nextPos = center.offset(x, y, z);
                        return true;
                    }

                    advance();
                }

                return false;
            }

            @Override
            public BlockPos next() {
                if (!hasNext()) throw new NoSuchElementException();
                BlockPos result = nextPos;
                nextPos = null;
                advance();
                return result;
            }

            private void advance() {
                z++;
                if (z > radius) {
                    z = -radius;
                    x++;
                    if (x > radius) {
                        x = -radius;
                        y++;
                    }
                }
            }
        };
    }

    /**
     * Checks if the player is looking directly at the given block type.
     */
    public static boolean isLookingAt(Block block) {
        if (mc.hitResult instanceof BlockHitResult hitResult) {
            BlockState state = mc.level.getBlockState(hitResult.getBlockPos());
            return state.is(block);
        }
        return false;
    }

    /**
     * Checks if the player is looking at a Respawn Anchor with the exact charge count.
     */
    public static boolean isAnchorLoaded(int charges) {
        if (!(mc.hitResult instanceof BlockHitResult hit)) return false;
        return isAnchorLoaded(charges, hit.getBlockPos());
    }

    /**
     * Checks if the Respawn Anchor at the given position has the exact charge count.
     */
    public static boolean isAnchorLoaded(int charges, BlockPos pos) {
        BlockState state = mc.level.getBlockState(pos);
        return state.getBlock() == Blocks.RESPAWN_ANCHOR &&
                state.getValue(RespawnAnchorBlock.CHARGE) == charges;
    }
}
