package bottomtextdanny.braincell.mod.entity.psyche.actions;

import bottomtextdanny.braincell.base.BCMath;
import bottomtextdanny.braincell.base.value_mapper.FloatMapper;
import bottomtextdanny.braincell.base.value_mapper.FloatMappers;
import bottomtextdanny.braincell.base.value_mapper.RandomIntegerMapper;
import bottomtextdanny.braincell.mod.entity.psyche.Action;
import net.minecraft.world.entity.PathfinderMob;

public class LookRandomlyAction extends Action {
   private static final RandomIntegerMapper DEFAULT_COOLDOWN_MAPPER = RandomIntegerMapper.of(150, 220);
   private static final FloatMapper NO_VERTICAL_ROTATION = FloatMappers.of(0.0F);
   private final RandomIntegerMapper cooldownMapper;
   private FloatMapper yLookLocationMapper;
   private int lookCooldownTracker;
   private float lookX;
   private float lookY;
   private float lookZ;
   private int lookTime;

   public LookRandomlyAction(PathfinderMob mob, RandomIntegerMapper cooldownMapper) {
      super(mob);
      this.yLookLocationMapper = NO_VERTICAL_ROTATION;
      this.cooldownMapper = cooldownMapper;
   }

   public LookRandomlyAction(PathfinderMob mob) {
      this(mob, DEFAULT_COOLDOWN_MAPPER);
   }

   public LookRandomlyAction vertical(FloatMapper mapper) {
      this.yLookLocationMapper = mapper;
      return this;
   }

   public boolean canStart() {
      if (!this.active()) {
         return false;
      } else if (this.lookCooldownTracker > 0) {
         --this.lookCooldownTracker;
         return false;
      } else {
         this.lookCooldownTracker = this.cooldownMapper.map(UNSAFE_RANDOM);
         return true;
      }
   }

   protected void start() {
      float d0 = 6.2831855F * this.mob.getRandom().nextFloat();
      this.lookX = BCMath.cos(d0);
      this.lookY = this.yLookLocationMapper.map(UNSAFE_RANDOM);
      this.lookZ = BCMath.sin(d0);
      this.lookTime = 20 + UNSAFE_RANDOM.nextInt(20);
   }

   protected void update() {
      --this.lookTime;
      this.mob.getLookControl().setLookAt(this.mob.getX() + (double)this.lookX, this.mob.getEyeY() + (double)this.lookY, this.mob.getZ() + (double)this.lookZ);
   }

   public boolean shouldKeepGoing() {
      return this.lookTime > 0;
   }
}
