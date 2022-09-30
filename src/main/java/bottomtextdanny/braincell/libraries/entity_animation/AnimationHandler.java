package bottomtextdanny.braincell.libraries.entity_animation;

import bottomtextdanny.braincell.libraries.mod.BCStaticData;
import bottomtextdanny.braincell.libraries.entity_animation.animations.Animation;
import bottomtextdanny.braincell.libraries.entity_animation.animations.AnimationData;
import bottomtextdanny.braincell.libraries.network.Connection;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

public class AnimationHandler {
	private int index;
	protected AnimationData animationMetadata = AnimationData.NULL;
	protected Animation<?> animation = Animation.NULL;
	@OnlyIn(Dist.CLIENT)
	protected int prevTick;
	protected int tick;

	public AnimationHandler() {
	}
	
	public void play(Entity entity, Animation<?> animation) {

		if (!entity.level.isClientSide()) {
			new MSGEntityAnimation(entity.getId(), this.index, animation.getIndex())
				.sendTo(PacketDistributor.ALL.with(() -> null));
		}

		this.animation.onEnd(this);
		Connection.doClientSide(() -> prevTick = 0);
		this.tick = 0;
		this.animation = animation;
		this.animationMetadata = animation.dataForPlay().get();
		this.animation.onStart(this);
	}

	public void update(int tick) {
		Connection.doClientSide(() -> prevTick = this.tick);
		this.tick = tick;
	}

	public void deactivate() {
		Connection.doClientSide(() -> prevTick = 0);
		this.tick = 0;
		this.animation.onEnd(this);
		this.animation = Animation.NULL;
	}

	public void setIndex(int index, BCAnimationToken token) {
		if (token == null) {
			throw new IllegalArgumentException("Invalid token.");
		}
        this.index = index;
	}

	public Animation<?> getAnimation() {
		return this.animation;
	}

	public AnimationData getData() {
		return this.animationMetadata;
	}

	public int getIndex() {
		return this.index;
	}
	
	public int getTick() {
		return this.tick;
	}

	public boolean isPlaying(Animation<?> animation) {
		return this.animation == animation;
	}

	public boolean isPlayingNull() {
		return this.animation == Animation.NULL;
	}

	@OnlyIn(Dist.CLIENT)
	public float linearProgress() {
		return this.tick + BCStaticData.partialTick();
	}
	
	@OnlyIn(Dist.CLIENT)
	public float dynamicProgress() {
		return Mth.lerp(BCStaticData.partialTick(), this.prevTick, this.tick);
	}
}
