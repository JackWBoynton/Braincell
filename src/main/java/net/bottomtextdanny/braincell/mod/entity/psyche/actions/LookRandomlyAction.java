package net.bottomtextdanny.braincell.mod.entity.psyche.actions;

import net.bottomtextdanny.braincell.base.BCMath;
import net.bottomtextdanny.braincell.base.value_mapper.FloatMapper;
import net.bottomtextdanny.braincell.base.value_mapper.FloatMappers;
import net.bottomtextdanny.braincell.base.value_mapper.RandomIntegerMapper;
import net.bottomtextdanny.braincell.mod._base.entity.psyche.Action;
import net.minecraft.world.entity.PathfinderMob;

public class LookRandomlyAction extends Action<PathfinderMob> {
    private static final RandomIntegerMapper DEFAULT_COOLDOWN_MAPPER = RandomIntegerMapper.of(150, 220);
    private static final FloatMapper NO_VERTICAL_ROTATION = FloatMappers.of(0.0F);
    private final RandomIntegerMapper cooldownMapper;
    private FloatMapper yLookLocationMapper = NO_VERTICAL_ROTATION;
    private int lookCooldownTracker;
    private float lookX;
    private float lookY;
    private float lookZ;
    private int lookTime;

    public LookRandomlyAction(PathfinderMob mob, RandomIntegerMapper cooldownMapper) {
        super(mob);
        this.cooldownMapper = cooldownMapper;
    }

    public LookRandomlyAction(PathfinderMob mob) {
        this(mob, DEFAULT_COOLDOWN_MAPPER);
    }

    public LookRandomlyAction vertical(FloatMapper mapper) {
        this.yLookLocationMapper = mapper;
        return this;
    }

    @Override
    public boolean canStart() {
        if (!active()) return false;

        if (this.lookCooldownTracker > 0) {
            this.lookCooldownTracker--;
            return false;
        }

        this.lookCooldownTracker = this.cooldownMapper.map(UNSAFE_RANDOM);
        return true;
    }

    @Override
    protected void start() {
        float d0 = (float) Math.PI * 2.0F * this.mob.getRandom().nextFloat();
        this.lookX = BCMath.cos(d0);
        this.lookY = this.yLookLocationMapper.map(UNSAFE_RANDOM);
        this.lookZ = BCMath.sin(d0);
        this.lookTime = 20 + UNSAFE_RANDOM.nextInt(20);
    }

    @Override
    protected void update() {
        --this.lookTime;
        this.mob.getLookControl().setLookAt(this.mob.getX() + this.lookX, this.mob.getEyeY() + this.lookY, this.mob.getZ() + this.lookZ);
    }

    @Override
    public boolean shouldKeepGoing() {
        return this.lookTime > 0;
    }
}
