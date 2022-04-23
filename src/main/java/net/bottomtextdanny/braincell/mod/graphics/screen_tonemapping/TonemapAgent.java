package net.bottomtextdanny.braincell.mod.graphics.screen_tonemapping;

import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class TonemapAgent {
	private Vector3f channelModifier = new Vector3f();
	protected float saturationModifier;
	protected final LocalPlayer player;
	
	public TonemapAgent() {
		super();
		this.player = Minecraft.getInstance().player;
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
