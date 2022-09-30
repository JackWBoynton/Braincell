package bottomtextdanny.braincell.libraries.screen_tonemapping;

import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class TonemapAgent {
	private Vector3f channelModifier = new Vector3f();
	protected float saturationModifier;
	
	public TonemapAgent() {
		super();
	}

	@Nullable
	public LocalPlayer getPlayer() {
		return Minecraft.getInstance().player;
	}

	public abstract void render(float partialTick);
	
	public abstract void tick();

	public abstract boolean removeIf();
	
	public Vector3f getChannelModifier() {
		return this.channelModifier;
	}

	public void setChannelModifier(Vector3f channelModifier) {
		this.channelModifier = channelModifier;
	}

	public void setSaturationModifier(float saturationModifier) {
		this.saturationModifier = saturationModifier;
	}

	public float getSaturationModifier() {
		return this.saturationModifier;
	}
}
