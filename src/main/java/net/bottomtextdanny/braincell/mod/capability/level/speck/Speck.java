package net.bottomtextdanny.braincell.mod.capability.level.speck;

import net.bottomtextdanny.braincell.mod.capability.level.BCLevelCapability;
import net.bottomtextdanny.braincell.mod._base.capability.CapabilityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Speck {
    private static final int MAX_UNLOADED_TICKS = 400;
    protected final Level level;
    private Vec3 position = Vec3.ZERO;
    public double xo, yo, zo;
    private int areaUnloadedTicks;
    private int ticksExisted;
    private boolean removed;

    public Speck(Level level) {
        super();
        this.level = level;
    }

    public final void baseTick() {
        this.ticksExisted++;
        if (!this.level.hasChunkAt(getBlockPosition())) {
            this.areaUnloadedTicks++;
            if (this.areaUnloadedTicks > MAX_UNLOADED_TICKS) {
                remove();
            }
        } else {
            this.areaUnloadedTicks = 0;
        }
        this.xo = this.position.x;
        this.yo = this.position.y;
        this.zo = this.position.z;
        tick();
    }

    protected void tick() {}

    public void remove() {
        this.removed = true;
    }

    public void addToLevel() {
        BCLevelCapability capability = CapabilityHelper.get(this.level, BCLevelCapability.TOKEN);
        capability.getSpeckManager().addSpeck(this);
    }

    public boolean isRemoved() {
        return removed;
    }

    public BlockPos getBlockPosition() {
        return new BlockPos(this.position);
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public double getPosX() {
        return this.position.x;
    }

    public double getPosY() {
        return this.position.y;
    }

    public double getPosZ() {
        return this.position.z;
    }

    public int getAreaUnloadedTicks() {
        return this.areaUnloadedTicks;
    }

    public int getTicksExisted() {
        return this.ticksExisted;
    }
}
